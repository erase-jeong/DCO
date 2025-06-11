package dco.domain.submission.service;

import dco.domain.submission.dto.GradeResponse;
import dco.domain.submission.dto.SubmissionResponse;
import dco.domain.submission.entity.Result;
import dco.domain.testcase.entity.TestCase;
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
public class CppRunner {

    private final FileService fileService;

    public SubmissionResponse run(String code, List<TestCase> testCaseList, Integer timeLimit) throws Exception {
        List<GradeResponse> gradeResponseList = new ArrayList<>();

        // 고유한 디렉토리 생성
        String uniqueID = UUID.randomUUID().toString();
        Path dirPath = Paths.get(fileService.getRunningDir(), uniqueID);
        fileService.createDirectory(dirPath);

        // C++ 소스 코드를 파일로 작성
        Path sourcePath = dirPath.resolve("main.cpp");
        Files.write(sourcePath, code.getBytes());

        // c++ 소스 코드 컴파일
        ProcessBuilder gpp = new ProcessBuilder("g++", "-o", "main.exe", "main.cpp");
        gpp.directory(dirPath.toFile());
        Process gppProcess = gpp.start();

        // 컴파일 오류 발생 처리
        int gppResult = gppProcess.waitFor();
        if (gppResult != 0) {
            String errorMessage = fileService.readProcessOutput(gppProcess);
            fileService.cleanUpDirectory(dirPath);
            return new SubmissionResponse(0F, null, "Compilation error:\n" + errorMessage, 0L);
        }

        int correctCount = 0; // 맞은 테스트 케이스 개수
        long maxRuntime = 0;  // 최대 실행 시간

        // 각 테스트 케이스에 대해 코드 실행
        for (TestCase testCase : testCaseList) {
            // 예제 입력 데이터를 파일로 작성
            Path inputPath = dirPath.resolve("input.txt");
            Files.write(inputPath, testCase.getInput().getBytes());

            // c++ 코드 실행
            Path executablePath = dirPath.resolve("main.exe");
            ProcessBuilder cppRun = new ProcessBuilder(executablePath.toString());
            cppRun.directory(dirPath.toFile());
            cppRun.redirectInput(inputPath.toFile());

            long startTime = System.currentTimeMillis(); // 시간 측정 시작
            Process cppRunProcess = cppRun.start();
            String result = fileService.readProcessOutput(cppRunProcess); // 프로그램 실행 결과 읽기
            long endTime = System.currentTimeMillis(); // 시간 측정 종료

            // 오류 발생 처리
            int exitCode = cppRunProcess.waitFor();
            if (exitCode != 0) {
                String errorMessage = fileService.readProcessOutput(cppRunProcess);
                fileService.cleanUpDirectory(dirPath);
                return new SubmissionResponse(0F, null, errorMessage, null);
            }

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