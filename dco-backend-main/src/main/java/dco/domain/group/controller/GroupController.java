package dco.domain.group.controller;

import dco.domain.group.dto.*;
import dco.global.auth.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dco.domain.group.service.GroupService;
import dco.global.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Tag(name = "그룹 관리 API", description = "그룹 관련 API endpoints")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성", description = """
    semester = FIRST, SECOND, SUMMER, WINTER (각각 1학기, 2학기, 하계계절학기, 동계계절학기)
    """)
    @PostMapping("/professor")
    public ResponseEntity<Response<CreateGroupResponse>> createGroup(@RequestBody CreateGroupRequest request) {
        return ResponseEntity.ok(new Response<>(groupService.createGroup(request), "그룹이 생성되었습니다."));
    }

    @Operation(summary = "그룹 페이지 조회에 필요한 데이터 가져오기", description = "해당 그룹에 초대 수락한 멤버만 접근 가능")
    @GetMapping("/member/{groupId}")
    public ResponseEntity<Response<GroupResponse>> getGroupData(@PathVariable Long groupId) {
        GroupResponse groupResponse = groupService.getGroupData(groupId);
        return ResponseEntity.ok(new Response<>(groupResponse, "그룹 데이터가 조회되었습니다."));
    }

    @Operation(summary = "그룹 가입 수락(입장)", description = "초대 링크를 통해 그룹 가입을 수락합니다.")
    @PostMapping("/member/accept")
    public ResponseEntity<Response<Void>> acceptInvitation(@RequestParam String inviteUrl) {
        groupService.acceptInvitation(inviteUrl);
        return ResponseEntity.ok(new Response<>(null, "그룹에 가입되었습니다."));
    }

    @Operation(summary = "내가 속한 그룹 리스트 조회", description = "사용자가 속한 그룹의 리스트를 조회합니다")
    @GetMapping("/member/list")
    public ResponseEntity<Response<List<GetGroupListByMemberResponse>>> getGroupListMyMember(){

        return ResponseEntity.ok(new Response<>(groupService.getGroupListByMember(), "사용자별 그룹 리스트 조회가 완료되었습니다."));
    }

    @Operation(summary = "내가 생성한 그룹 리스트 조회",description = "자신(교수)이 생성한 그룹의 리스트를 조회합니다." )
    @GetMapping("/professor/list")
    public ResponseEntity<Response<List<GetGroupListByProfessorResponse>>> getGroupListMyProfessor(){

        return ResponseEntity.ok(new Response<>(groupService.getGroupListByProfessor(), "자신이 생성한 그룹 리스트 조회가 완료되었습니다."));
    }
}