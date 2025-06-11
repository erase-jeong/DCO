package dco.domain.comment.controller;

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

import dco.domain.comment.dto.CreateCommentRequest;
import dco.domain.comment.dto.CreateCommentResponse;
import dco.domain.comment.dto.GetCommentResponse;
import dco.domain.comment.dto.UpdateCommentRequest;
import dco.domain.comment.dto.UpdateCommentResponse;
import dco.domain.comment.service.CommentService;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "댓글 관리 API", description = "댓글 관련 API endpoints")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 등록
    @Operation(summary = "댓글 등록", description = "댓글을 등록합니다.")
    @PostMapping("/member")
    public ResponseEntity<Response<CreateCommentResponse>> createComment(@Valid @RequestBody CreateCommentRequest request){
        return ResponseEntity.ok(new Response<>(commentService.createComment(request), "댓글 등록이 완료되었습니다."));
    }

    //전체 댓글 조회
    @Operation(summary = "댓글 목록 조회", description = "질문의 댓글 목록을 조회합니다.")
    @GetMapping("/member/list")
    public ResponseEntity<Response<List<GetCommentResponse>>> getComment(@RequestParam Long groupId, @RequestParam Long questionId){
        List<GetCommentResponse> response = commentService.getCommentList(groupId, questionId);
        return ResponseEntity.ok(new Response<>(response, "댓글 조회가 완료되었습니다."));
    }

    //삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/member/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new Response<>("댓글 삭제가 완료되었습니다."));
    }

    //수정
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/member")
    public ResponseEntity<Response<UpdateCommentResponse>> updateComment(@Valid @RequestBody UpdateCommentRequest request){
        return ResponseEntity.ok(new Response<>(commentService.updateComment(request),""));
    }
}
