package dco.domain.group.dto;

import dco.domain.group.entity.Group;
import dco.domain.group.entity.GroupMember;
import dco.domain.member.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetGroupListByMemberResponse {
    private Long groupId;
    private String groupName;
    private Integer year;
    private Semester semester;
    private String leaderName;

    public static GetGroupListByMemberResponse toDto(GroupMember group){
        return GetGroupListByMemberResponse.builder()
                .groupId(group.getGroup().getGroupId())
                .groupName(group.getGroup().getGroupName())
                .year(group.getGroup().getYear())
                .semester(group.getGroup().getSemester())
                .leaderName(group.getGroup().getLeaderName())
                .build();
    }
}