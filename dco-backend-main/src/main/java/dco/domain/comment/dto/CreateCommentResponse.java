package dco.domain.comment.dto;

import dco.domain.comment.entity.Comment;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCommentResponse {

    private Long commentId;
    private String memberName;
    private String content;
    private boolean secret;

    public static CreateCommentResponse toDto(Comment comment){
        return CreateCommentResponse.builder()
                .commentId(comment.getCommentId())
                .memberName(comment.getMember().getName())
                .content(comment.getContent())
                .secret(comment.isSecret())
                .build();
    }
}
