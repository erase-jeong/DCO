package dco.domain.question.repository;

import dco.domain.group.entity.Group;
import dco.domain.notification.entity.Notification;
import dco.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByGroupOrderByQuestionIdDesc(Group group);

    Optional<Question> findByQuestionId(Long questionId);
}
