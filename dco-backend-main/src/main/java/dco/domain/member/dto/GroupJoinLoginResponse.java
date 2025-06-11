package dco.domain.member.dto;

import dco.global.auth.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Builder
public class GroupJoinLoginResponse {
    private JwtToken jwtToken;
    private String inviteUrl;

    public static GroupJoinLoginResponse from(JwtToken jwtToken, String inviteUrl) {
        return GroupJoinLoginResponse.builder()
                .jwtToken(jwtToken)
                .inviteUrl(inviteUrl)
                .build();
    }
}
