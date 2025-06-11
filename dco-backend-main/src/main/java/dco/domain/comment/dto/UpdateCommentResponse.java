package dco.domain.comment.dto;

import dco.domain.comment.entity.Comment;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateCommentResponse {
    private String content;
    private Long commentId;
    private boolean secret;

    public static UpdateCommentResponse toDto(Comment comment){
        return UpdateCommentResponse.builder()
                .content(comment.getContent())
                .commentId(comment.getCommentId())
                .secret(comment.isSecret())
                .build();
    }
}
