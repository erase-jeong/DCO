package dco.domain.group.dto;

import dco.domain.member.entity.Semester;
import lombok.Getter;

@Getter
public class CreateGroupRequest {
    private String groupName;
    private Integer year;
    private Semester semester;
}
