package dco.domain.submission.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionResponse {
    private Float score; // 점수
    private List<GradeResponse> gradeResponseList;
    private String errorMessage;
    private Long runtime; // 실행 시간
}
