package dco.domain.submission.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionMemberScoreResponse {
    private Long memberId;
    private String name;
    private Long problemId;
    private String title;
    private Long score;
    private LocalDateTime submitTime;
    //@Enumerated(value = EnumType.STRING)
    private String language;

}
