package dco.domain.testcase.entity;

import dco.domain.problem.dto.CreateTestcaseRequest;
import dco.domain.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;

    @Column(nullable = false)
    private Integer number;
    @Column(columnDefinition = "TEXT")
    private String input;
    @Column(columnDefinition = "TEXT")
    private String output;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Builder
    public TestCase(Integer number, String input, String output, Problem problem) {
        this.number = number;
        this.input = input;
        this.output = output;
        this.problem = problem;
    }

    public static TestCase createTestCase(Problem problem, CreateTestcaseRequest request) {
        return TestCase.builder()
                .number(request.getNumber())
                .input(request.getInput())
                .output(request.getOutput())
                .problem(problem)
                .build();
    }
}
