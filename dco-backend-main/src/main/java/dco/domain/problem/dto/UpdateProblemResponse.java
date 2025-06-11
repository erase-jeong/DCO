package dco.domain.problem.dto;

import dco.domain.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProblemResponse {
    private Long problemId; // 생성된 문제의 ID
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String content;
    private Integer javaExecuteTimeLimit;
    private Integer cppExecuteTimeLimit;
    private Integer pythonExecuteTimeLimit;

    public static UpdateProblemResponse toDto(Problem problem) {
        return UpdateProblemResponse.builder()
                .problemId(problem.getProblemId())
                .content(problem.getContent())
                .title(problem.getTitle())
                .startTime(problem.getStartTime())
                .endTime(problem.getEndTime())
                .javaExecuteTimeLimit(problem.getJavaExecuteTimeLimit())
                .cppExecuteTimeLimit(problem.getCppExecuteTimeLimit())
                .pythonExecuteTimeLimit(problem.getPythonExecuteTimeLimit())
                .build();
    }
}
