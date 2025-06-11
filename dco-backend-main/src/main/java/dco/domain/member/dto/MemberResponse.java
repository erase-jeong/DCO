package dco.domain.member.dto;

import dco.domain.member.entity.Member;
import dco.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberResponse {
    private Long memberId;
    private String code;
    private String name;
    private Role role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .code(member.getCode())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
