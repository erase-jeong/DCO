package dco.domain.comment.repository;

import dco.domain.comment.entity.Comment;
import dco.domain.question.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.parent IS NULL AND c.group.id = :groupId AND c.question.id = :questionId")
    @EntityGraph(attributePaths = {"children"}) //대댓글도 같이 로드
    List<Comment> findByQuestion(Long groupId, Long questionId);

    Optional<Comment> findByCommentId(Long commentId);

void deleteByQuestion(Question question);
}
