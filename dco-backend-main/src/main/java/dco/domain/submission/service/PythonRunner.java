package dco.domain.submission.service;

import dco.domain.submission.dto.GradeResponse;
import dco.domain.submission.dto.SubmissionResponse;
import dco.domain.submission.entity.Result;
import dco.domain.testcase.entity.TestCase;
import dco.global.util.RunnerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PythonRunner {

    private final FileService fileService;

    public SubmissionResponse run(String code, List<TestCase> testCaseList, Integer timeLimit) throws Exception {
        List<GradeResponse> gradeResponseList = new ArrayList<>();

        // 고유한 디렉토리 생성
        String uniqueID = UUID.randomUUID().toString();
        Path dirPath = Paths.get(fileService.getRunningDir(), uniqueID);
        fileService.createDirectory(dirPath);

        // python 소스 코드를 파일로 작성
        Path sourcePath = dirPath.resolve("script.py");
        Files.write(sourcePath, code.getBytes());

        int correctCount = 0; // 맞은 테스트 케이스 개수
        long maxRuntime = 0;  // 테스트 케이스 중 최대 실행 시간을 할당

        // 각 테스트 케이스에 대해 코드 실행
        for (TestCase testCase : testCaseList) {
            // 예제 입력 데이터를 파일로 작성
            Path inputPath = dirPath.resolve("input.txt");
            Files.write(inputPath, testCase.getInput().getBytes());

            // python 소스 코드 실행
            ProcessBuilder python = new ProcessBuilder("/usr/bin/python3", "script.py");
            python.directory(dirPath.toFile());
            python.redirectInput(inputPath.toFile());

            long startTime = System.currentTimeMillis(); // 시간 측정 시작
            Process pythonProcess = python.start();
            String result = fileService.readProcessOutput(pythonProcess); // 프로그램 실행 결과 읽기
            long endTime = System.currentTimeMillis(); // 시간 측정 종료

            // 최대 실행 시간 구하기
            maxRuntime = RunnerUtil.getMaxRuntime(maxRuntime, startTime, endTime);

            // 오류 발생 처리
            int exitCode = pythonProcess.waitFor();
            if (exitCode != 0) {
                String errorMessage = fileService.readProcessOutput(pythonProcess);
                fileService.cleanUpDirectory(dirPath);
                return new SubmissionResponse(0F, null, errorMessage, maxRuntime);
            }

            // 결과 비교
            if ((endTime - startTime) > timeLimit) {
                gradeResponseList.add(new GradeResponse(testCase.getNumber(), Result.TIMEOUT));
            }
            else if (result.trim().equals(testCase.getOutput().trim())) {
                gradeResponseList.add(new GradeResponse(testCase.getNumber(), Result.CORRECT));
                correctCount++;
            } else {
                gradeResponseList.add(new GradeResponse(testCase.getNumber(), Result.INCORRECT));
            }
        }

        // 디렉토리 정리
        fileService.cleanUpDirectory(dirPath);

        // 점수 계산
        float score = RunnerUtil.calculateScore(correctCount, testCaseList.size());

        return new SubmissionResponse(score, gradeResponseList, null, maxRuntime);
    }
}