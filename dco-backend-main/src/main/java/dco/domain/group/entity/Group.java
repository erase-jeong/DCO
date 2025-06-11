package dco.domain.group.entity;

import dco.domain.group.dto.CreateGroupRequest;
import dco.domain.member.entity.Member;
import dco.domain.member.entity.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="\"group\"") // 테이블 이름을 큰따옴표로 감싸서 예약어 문제 해결
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String leaderCode;

    @Column(nullable = false)
    private String leaderName;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Column(nullable = false, unique = true)
    private String inviteUrl;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupMember> groupMembers = new ArrayList<>();
    @Builder
    public Group(String groupName, String leaderCode, Integer year, Semester semester, String inviteUrl, String leaderName) {
        this.groupName = groupName;
        this.leaderCode = leaderCode;
        this.year = year;
        this.semester = semester;
        this.inviteUrl = inviteUrl;
        this.leaderName = leaderName;
    }

    @Builder
    public static Group createGroup(CreateGroupRequest request, Member leader, String inviteUrl) {
        return Group.builder()
                .groupName(request.getGroupName())
                .leaderCode(leader.getCode())
                .year(request.getYear())
                .semester(request.getSemester())
                .inviteUrl(inviteUrl)
                .leaderName(leader.getName())
                .build();
    }

    public void addMember(Member member) {

        GroupMember groupMember = GroupMember.builder()
                .group(this)
                .member(member)
                .build();
        groupMembers.add(groupMember);
    }

    public void removeMember(Member member) {
        groupMembers.removeIf(groupMember -> groupMember.getMember().equals(member));
    }

}
