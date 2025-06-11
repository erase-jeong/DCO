package dco.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogInRequest {
    @Schema(description = "식별번호는 6자리 이상의 숫자가 되어야 합니다.", example = "1630202", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "식별번호는 필수 입력 값입니다.")
    private String code;

    @Schema(description = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.", example = "!test1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}