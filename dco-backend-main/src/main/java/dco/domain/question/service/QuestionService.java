package dco.domain.question.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dco.domain.comment.repository.CommentRepository;
import dco.domain.group.entity.Group;
import dco.domain.group.repository.GroupMemberRepository;
import dco.domain.group.repository.GroupRepository;
import dco.domain.member.entity.Member;
import dco.domain.member.entity.Role;
import dco.domain.member.repository.MemberRepository;
import dco.domain.question.dto.CreateQuestionRequest;
import dco.domain.question.dto.CreateQuestionResponse;
import dco.domain.question.dto.GetQuestionDetailResponse;
import dco.domain.question.dto.GetQuestionListResponse;
import dco.domain.question.dto.UpdateQuestionRequest;
import dco.domain.question.dto.UpdateQuestionResponse;
import dco.domain.question.entity.Question;
import dco.domain.question.repository.QuestionRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    public List<GetQuestionListResponse> getQuestionList(Long groupId){

        Group group = getGroup(groupId);

        Member member = getMember();

        //그룹에 속해있는지 확인
        if (!isMemberJoined(group.getGroupId(),member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_JOINED_MEMBER);
        }

        List<Question> questionList = questionRepository.findAllByGroupOrderByQuestionIdDesc(group);

        return questionList.stream()
                .map(question -> {
                    // 비밀글이고 로그인한 유저가 작성자 또는 교수가 아닐 경우
                    if (question.isSecret() &&
                            !question.getMember().getMemberId().equals(member.getMemberId()) &&
                            member.getRole() != Role.PROFESSOR) {
                        return GetQuestionListResponse.builder()
                                .questionId(question.getQuestionId())
                                .title("비밀글입니다")
                                .content("비밀글입니다")
                                .name("익명")
                                .category(question.getCategory())
                                .secret(question.isSecret())
                                .build();
                    } else {
                        // 일반 글이거나 로그인한 유저가 작성자인 경우 원래 데이터를 반환
                        return GetQuestionListResponse.toDto(question);
                    }
                })
                .collect(Collectors.toList());
    }

    public GetQuestionDetailResponse getQuestionDetail(Long questionId){
        Question question = questionRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Member member = getMember();

        //비밀글이라면
        if(question.isSecret()){
            //작성자 또는 교수만 조회 가능
            if(Objects.equals(question.getMember().getMemberId(), member.getMemberId()) || (member.getRole() == (Role.PROFESSOR))){
                GetQuestionDetailResponse.toDto(question);
            }else{
                return null;
            }
        }

        return GetQuestionDetailResponse.toDto(question);
    }

    @Transactional
    public CreateQuestionResponse uploadQuestion(CreateQuestionRequest request){

        Group group = getGroup(request.getGroupId());

        Member member = getMember();

        if (!isMemberJoined(group.getGroupId(),member.getMemberId())) {
            throw new CustomException(ErrorCode.NOT_JOINED_MEMBER);
        }

        Question question = Question.createQuestion(request, group, member, request.getCategory());

        questionRepository.save(question);

        return CreateQuestionResponse.toDto(question);
    }

    public UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request){
        Member member = getMember();
        Group group = getGroup(request.getGroupId());

        Question question = questionRepository.findByQuestionId(request.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        //학번 비교
        if(question.getMember().getMemberId() != member.getMemberId()){
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        question.updateQuestion(request);
        questionRepository.save(question);

        return UpdateQuestionResponse.toDto(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId){
        Question question = questionRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        //질문에 달린 댓글 삭제
        commentRepository.deleteByQuestion(question);

        questionRepository.delete(question);
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
