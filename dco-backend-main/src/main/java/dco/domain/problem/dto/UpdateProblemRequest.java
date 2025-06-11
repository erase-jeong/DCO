package dco.domain.problem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class UpdateProblemRequest {

    @Schema(description = "문제 제목", example = "두 개의 수 덧셈", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "최소 1자리 이상 입력해야합니다.")
    private String title;          // 문제 제목

    //    @Schema(description = "문제 제출 가능한 시작 시간", example = "2024-08-15T10:30:45.123", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "최소 1자리 이상 입력해야합니다.")
    private LocalDateTime startTime;  // 문제 제출 가능한 시작 시간

    //    @Schema(description = "문제 제출 마감 시간", example = "2024-08-17T10:30:45.123", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotBlank(message = "최소 1자리 이상 입력해야합니다.")
    private LocalDateTime endTime;    // 문제 제출 마감 시간


    @Schema(description = "제출 횟수 제한", example = "10")
    private Integer submitLimit; // 제출 횟수 제한
    @Schema(description = "문제 내용", example = "별찍기")
    private String content; // 문제 내용
    @Schema(description = "파이썬 실행 제한 시간", example = "1000")
    private Integer pythonExecuteTimeLimit;
    @Schema(description = "c++ 실행 제한 시간", example = "1000")
    private Integer cppExecuteTimeLimit;
    @Schema(description = "자바 실행 제한 시간", example = "1000")
    private Integer javaExecuteTimeLimit;
    @Schema(description = "groupId", example = "1")
    private Long groupId;
    @Schema(description = "problemId", example = "1")
    private Long problemId;
}
