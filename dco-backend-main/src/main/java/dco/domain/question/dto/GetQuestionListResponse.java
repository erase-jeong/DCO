package dco.domain.question.dto;

import dco.domain.member.entity.Member;
import dco.domain.notification.dto.GetNotificationResponse;
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
public class GetQuestionListResponse {

    private Long questionId;
    private String title;
    private String content; //글자수 잘라서 넘겨주기
    private String name;
    private Category category;
    private boolean secret;

    public static GetQuestionListResponse toDto(Question question){
        return GetQuestionListResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .name(question.getMember().getName())
                .category(question.getCategory())
                .secret(question.isSecret())
                .build();
    }
}
