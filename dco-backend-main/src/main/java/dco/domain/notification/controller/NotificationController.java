package dco.domain.notification.controller;

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

import dco.domain.notification.dto.GetNotificationDetailResponse;
import dco.domain.notification.dto.GetNotificationResponse;
import dco.domain.notification.dto.UpdateNotificationRequest;
import dco.domain.notification.dto.UpdateNotificationResponse;
import dco.domain.notification.dto.UploadNotificationRequest;
import dco.domain.notification.dto.UploadNotificationResponse;
import dco.domain.notification.service.NotificationService;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "공지 API", description = "공지 관련 API endpoints")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    /*
     * 1. 공지 조회
     * 2. 공지 상세 조회
     * 3. 공지 생성 (PROFESSOR 권한 확인 필요)
     * 4. 공지 삭제
     */


    @Operation(summary = "공지 목록 조회", description = "그룹에 속한 공지 목록을 조회합니다.")
    @GetMapping("/member/list")
    public ResponseEntity<Response<List<GetNotificationResponse>>> getNotification(@Valid @RequestParam Long groupId) {
        List<GetNotificationResponse> response = notificationService.getNotificationList(groupId);
        return ResponseEntity.ok(new Response<>(response, "공지 목록 조회가 완료되었습니다"));
    }

    @Operation(summary = "공지 상세 조회", description = "공지 상세 정보를 조회합니다.")
    @GetMapping("/member/detail")
    public ResponseEntity<Response<GetNotificationDetailResponse>> getNotificationDetail(@Parameter(name = "notificationId", description = "조회할 공지의 ID") @Valid @RequestParam Long notificationId){

        GetNotificationDetailResponse response = notificationService.getNotificationDetail(notificationId);
        return ResponseEntity.ok(new Response<>(response, "공지 상세조회가 완료되었습니다."));
    }

    @Operation(summary = "공지 생성", description = "공지를 생성합니다.")
    @PostMapping("/professor")
    public ResponseEntity<Response<UploadNotificationResponse>> uploadNotification(@Valid @RequestBody UploadNotificationRequest request){
        return ResponseEntity.ok(new Response<>(notificationService.uploadNotification(request), "완"));
    }

    @Operation(summary = "공지 수정", description = "공지를 수정합니다.")
    @PutMapping("/professor")
    public ResponseEntity<Response<UpdateNotificationResponse>> updateNotification(@Valid @RequestBody UpdateNotificationRequest request){
        return ResponseEntity.ok(new Response<>(notificationService.updateNotification(request), "공지 수정이 완료되었습니다."));
    }

    @Operation(summary = "공지 삭제", description = "공지를 삭제합니다.")
    @DeleteMapping("/professor/{notificationId}")
    public ResponseEntity<Response> deleteNotification(@PathVariable Long notificationId){
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new Response<>("공지 삭제가 완료되었습니다."));
    }
}
