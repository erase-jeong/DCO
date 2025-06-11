package dco.domain.question.dto;

import dco.domain.question.entity.Category;
import dco.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionDetailResponse {

    private Long questionId;
    private String title;
    private String content;
    private String name;
    private Category category;
    private String code;

    public static GetQuestionDetailResponse toDto(Question question){
        return GetQuestionDetailResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .name(question.getMember().getName())
                .category(question.getCategory())
                .code(question.getCode())
                .build();
    }
}
