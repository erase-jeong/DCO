package dco.domain.question.controller;

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

import dco.domain.question.dto.CreateQuestionRequest;
import dco.domain.question.dto.CreateQuestionResponse;
import dco.domain.question.dto.GetQuestionDetailResponse;
import dco.domain.question.dto.GetQuestionListResponse;
import dco.domain.question.dto.UpdateQuestionRequest;
import dco.domain.question.dto.UpdateQuestionResponse;
import dco.domain.question.service.QuestionService;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "질문 관리 API", description = "질문 관련 API endpoints")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "질문 목록 조회", description = "그룹에 속한 질문 목록을 조회합니다.")
    @GetMapping("/member/list")
    public ResponseEntity<Response<List<GetQuestionListResponse>>> getQuestionList(@Valid @RequestParam Long groupId){
        List<GetQuestionListResponse> response = questionService.getQuestionList(groupId);
        return ResponseEntity.ok(new Response<>(response, "질문 목록 조회가 완료되었습니다"));
    }

    @Operation(summary = "질문 상세 조회", description = "질문 상세 정보를 조회합니다.")
    @GetMapping("/member/detail")
    public ResponseEntity<Response<GetQuestionDetailResponse>> getQuestionDetail(@Valid @RequestParam Long questionId){

        GetQuestionDetailResponse response = questionService.getQuestionDetail(questionId);

        String msg = "질문 상세 조회가 완료되었습니다.";

        if(response == null){
            msg = "권한이 없습니다.";
        }

        return ResponseEntity.ok(new Response<>(response, msg));
    }

    @Operation(summary = "질문 생성", description = "질문을 생성합니다.")
    @PostMapping("/member")
    public ResponseEntity<Response<CreateQuestionResponse>> uploadQuestion(@Valid @RequestBody CreateQuestionRequest request){
        return ResponseEntity.ok(new Response<>(questionService.uploadQuestion(request), "질문 등록이 완료되었습니다."));
    }

    @Operation(summary = "질문 수정", description = "질문을 수정합니다.")
    @PutMapping("/member")
    public ResponseEntity<Response<UpdateQuestionResponse>> updateQuestion(@Valid @RequestBody UpdateQuestionRequest request){
        return ResponseEntity.ok(new Response<>(questionService.updateQuestion(request), "질문 수정이 완료되었습니다."));
    }

    @Operation(summary = "질문 삭제", description = "질문을 삭제합니다.")
    @DeleteMapping("/member/{questionId}")
    public ResponseEntity<Response> deleteQuestion(@PathVariable Long questionId){
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(new Response<>("질문 삭제가 완료되었습니다."));
    }
}
