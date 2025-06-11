package dco.domain.submission.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dco.domain.group.entity.Group;
import dco.domain.member.entity.Member;
import dco.domain.submission.entity.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("select count(s) from Submission s where s.member.memberId = :memberId and s.problem.problemId = :problemId")
    Long findByMemberAndProblem(@Param("memberId") Long memberId, @Param("problemId") Long problemId);

    List<Submission> findByGroupAndMember(Group group, Member member);

    @Query("SELECT s FROM Submission s " +
        "WHERE s.problem.problemId = :problemId " +
        "AND s.score > 0 " +
        "AND (s.member.memberId, s.score, s.createdDate) IN (" +
        "    SELECT s2.member.memberId, MAX(s2.score), MIN(s2.createdDate) " +
        "    FROM Submission s2 " +
        "    WHERE s2.problem.problemId = :problemId " +
        "    GROUP BY s2.member.memberId" +
        ") " +
        "ORDER BY s.score DESC, s.createdDate ASC")
    List<Submission> findTop3ByProblemId(@Param("problemId") Long problemId, Pageable pageable);
}
