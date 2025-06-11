package dco.domain.problem.service;

import dco.domain.group.entity.Group;
import dco.domain.group.repository.GroupMemberRepository;
import dco.domain.group.repository.GroupRepository;
import dco.domain.problem.dto.*;
import dco.domain.problem.entity.Problem;
import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.domain.problem.repository.ProblemRepository;
import dco.domain.testcase.entity.TestCase;
import dco.domain.testcase.repository.TestCaseRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProblemService {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TestCaseRepository testCaseRepository;

    public List<GetProblemListResponse> getProblemList(Long groupId) {
        // 이 회원이 이 그룹에 소속되었는지 확인
        Group group = getGroup(groupId);

        Member member = getMember();

        // 이 그룹에 소속된 문제들에 대한 리스트를 가져오기
        List<Problem> problemList = problemRepository.findAllByGroupOrderByEndTimeAsc(group);

        // DTO로 변환 후 반환
        return problemList.stream()
                .map(GetProblemListResponse::toDto)
                .collect(Collectors.toList());
    }



    public GetProblemDetailResponse getProblemDetail(Long problemId) {
        /*
        1. problemId
        2. problemId에 해당하는 모든 problem 정보를 가져오는 쿼리문을 repository에 작성한다.
         */
        // problemId와 title이 같은 Problem을 조회
        Problem problem = problemRepository.findByProblemId(problemId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));
        // 조회된 문제의 정보를 기반으로 응답 객체 생성
        return GetProblemDetailResponse.toDto(problem);
    }



    @Transactional
    public Long uploadProblem(CreateProblemRequest request) {
        // group id를 넘겨줬던 이유?
        // 교수가 그룹을 여러개 만들 수 있기 때문.
        // 시큐리티가 지금 회원이 보고 있는 페이지를 알 수 없기 때문
        // 그래서 프론트가 지금 보고 있는 그룹의 id값을 넘겨줬다.
        Member currentMember = getMember();

        // TODO: 나중에 시큐리티에서 권한 체크 하도록 수정
        // 권한이 PROFESSOR인지 확인
        if (currentMember.isNotProfessor()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TASK);
        }

        // group 조회
        Group group = groupRepository.findByGroupId(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // Problem 생성하기
        // request에 있는 정보를 바탕으로 새로운 Problem 생성자를 만들고
        Problem newProblem = Problem.createProblem(request, group);
        // 엔티티 객체 그대로 db에 저장
        problemRepository.save(newProblem);


        // testcase 생성
        List<TestCase> testCaseList = request.getTestcaseList()
                .stream()
                .map(req -> TestCase.createTestCase(newProblem, req))
                .collect(Collectors.toList());

        testCaseRepository.saveAll(testCaseList);


        return newProblem.getProblemId();
    }

    private Member getMember() {
        // 현재 로그인된 사용자가 존재하는지 확인
        return memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다."));
    }

    private Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
    }

    @Transactional
    public UpdateProblemResponse UpdateProblem(UpdateProblemRequest request) {
        /*기
        1. problemId로 problem정보를 모두 가져온다.
        repository에 sql문을 작성한다.
        2. 수정하고 싶은 부분을 수정한다.
        3. 다시 problem을 저장한다.
         */

        // 이 회원이 이 그룹에 소속되는가?
        Member member = getMember();
        Group group = getGroup(request.getGroupId());
        isMemberJoined(group.getGroupId(), member.getMemberId());

        // 이 문제가 이 그룹에 소속된 문제인가?
        Problem problem = problemRepository.findByProblemId(request.getProblemId())
                .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        // 이 회원이 교수님인가?
        if (member.isNotProfessor()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TASK);
        }

        // proble 업데이트 후 저장
        problem.updateProblem(request);
        problemRepository.save(problem);
        // 엔티티를 반환하면 안돼요. 엔티티는 직접 수정하면 안돼요.

        return UpdateProblemResponse.toDto(problem);
    }

    private boolean isMemberJoined(Long groupId, Long memberId) {
        return groupMemberRepository.existsByGroupIdAndMemberId(groupId, memberId);
    }

    /**
     * 문제 삭제 (soft delete)
     */
    public void deleteProblem(Long problemId) {
        Problem problem = problemRepository.findByProblemId(problemId)
            .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        problemRepository.delete(problem);
    }
}
