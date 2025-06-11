package dco.domain.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dco.domain.member.dto.GetGithubResponse;
import dco.domain.member.dto.GithubRequest;
import dco.domain.member.dto.GroupJoinLoginRequest;
import dco.domain.member.dto.GroupJoinLoginResponse;
import dco.domain.member.dto.LogInRequest;
import dco.domain.member.dto.RefreshTokenRequest;
import dco.domain.member.dto.SignUpRequest;
import dco.domain.member.service.MemberService;
import dco.global.auth.CustomUserDetails;
import dco.global.auth.JwtToken;
import dco.global.auth.JwtTokenProvider;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "회원 관리 API", description = "회원 관련 API endpoints")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입", description = """
    code: String, 6자리 이상
    
    password: 비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.
    
    role: STUDENT, PROFESSOR, ADMIN 중에서 택 1
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @PostMapping("/signup")
    public ResponseEntity<Response<Void>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        return ResponseEntity.ok(new Response<>(null, "회원가입이 완료되었습니다!"));
    }

    @Operation(summary = "로그인", description = """
    로그인 기능입니다.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @PostMapping("/login")
    public ResponseEntity<Response<JwtToken>> logIn(@Valid @RequestBody LogInRequest logInDto) {
        String code = logInDto.getCode();
        String password = logInDto.getPassword();


        JwtToken jwtToken = memberService.logIn(code, password);
        log.debug("request code = {}, password = {}", code, password);
        log.debug("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return ResponseEntity.ok(new Response<>(jwtToken, "로그인 성공."));

    }

    @Operation(summary = "그룹 초대 링크를 눌렀을 경우 필요한 로그인", description = """
    STUDENT가 아니면 권한 문제 발생. 이에 대해서는 메인 페이지로 Redirect 처리.
    로그인 성공시 프론트에서 그룹 초대 수락/거절 페이지로 Redirect 처리.
    
    """)
    @PostMapping("/login/group-join")
    public ResponseEntity<Response<GroupJoinLoginResponse>> logInByGroupJoin(@Valid @RequestBody GroupJoinLoginRequest logInDto) {
        String code = logInDto.getCode();
        String password = logInDto.getPassword();


        JwtToken jwtToken = memberService.logIn(code, password);
        log.debug("request code = {}, password = {}", code, password);
        log.debug("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        // 학생들만 초대가 가능하도록 구현
        memberService.isStudent();
        // 프론트에서 그룹 초대 수락/거절 페이지로 Redirect 할 수 있도록 구현해야 한다.
        return ResponseEntity.ok(new Response<>(GroupJoinLoginResponse.from(jwtToken, logInDto.getInviteUrl()), "로그인 성공."));

    }

    @Operation(summary = "Access Token 재발급", description = "Access Token 만료시 기존에 발급받은 Refresh Token을 이쪽으로 보내서 새로운 Access Token 받아가기")
    @PostMapping("/member/refresh")
    public ResponseEntity<Response<JwtToken>> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtToken token = jwtTokenProvider.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(new Response<>(token, "Access Token 재발급 완료."));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인 된 계정의 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/member/logout")
    public ResponseEntity<Response<Void>> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String studentId = customUserDetails.getUsername();
        jwtTokenProvider.deleteRefreshToken(studentId);
        return ResponseEntity.ok(new Response<>(null, "로그아웃 되었습니다."));
    }

    @Operation(summary = "swagger test", description = "swagger 테스트를 위한 엔드포인트 입니다.")
    @GetMapping("/member/test")
    public ResponseEntity<Response<List<Integer>>> test() {
        List<Integer> testList = List.of(1, 2, 3, 4, 5); //테스트
        return ResponseEntity.ok(new Response<>(testList, "테스트 리스트 반환 완료"));
    }

    @Operation(summary = "깃허브 정보 입력", description = "제출시 업로드 될 깃허브의 정보(accessToken, repository, name)를 입력")
    @PostMapping("/member/github")
    public ResponseEntity<Response<Void>> getGithub(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody GithubRequest githubRequest){
        String studentId = customUserDetails.getUsername();
        memberService.uploadGit(studentId, githubRequest);
        return ResponseEntity.ok(new Response<>(null, "사용자의 정보가 저장되었습니다."));
    }

    @Operation(summary = "깃허브 정보 조회", description = "입력된 사용자의 깃허브 정보(accessToken, repository, name)를 반환")
    @GetMapping("/member/github")
    public ResponseEntity<Response<GetGithubResponse>> githubDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String studentId = customUserDetails.getUsername();

        GetGithubResponse response = memberService.githubDetail(studentId);

        if(response.getGithubToken() == null){
            return ResponseEntity.ok(new Response<>(response, "등록된 정보가 없습니다."));
        }

        return ResponseEntity.ok(new Response<>(response, "사용자의 깃허브 정보가 조회 완료되었습니다."));
    }
}
