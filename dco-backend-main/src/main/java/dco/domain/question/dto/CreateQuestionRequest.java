package dco.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dco.domain.question.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

    private String title;
    private String content;
    private Long groupId;
    private Category category;
    private String code;

    private boolean secret;
}
