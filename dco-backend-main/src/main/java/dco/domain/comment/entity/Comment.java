package dco.domain.comment.entity;

import dco.domain.comment.dto.CreateCommentRequest;
import dco.domain.comment.dto.UpdateCommentRequest;
import dco.domain.group.entity.Group;
import dco.domain.member.entity.Member;
import dco.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, length = 1000)
    private String content;

    //부모 댓글이 삭제되었는지 아닌지 확인하는 용도
    @ColumnDefault("FALSE")
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; //대댓글 구현시 필요 (부모는 null, 자식 댓글은 부모 댓글의 번호)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @BatchSize(size = 200)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(nullable = false)
    private boolean secret;

    @Builder
    public Comment(Member member, String content, Question question, Comment parent, Group group, boolean secret) {
        this.member = member;
        this.content = content;
        this.question = question;
        this.parent = parent;
        this.group = group;
        this.secret = secret;
    }

    public static Comment createComment(CreateCommentRequest request, Group group){
        return Comment.builder()
                .group(group)
                .content(request.getContent())
                .secret(request.isSecret())
                .build();
    }

    public void updateComment(UpdateCommentRequest request){
        this.content = request.getContent();
        this.secret = request.isSecret();
    }

    public void updateDeleted(){
        this.isDeleted = true;
    }
}
