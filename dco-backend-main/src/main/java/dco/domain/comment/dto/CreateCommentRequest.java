package dco.domain.comment.dto;

import dco.domain.comment.entity.Comment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함한 생성자 (필요 시)
public class CreateCommentRequest {

        private Long groupId;
        private Long questionId;
        private Long parentCommentId; //대댓글이라면 번호 아니면 null
        private String content;
        private boolean secret;
}
