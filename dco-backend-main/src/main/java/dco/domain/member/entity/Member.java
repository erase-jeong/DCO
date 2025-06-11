package dco.domain.member.entity;

import dco.domain.group.entity.GroupMember;
import dco.domain.member.dto.SignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupMember> groupMembers = new HashSet<>();

    @Column
    private String githubToken;

    @Column
    private String repositoryName;

    @Column
    private String githubName;

    @Builder
    public Member(String code, String password, Role role, String name, String githubToken, String repositoryName, String githubName) {
        this.code = code;
        this.password = password;
        this.role = role;
        this.name = name;
        this.githubToken = githubToken;
        this.repositoryName = repositoryName;
        this.githubName = githubName;
    }

    @Builder
    public static Member createMember(SignUpRequest request, String encodedPassword, Role role) {
        return Member.builder()
                .code(request.getCode())
                .password(encodedPassword)
                .role(role)
                .name(request.getName())
                .githubToken(null)
                .repositoryName(null)
                .githubName(null)
                .build();
    }

    public boolean isNotAllowedToCreateGroup() {
        return this.role != Role.PROFESSOR;
    }

    public boolean isNotProfessor() {
        return this.role != Role.PROFESSOR;
    }

}
