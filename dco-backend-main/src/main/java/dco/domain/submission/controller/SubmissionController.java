package dco.domain.submission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dco.domain.submission.dto.SubmissionMemberScoreResponse;
import dco.domain.submission.dto.SubmissionRequest;
import dco.domain.submission.dto.SubmissionResponse;
import dco.domain.submission.dto.out.GetRankingResponse;
import dco.domain.submission.service.SubmissionScoreService;
import dco.domain.submission.service.SubmissionService;
import dco.global.auth.CustomUserDetails;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission")
@Slf4j
public class SubmissionController {

    /**
     * 채점을 위한 테스트 컨트롤러 입니다.
     */
    private final SubmissionService submissionService;
    private final SubmissionScoreService submissionScoreService;

    @Operation(summary = "문제 제출", description = "문제를 제출합니다.")
    @PostMapping("/member")
    public ResponseEntity<Response> processSubmission(@AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody SubmissionRequest request) throws Exception {
        SubmissionResponse submissionResponse = submissionService.processSubmission(customUserDetails.getUsername(),
            request);
        return ResponseEntity.ok(new Response(submissionResponse, "컴파일 완료"));
    }

    @Operation(summary = "개인 제출 현황", description = "교수 권한")
    @GetMapping("/professor/{groupId}/{memberId}")
    public ResponseEntity<Response<List<SubmissionMemberScoreResponse>>> submissionMemberScore(@PathVariable Long groupId, @PathVariable Long memberId) {
        // 인스턴스를 통해 메서드 호출
        List<SubmissionMemberScoreResponse> response = submissionScoreService.SubmissionMemberScore(groupId, memberId);
        return ResponseEntity.ok(new Response<>(response, "개인별 제출 현황 조회가 완료되었습니다."));
    }

    @Operation(summary = "특정 문제의 Top3 제출 명단", description = "고득점 순, 동점자는 제출 시간 순")
    @GetMapping("/member/submission/ranks/{problemId}")
    public ResponseEntity<Response<List<GetRankingResponse>>> getRanking(@PathVariable Long problemId) {
        // 인스턴스를 통해 메서드 호출
        List<GetRankingResponse> response = submissionScoreService.getTop3Rankings(problemId);
        return ResponseEntity.ok(new Response<>(response, "Top3 제출 명단 조회가 완료되었습니다."));
    }
}
