package dco.domain.group.dto;

import dco.domain.group.entity.Group;
import dco.domain.group.entity.GroupMember;
import dco.domain.member.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class GetGroupListByProfessorResponse {

    private Long groupId;
    private String groupName;
    private Integer year;
    private Semester semester;
    private String leaderName;

    public static GetGroupListByProfessorResponse toDto(Group group){
        return GetGroupListByProfessorResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .year(group.getYear())
                .semester(group.getSemester())
                .leaderName(group.getLeaderName())
                .build();
    }
}
