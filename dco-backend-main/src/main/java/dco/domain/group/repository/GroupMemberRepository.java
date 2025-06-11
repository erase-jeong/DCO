package dco.domain.group.repository;

import dco.domain.group.entity.GroupMember;
import dco.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    @Query("SELECT CASE WHEN COUNT(gm) > 0 THEN TRUE ELSE FALSE END FROM GroupMember gm WHERE gm.group.groupId = :groupId AND gm.member.memberId = :memberId")
    boolean existsByGroupIdAndMemberId(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    List<GroupMember> findByMember(Member member);
}
