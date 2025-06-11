package dco.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    private Long groupId;
    private Long commentId;
    private String content;
    private boolean secret;
}
