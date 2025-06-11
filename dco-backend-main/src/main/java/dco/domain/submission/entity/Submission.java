package dco.domain.submission.entity;

import dco.domain.common.BaseEntity;
import dco.domain.common.Language;
import dco.domain.group.entity.Group;
import dco.domain.member.entity.Member;
import dco.domain.problem.entity.Problem;
import dco.domain.submission.dto.SubmissionRequest;
import dco.domain.submission.dto.SubmissionResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long submissionId;

    private Long runtime;
    private String errorMessage;
    private Float score;
    @Enumerated(value = EnumType.STRING)
    private Language language;
    private String code;
    private Integer codeLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Submission(Long runtime, String errorMessage, Float score, Language language, String code,
        Integer codeLength,
        Member member, Problem problem, Group group) {
        this.runtime = runtime;
        this.errorMessage = errorMessage;
        this.score = score;
        this.language = language;
        this.code = code;
        this.codeLength = codeLength;
        this.member = member;
        this.problem = problem;
        this.group = group;
    }

    public static Submission createSubmission(SubmissionRequest request, SubmissionResponse response, Member member, Problem problem) {
        return Submission.builder()
            .runtime(response.getRuntime())
            .errorMessage(response.getErrorMessage())
            .score(response.getScore())
            .language(request.getLanguage())
            .code(request.getCode())
            .codeLength(request.getCode().length())
            .member(member)
            .problem(problem)
            .group(problem.getGroup())
            .build();
    }
}