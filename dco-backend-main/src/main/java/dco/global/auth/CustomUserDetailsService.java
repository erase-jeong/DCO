package dco.global.auth;

import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByCode(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private CustomUserDetails createUserDetails(Member member) {
        return CustomUserDetails.builder()
                .member(member)
                .build();
    }
}