package dco.domain.problem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dco.domain.group.entity.Group;
import dco.domain.problem.dto.CreateProblemRequest;
import dco.domain.problem.dto.UpdateProblemRequest;
import dco.domain.problem.service.ProblemService;
import dco.domain.testcase.entity.TestCase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE problem SET deleted_at = NOW() WHERE problem_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long problemId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime; // 제출 가능 시작 시각

    @Column(nullable = false)
    private LocalDateTime endTime; // // 제출 가능 종료 시각

    @Column(nullable = false)
    private Integer submitLimit; // 제출 횟수 제한

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer pythonExecuteTimeLimit;

    @Column(nullable = false)
    private Integer cppExecuteTimeLimit;

    @Column(nullable = false)
    private Integer javaExecuteTimeLimit;

    private LocalDateTime deletedAt; // 논리 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "problem")
    private List<TestCase> testCaseList = new ArrayList<>();

    @Builder
    public Problem(String title, LocalDateTime startTime, LocalDateTime endTime, Integer submitLimit, String content,
        Group group, Integer pythonExecuteTimeLimit, Integer cppExecuteTimeLimit, Integer javaExecuteTimeLimit) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.submitLimit = submitLimit;
        this.content = content;
        this.group = group;
        this.pythonExecuteTimeLimit = pythonExecuteTimeLimit;
        this.cppExecuteTimeLimit = cppExecuteTimeLimit;
        this.javaExecuteTimeLimit = javaExecuteTimeLimit;

        this.testCaseList = new ArrayList<>();
    }

    // 정적 팩토리 메서드
    public static Problem createProblem(CreateProblemRequest request, Group group) {
        return Problem.builder()
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .submitLimit(request.getSubmitLimit())
                .content(request.getContent())
                .javaExecuteTimeLimit(request.getJavaExecuteTimeLimit())
                .cppExecuteTimeLimit(request.getCppExecuteTimeLimit())
                .pythonExecuteTimeLimit(request.getPythonExecuteTimeLimit())
                .group(group)
                .build();
    }

    public void updateProblem(UpdateProblemRequest request) {
        this.title = request.getTitle();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.submitLimit = request.getSubmitLimit();
        this.content = request.getContent();
        this.javaExecuteTimeLimit = request.getJavaExecuteTimeLimit();
        this.cppExecuteTimeLimit = request.getCppExecuteTimeLimit();
        this.pythonExecuteTimeLimit = request.getPythonExecuteTimeLimit();
    }
}
