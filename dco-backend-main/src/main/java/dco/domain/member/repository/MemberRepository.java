package dco.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dco.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByCode(String code);
    Optional<Member> findByCode(String code);
    Optional<Member> findByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Member m SET m.githubToken = :githubToken, m.githubName = :githubName, m.repositoryName = :repositoryName WHERE m.code = :memberId")
    void updateGithub(@Param("memberId") String memberId, @Param("githubToken") String githubToken, @Param("githubName") String githubName, @Param("repositoryName") String repositoryName);


    // @Query("SELECT githubToken, githubName, repositoryName FROM Member WHERE code = :memberId")
    // GetGithubResponse findByUserGithub(@Param("memberId") String memberId);
    //
    //



}
