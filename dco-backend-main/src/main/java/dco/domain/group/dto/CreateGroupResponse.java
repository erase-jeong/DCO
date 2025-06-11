package dco.domain.group.dto;

import dco.domain.group.entity.Group;
import dco.domain.member.dto.MemberResponse;
import dco.domain.member.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class CreateGroupResponse {
    private Long groupId;
    private String groupName;
    private String leaderCode;
    private Integer year;
    private Semester semester;
    private String leaderName;
    private String inviteUrl;
    private List<MemberResponse> members;

    public static CreateGroupResponse from(Group group) {
        return CreateGroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .leaderName(group.getLeaderName())
                .leaderCode(group.getLeaderCode())
                .year(group.getYear())
                .semester(group.getSemester())
                .inviteUrl(group.getInviteUrl())
                .members(group.getGroupMembers().stream()
                        .map(groupMember -> MemberResponse.from(groupMember.getMember()))
                        .collect(Collectors.toList()))
                .build();
    }
}
