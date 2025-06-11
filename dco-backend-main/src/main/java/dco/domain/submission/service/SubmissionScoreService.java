package dco.domain.submission.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dco.domain.group.entity.Group;
import dco.domain.group.repository.GroupRepository;
import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.domain.submission.dto.SubmissionMemberScoreResponse;
import dco.domain.submission.dto.out.GetRankingResponse;
import dco.domain.submission.entity.Submission;
import dco.domain.submission.repository.SubmissionRepository;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;

@Service
public class SubmissionScoreService {

    private final SubmissionRepository submissionRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;


    public SubmissionScoreService(SubmissionRepository submissionRepository,GroupRepository groupRepository,MemberRepository memberRepository) {
        this.submissionRepository = submissionRepository;
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
    }


    public List<SubmissionMemberScoreResponse> SubmissionMemberScore(Long groupId, Long memberId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Submission> submissions = submissionRepository.findByGroupAndMember(group, member);

        // Submission 엔티티를 SubmissionMemberScoreResponse DTO로 변환
        return submissions.stream().map(submission ->
                SubmissionMemberScoreResponse.builder()
                        .memberId(submission.getMember().getMemberId())
                        .name(submission.getMember().getName())
                        .problemId(submission.getProblem().getProblemId())
                        .title(submission.getProblem().getTitle())
                        .score(submission.getScore().longValue())
                        .language(submission.getLanguage().name())
                        .submitTime(submission.getCreatedDate())
                        .build()
        ).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<GetRankingResponse> getTop3Rankings(Long problemId) {
        return submissionRepository.findTop3ByProblemId(problemId, PageRequest.of(0, 3))
            .stream()
            .map(submission -> GetRankingResponse.builder()
                .memberName(submission.getMember().getName())
                .score(submission.getScore())
                .submitDate(submission.getCreatedDate().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build())
            .toList();
    }

}
