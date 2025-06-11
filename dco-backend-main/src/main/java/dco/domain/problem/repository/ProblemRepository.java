package dco.domain.problem.repository;

import dco.domain.group.entity.Group;
import dco.domain.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    // 특정 그룹에 속한 모든 Problem을 마감 날짜 기준으로 정렬하여 조회하는 메서드
    List<Problem> findAllByGroupOrderByEndTimeAsc(Group group);

    // problemId와 title이 같은 Problem을 조회하는 메서드
    Optional<Problem> findByProblemId(Long problemId);
}
