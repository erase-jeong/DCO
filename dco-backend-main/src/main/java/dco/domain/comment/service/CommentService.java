package dco.domain.comment.service;

import dco.domain.comment.dto.*;
import dco.domain.comment.entity.Comment;
import dco.domain.comment.repository.CommentRepository;
import dco.domain.group.controller.GroupController;
import dco.domain.group.entity.Group;
import dco.domain.group.repository.GroupMemberRepository;
import dco.domain.group.repository.GroupRepository;
import dco.domain.member.entity.Member;
import dco.domain.member.entity.Role;
import dco.domain.member.repository.MemberRepository;
import dco.domain.problem.repository.ProblemRepository;
import dco.domain.question.entity.Question;
import dco.domain.question.repository.QuestionRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    //댓글 생성
    @Transactional
    public CreateCommentResponse createComment(CreateCommentRequest request) {

        //그룹에 속한 멤버인지 먼저 확인
        Group group = getGroup(request.getGroupId());

        Member member = getMember();

        if (!isMemberJoined(group.getGroupId(),member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_JOINED_MEMBER);
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Comment comment = Comment.createComment(request,group);

        Comment parentComment;

        //부모댓글이 있는지 확인
        if (request.getParentCommentId()!= null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParent(parentComment);
        }

        comment.setMember(member);
        comment.setQuestion(question);
        comment.setIsDeleted(false);
        commentRepository.save(comment);

        return CreateCommentResponse.toDto(comment);

    }

    public List<GetCommentResponse> getCommentList(Long groupId, Long questionId){
        Group group = getGroup(groupId);

        Member member = getMember();

        //그룹에 속해있는지 확인
        if (!isMemberJoined(group.getGroupId(),member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_JOINED_MEMBER);
        }

        List<Comment> commentList = commentRepository.findByQuestion(groupId, questionId);

        return commentList.stream()
                .map(comment -> createCommentResponse(comment, member))
                .collect(Collectors.toList());
    }

    private GetCommentResponse createCommentResponse(Comment comment, Member member) {

        List<GetCommentResponse> childResponses = comment.getChildren().stream()
                .map(childComment -> createCommentResponse(childComment, member)) // 자식 댓글도 권한에 따라 필터링
                .collect(Collectors.toList());

        // 댓글을 볼 수 없는 경우 비공개 메시지로 설정
        if (!canViewComment(comment, member)) {

            return GetCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .content("비공개입니다")
                    .isDeleted(comment.getIsDeleted())
                    .memberName("익명") // 댓글 작성자의 이름을 Member 엔티티에서 가져옴
                    .children(childResponses)
                    .role(comment.getMember().getRole())
                    .secret(comment.isSecret())
                    .build();
        }

        return GetCommentResponse.toDto(comment);
    }

    //비밀댓글인지 여부와 권한,작성자 여부를 확인하여 댓글을 볼 수 있는지 결정하는 메서드
    private boolean canViewComment(Comment comment, Member member) {

        // 글 작성자, 댓글 작성자, 또는 교수 권한을 가진 사용자는 비밀댓글을 볼 수 있음
        if (isAuthorOrProfessor(member, comment.getQuestion().getMember()) || member.equals(comment.getMember())) {
            return true;
        }

        // 대댓글이 비밀일 때, 부모 댓글 작성자가 볼 수 있도록 설정
        if (comment.getParent() != null && comment.getParent().getMember().equals(member)) {
            return true;
        }

        //비밀이 아닌 댓글
        return !comment.isSecret();
    }

    //작성자가 글 작성자이거나 교수 권한을 가진 경우 true 반환
    private boolean isAuthorOrProfessor(Member member, Member author) {
        return member.equals(author) || member.getRole().equals(Role.PROFESSOR);
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(comment.getParent() == null){ //부모 댓글이라면
            comment.updateDeleted();
            commentRepository.save(comment);
        }else{
            commentRepository.delete(comment);
        }

    }

    public UpdateCommentResponse updateComment(UpdateCommentRequest request){
        Member member = getMember();
        Group group = getGroup(request.getGroupId());

        Comment comment = commentRepository.findByCommentId(request.getCommentId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        //댓글 쓴 사람, 수정 사람 일치 여부 확인
        if(comment.getMember().getMemberId() != member.getMemberId()){
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        comment.updateComment(request);
        commentRepository.save(comment);

        return UpdateCommentResponse.toDto(comment);
    }

    private Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    private Member getMember() {
        // 현재 로그인된 사용자가 존재하는지 확인
        return memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다."));
    }

    private boolean isMemberJoined(Long groupId, Long memberId) {
        return groupMemberRepository.existsByGroupIdAndMemberId(groupId, memberId);
    }
}
