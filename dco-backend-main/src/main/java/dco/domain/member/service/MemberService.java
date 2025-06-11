package dco.domain.member.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dco.domain.member.dto.GetGithubResponse;
import dco.domain.member.dto.GithubRequest;
import dco.domain.member.dto.SignUpRequest;
import dco.domain.member.entity.Member;
import dco.domain.member.entity.Role;
import dco.domain.member.repository.MemberRepository;
import dco.global.auth.JwtToken;
import dco.global.auth.JwtTokenProvider;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByCode(request.getCode())) {
            throw new CustomException(ErrorCode.STUDENT_ID_DUPLICATION);
        }
        // 넘겨받은 비밀번호를 인코딩하여 DB에 저장한다.
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 정적 팩토리 메서드를 사용하여 Member 객체 생성
        Member member = Member.createMember(request, encodedPassword, request.getRole());
        memberRepository.save(member);
    }

    public JwtToken logIn(String code, String password) {
        // 1. code + password 기반으로 Authentication 객체 생성
        // 로그인 요청시에는 아직 미인증 상태이므로 authentication은 인증 여부를 확인하는 authenticated 값이 false 상태이다.

        try {
            // 1. code + password 기반으로 Authentication 객체 생성
            // 2. AuthenticationManager를 통한 인증 요청
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(code, password)
            );
            // 인증된 Authentication 객체 봔환
            // 인증 상태이므로 authentication은 인증 여부를 확인하는 authenticated 값이 true 상태이다.
            return jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.LOGIN_FAILURE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    public GetGithubResponse githubDetail(String studentId){

        Member member = memberRepository.findByCode(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return GetGithubResponse.builder()
                .githubName(member.getGithubName())
                .githubToken(member.getGithubToken())
                .repositoryName(member.getRepositoryName())
                .build();
    }

    public void isStudent() {
        Member member = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.STUDENT) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }

    public void uploadGit(String studentId, GithubRequest githubRequest){
        memberRepository.updateGithub(studentId, githubRequest.getGithubToken(),githubRequest.getGithubName(),githubRequest.getRepositoryName());
    }
}
