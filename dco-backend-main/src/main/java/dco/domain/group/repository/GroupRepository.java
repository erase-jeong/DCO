package dco.domain.group.repository;

import dco.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByInviteUrl(String inviteUrl);
    Optional<Group> findByGroupId(Long groupId);
    List<Group> findByLeaderCode(String memberId);
}