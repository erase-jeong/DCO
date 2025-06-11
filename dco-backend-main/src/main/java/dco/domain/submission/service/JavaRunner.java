package dco.domain.submission.service;

import dco.domain.submission.dto.GradeResponse;
import dco.domain.submission.dto.SubmissionResponse;
import dco.domain.submission.entity.Result;
import dco.domain.testcase.entity.TestCase;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.RunnerUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JavaRunner {

    private final FileService fileService;

    public SubmissionResponse run(String code, List<TestCase> testCaseList, Integer timeLimit) throws Exception {
        List<GradeResponse> gradeResponseList = new ArrayList<>();

        // 고유한 디렉토리 생성
        String uniqueID = UUID.randomUUID().toString();
        Path dirPath = Paths.get(fileService.getRunningDir(), uniqueID);
        fileService.createDirectory(dirPath);

        // java 소스 코드를 파일로 작성
        Path sourcePath = dirPath.resolve("Main.java");
        Files.write(sourcePath, code.getBytes());

        // java 소스 코드 컴파일
        ProcessBuilder javac = new ProcessBuilder("javac", "-encoding", "UTF-8", "Main.java");
        javac.directory(dirPath.toFile());
        Process javacProcess = javac.start();

        // 컴파일 오류 발생 처리
        if (javacProcess.waitFor() != 0) {
            String errorMessage = fileService.readProcessOutput(javacProcess);
            fileService.cleanUpDirectory(dirPath);
            return new SubmissionResponse(0F, null, errorMessage, null);
        }

        StringBuilder resultBuilder = new StringBuilder();

        int correctCount = 0; // 맞은 테스트 케이스 개수
        long maxRuntime = 0;  // 최대 실행 시간

        // 각 테스트 케이스에 대해 코드 실행
        for (TestCase testCase : testCaseList) {
            // 예제 입력 데이터를 파일로 작성
            Path inputPath = dirPath.resolve("input.txt");
            Files.write(inputPath, testCase.getInput().getBytes());

            // java 코드 실행
            ProcessBuilder java = new ProcessBuilder("java", "Main");
            java.directory(dirPath.toFile());
            java.redirectInput(inputPath.toFile());

            long startTime = System.currentTimeMillis(); // 시간 측정 시작
            Process javaProcess = java.start();
            String result = fileService.readProcessOutput(javaProcess); // 프로그램 실행 결과 읽기
            long endTime = System.currentTimeMillis(); // 시간 측정 종료

            // 최대 실행 시간 구하기
            maxRuntime = RunnerUtil.getMaxRuntime(maxRuntime, startTime, endTime);

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