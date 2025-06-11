package dco.domain.notification.service;

import dco.domain.group.entity.Group;
import dco.domain.group.repository.GroupMemberRepository;
import dco.domain.group.repository.GroupRepository;
import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.domain.notification.dto.*;
import dco.domain.notification.entity.Notification;
import dco.domain.notification.repository.NotificationRepository;
import dco.domain.problem.dto.GetProblemListResponse;
import dco.domain.problem.entity.Problem;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final GroupMemberRepository groupMemberRepository;

    public List<GetNotificationResponse> getNotificationList(Long groupId){
        Group group = getGroup(groupId);

        Member member = getMember();

        if (!isMemberJoined(group.getGroupId(),member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_JOINED_MEMBER);
        }

        List<Notification> notificationList = notificationRepository.findAllByGroupOrderByNotificationIdDesc(group);

        return notificationList.stream()
                .map(GetNotificationResponse::toDto)
                .collect(Collectors.toList());
    }

    public GetNotificationDetailResponse getNotificationDetail(Long notificationId){
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        return GetNotificationDetailResponse.toDto(notification);
    }

    private Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    private Member getMember() {
        // 현재 로그인된 사용자가 존재하는지 확인
        return memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다."));
    }

    private boolean isMemberJoined(Long groupId, Long memberId) {
        return groupMemberRepository.existsByGroupIdAndMemberId(groupId, memberId);
    }

    @Transactional
    public UploadNotificationResponse uploadNotification(UploadNotificationRequest request){

        Member currentMember = getMember();

        // 권한이 PROFESSOR인지 확인
        if (currentMember.isNotProfessor()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TASK);
        }

        Group group = groupRepository.findByGroupId(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Notification newNotification = Notification.createNotification(request, group);
        notificationRepository.save(newNotification);

        return UploadNotificationResponse.toDto(newNotification);
    }

    @Transactional
    public UpdateNotificationResponse updateNotification(UpdateNotificationRequest request){

        Member member = getMember();
        Group group = getGroup(request.getGroupId());
        isMemberJoined(group.getGroupId(), member.getMemberId());

        if (member.isNotProfessor()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TASK);
        }

        Notification notification = notificationRepository.findByNotificationId(request.getNotificationId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.updateNotification(request);
        notificationRepository.save(notification);

        return UpdateNotificationResponse.toDto(notification);
    }

    public void deleteNotification(Long notificationId){
        Notification notification = notificationRepository.findByNotificationId((notificationId))
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }
}
