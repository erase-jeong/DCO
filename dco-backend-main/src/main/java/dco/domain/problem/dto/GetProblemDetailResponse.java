package dco.domain.problem.dto;


import dco.domain.problem.entity.Problem;
import dco.domain.testcase.entity.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProblemDetailResponse {
    private long problemId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
    private List<TestCaseResponse> testCaseList;

    public static GetProblemDetailResponse toDto(Problem problem) {
        return GetProblemDetailResponse.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .content(problem.getContent())
                .startTime(problem.getStartTime())
                .endTime(problem.getEndTime())
                .testCaseList(problem.getTestCaseList().stream()
                        .map(TestCaseResponse::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestCaseResponse {
        private Integer number;
        private String input;
        private String output;

        public static TestCaseResponse toDto(TestCase testCase) {
            return TestCaseResponse.builder()
                    .number(testCase.getNumber())
                    .input(testCase.getInput())
                    .output(testCase.getOutput())
                    .build();
        }
    }
}
