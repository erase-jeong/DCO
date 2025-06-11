package dco.domain.problem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dco.domain.problem.dto.CreateProblemRequest;
import dco.domain.problem.dto.GetProblemDetailResponse;
import dco.domain.problem.dto.GetProblemListResponse;
import dco.domain.problem.dto.UpdateProblemRequest;
import dco.domain.problem.dto.UpdateProblemResponse;
import dco.domain.problem.service.ProblemService;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "문제 관리 API", description = "문제 관련 API endpoints")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemController {
    /*
     * 1. dto에서부터 필요한 데이터를 받고 service로 넘겨준다.
     * =============================== service
     * 2. 로그인 된 유저가 권한이 PROFESSOR가 맞는지 확인한다.
     * 3. 로그인 된 유저가 만든 그룹이 맞는지 확인
     * 3. 문제를 생성한다.
     * 4. 생성된 문제에 대한 정보를 반환한다.
     */
    private final ProblemService problemService;

    @Operation(summary = "문제 생성", description = "문제를 생성합니다.")
    @PostMapping("/professor")
    public ResponseEntity<Response<Long>> uploadProblem(@Valid @RequestBody CreateProblemRequest request) {
        return ResponseEntity.ok(new Response<>(problemService.uploadProblem(request), "문제 생성이 완료되었습니다!"));
    }

    @Operation(summary = "문제 목록 조회", description = "그룹에 속한 문제 목록을 조회합니다.")
    @GetMapping("/member/list")
    public ResponseEntity<Response<List<GetProblemListResponse>>> getProblemList(@Valid @RequestParam Long groupId) {
        List<GetProblemListResponse> response = problemService.getProblemList(groupId);
        return ResponseEntity.ok(new Response<>(response, "문제 목록 조회가 완료되었습니다!"));
    }

    @Operation(summary = "문제 상세 조회", description = "문제 상세 정보를 조회합니다.")
    @GetMapping("/member/detail")
public ResponseEntity<Response<GetProblemDetailResponse>> getProblemDetail(@Valid @RequestParam Long problemId) {
        GetProblemDetailResponse response = problemService.getProblemDetail(problemId);
        return ResponseEntity.ok(new Response<>(response, "문제 상세 조회가 완료되었습니다."));
    }

    @Operation(summary = "문제 수정", description = "문제를 수정합니다.")
    @PutMapping("/professor")
    public ResponseEntity<Response<UpdateProblemResponse>> updateProblem(@Valid @RequestBody UpdateProblemRequest request) {
        UpdateProblemResponse response = problemService.UpdateProblem(request);
        return ResponseEntity.ok(new Response<>(response, "문제 수정이 완료되었습니다."));
    }

    @Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
    @DeleteMapping("/professor/{problemId}")
    public ResponseEntity<Response> deleteProblem(@PathVariable Long problemId) {
        problemService.deleteProblem(problemId);
        return ResponseEntity.ok(new Response<>("문제 삭제가 완료되었습니다."));
    }

}
