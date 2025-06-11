package dco.domain.question.entity;

import dco.domain.group.entity.Group;
import dco.domain.member.entity.Member;
import dco.domain.question.dto.CreateQuestionRequest;
import dco.domain.question.dto.UpdateQuestionRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Category category;

    @Column
    private String code;

    @Column
    private boolean secret; //비밀글 설정

    @Builder
    public Question(String title, String content, Group group, Member member, Category category, String code, boolean secret){
        this.title = title;
        this.content = content;
        this.group = group;
        this.member = member;
        this.category = category;
        this.code = code;
        this.secret = secret;
    }

    public static Question createQuestion(CreateQuestionRequest request, Group group, Member member, Category category){
        return Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .group(group)
                .member(member)
                .category(category)
                .code(request.getCode())
                .secret(request.isSecret())
                .build();
    }

    public void updateQuestion(UpdateQuestionRequest request){
        this.title = request.getTitle();
        this.content = request.getContent();
        this.code = request.getCode();
        this.secret = request.isSecret();
    }
}
