package dco.domain.submission.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class FileService {

    private final String RUNNING_DIR = "./running-file";

    public String getRunningDir() {
        return RUNNING_DIR;
    }

    /**
     * 코드 파일 생성
     */
    public void createDirectory(Path dirPath) throws Exception {
        Files.createDirectories(dirPath);
    }

    /**
     * 결과 읽기
     */
    public String readProcessOutput(Process process) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        if (errorOutput.length() > 0) {
            log.error("Process error: {}", errorOutput.toString());
            return "Error: " + errorOutput.toString();
        }

        return output.toString();
    }

    /**
     * 파일 제거
     */
    public void cleanUpDirectory(Path dirPath) {
        try {
            Files.walk(dirPath)
                .map(Path::toFile)
                .forEach(File::delete);
            Files.deleteIfExists(dirPath);
        } catch (Exception e) {
            log.error("Failed to clean up directory: {}", dirPath, e);
        }
    }
}