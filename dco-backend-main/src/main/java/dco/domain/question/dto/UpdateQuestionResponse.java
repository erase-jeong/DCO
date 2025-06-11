package dco.domain.question.dto;

import dco.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionResponse {

    private Long questionId;
    private String title;
    private String content;
    private String code;
    private boolean secret;

    public static UpdateQuestionResponse toDto(Question question){
        return UpdateQuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .code(question.getCode())
                .secret(question.isSecret())
                .build();
    }
}
