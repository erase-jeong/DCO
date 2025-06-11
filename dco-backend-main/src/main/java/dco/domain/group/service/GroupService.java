package dco.domain.group.service;

import dco.domain.group.dto.*;
import dco.domain.group.entity.Group;
import dco.domain.group.entity.GroupMember;
import dco.domain.group.repository.GroupMemberRepository;
import dco.domain.group.repository.GroupRepository;
import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    public CreateGroupResponse createGroup(CreateGroupRequest request) {
        Member leader = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 초대 URL 생성
        String host = "http://localhost:3000/group/join?token=";
        String token = UUID.randomUUID().toString();
        String inviteUrl = host + token;

        // 로그인 한 유저의 권한이 PROFESSOR인지 확인
        groupCreateAuthrorityCheck(leader);

        // 그룹 생성
        Group newGroup = Group.createGroup(request, leader, inviteUrl);
        // 리더를 그룹에 멤버로 포함
        GroupMember groupMember = GroupMember.builder()
                .group(newGroup)
                .member(leader)
                .build();

        newGroup.getGroupMembers().add(groupMember);
        leader.getGroupMembers().add(groupMember);

        groupRepository.save(newGroup);
        memberRepository.save(leader);

        return CreateGroupResponse.from(newGroup);
    }

    @Transactional(readOnly = true)
    public GroupResponse getGroupData(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Member member = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isMember = groupMemberRepository.existsByGroupIdAndMemberId(groupId, member.getMemberId());

        if (!isMember) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }
        return GroupResponse.from(group);
    }


    public String requestGroupUrl(RequestJoinGroupRequest request) {
        Group group = groupRepository.findByInviteUrl(request.getInviteUrl())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_GROUP_LINK));

        return group.getInviteUrl();
    }

    @Transactional
    public void acceptInvitation(String inviteUrl) {
        Member member = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Group group = groupRepository.findByInviteUrl(inviteUrl)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_GROUP_LINK));

        if (isMemberExists(group.getGroupId(), member.getMemberId())) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_MEMBER);
        }
        // 그룹에 멤버 추가
        group.addMember(member);
    }


    public boolean isMemberExists(Long groupId, Long memberId) {
        return groupMemberRepository.existsByGroupIdAndMemberId(groupId, memberId);
    }

//    @Transactional(readOnly = true)
//    public GetGroupResponse getGroupMembers(Long groupId) {
//        Group group = groupRepository.findByGroupId(groupId)
//                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
//
//        return group.getGroupMembers().stream()
//                .map(groupMember -> GetGroupResponse.toDto(groupMember.getMember()))
//                .collect(Collectors.toList());
//    }

    private void groupCreateAuthrorityCheck(Member leader) {
        if (leader.isNotAllowedToCreateGroup()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TASK);
        }
    }

    //자신이 속한 그룹 조회 - 멤버
    public List<GetGroupListByMemberResponse> getGroupListByMember(){
        Member member = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<GroupMember> groupMembers = groupMemberRepository.findByMember(member);

        return groupMembers.stream()
                .map(GetGroupListByMemberResponse::toDto)
                .collect(Collectors.toList());
    }

    //자신이 생성한 그룹 조회 - 교수
    public List<GetGroupListByProfessorResponse> getGroupListByProfessor(){
        Member leader = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Group> group = groupRepository.findByLeaderCode(leader.getCode());

        return group.stream()
                .map(GetGroupListByProfessorResponse::toDto)
                .collect(Collectors.toList());
    }
}
