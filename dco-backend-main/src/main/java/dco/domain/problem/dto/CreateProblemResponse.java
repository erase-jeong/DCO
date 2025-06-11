package dco.domain.problem.dto;

import dco.domain.group.entity.Group;
import dco.domain.problem.entity.Problem;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProblemResponse {
    private Long problemId; // 생성된 문제의 ID
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String content;
    private Integer javaExcuteTimeLimit;
    private Integer cppExcuteTimeLimit;
    private Integer pythonExcuteTimeLimit;

    public static CreateProblemResponse toDto(Problem problem) {
        return CreateProblemResponse.builder()
                .problemId(problem.getProblemId())
                .content(problem.getContent())
                .title(problem.getTitle())
                .startTime(problem.getStartTime())
                .endTime(problem.getEndTime())
                .javaExcuteTimeLimit(problem.getJavaExecuteTimeLimit())
                .cppExcuteTimeLimit(problem.getCppExecuteTimeLimit())
                .pythonExcuteTimeLimit(problem.getPythonExecuteTimeLimit())
                .build();
    }
}