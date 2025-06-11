package dco.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequest {
    private Long questionId;
    private String title;
    private String content;
    private Long groupId;
    private String code;
    private boolean secret;
}
