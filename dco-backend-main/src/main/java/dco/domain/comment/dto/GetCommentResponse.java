package dco.domain.comment.dto;

import dco.domain.comment.entity.Comment;
import dco.domain.member.entity.Role;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResponse {
    private Long commentId;
    private String memberName;
    private String content;
    private Role role;
    private Boolean isDeleted;
    private List<GetCommentResponse> children;
    private boolean secret;

    public static GetCommentResponse toDto(Comment comment) {
        // 자식 댓글들을 재귀적으로 변환
        List<GetCommentResponse> childResponses = comment.getChildren().stream()
                .map(GetCommentResponse::toDto)
                .collect(Collectors.toList());

        // 부모 댓글에 대한 GetCommentResponse 생성
        return GetCommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(isDeletedComment(comment))
                .isDeleted(comment.getIsDeleted())
                .memberName(comment.getMember().getName()) // 댓글 작성자의 이름을 Member 엔티티에서 가져옴
                .children(childResponses)
                .role(comment.getMember().getRole())
                .secret(comment.isSecret())
                .build();
    }

    public static String isDeletedComment(Comment comment){
        if(comment.getIsDeleted()){
            return "삭제된 댓글입니다.";
        }else{
            return comment.getContent();
        }
    }
}
