package dco.domain.submission.dto;

import dco.domain.common.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SubmissionRequest {
    @Schema(description = "problemId", example = "1")
    private Long problemId;
    @Schema(description = "언어", example = "PYTHON")
    private Language language;
    @Schema(description = "유저 코드", example = "a = int(input())\nprint(a ** 2)")
    private String code;
}
