package dco.domain.problem.dto;

import dco.domain.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProblemListResponse {
    private Long problemId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static GetProblemListResponse toDto(Problem problem) {
        return GetProblemListResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .startTime(problem.getStartTime())
                .endTime(problem.getEndTime())
                .build();
    }
}
