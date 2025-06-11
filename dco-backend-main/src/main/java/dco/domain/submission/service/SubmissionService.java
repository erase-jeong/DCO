package dco.domain.submission.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import dco.domain.common.Language;
import dco.domain.member.entity.Member;
import dco.domain.member.repository.MemberRepository;
import dco.domain.problem.entity.Problem;
import dco.domain.problem.repository.ProblemRepository;
import dco.domain.submission.dto.SubmissionRequest;
import dco.domain.submission.dto.SubmissionResponse;
import dco.domain.submission.entity.Submission;
import dco.domain.submission.repository.SubmissionRepository;
import dco.domain.testcase.entity.TestCase;
import dco.global.error.CustomException;
import dco.global.error.ErrorCode;
import dco.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final JavaRunner javaRunner;
    private final PythonRunner pythonRunner;
    private final CppRunner cppRunner;

    public SubmissionResponse processSubmission(String studentId, SubmissionRequest request) {
        Member member = memberRepository.findByCode(studentId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Problem problem = problemRepository.findById(request.getProblemId())
            .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        // TODO: 제출 횟수 제대로 올라가게 수정 필요
        // 제출 횟수 확인
        Long submissionCnt = submissionRepository.findByMemberAndProblem(member.getMemberId(),
            problem.getProblemId());
        if (submissionCnt >= problem.getSubmitLimit()) {
            throw new CustomException(ErrorCode.SUBMISSION_LIMIT_OVER);
        }

        // 제출 가능 시간 확인
        LocalDateTime now = LocalDateTime.now();
        log.debug("now: {}", now);
        if (now.isBefore(problem.getStartTime()) || now.isAfter(problem.getEndTime())) {
            throw new CustomException(ErrorCode.SUBMISSION_INVALID_TIME);
        }

        List<TestCase> testCaseList = problem.getTestCaseList();
        // 채점 데이터가 존재하지 않는지 확인
        if (testCaseList.isEmpty()) {
            throw new CustomException(ErrorCode.TESTCASE_EMPTY);
        }

        String code = request.getCode();
        Language language = request.getLanguage();

        SubmissionResponse submissionResponse;
        try {
            if (language == Language.JAVA) {
                submissionResponse = javaRunner.run(code, testCaseList, problem.getJavaExecuteTimeLimit());
            } else if (language == Language.PYTHON) {
                submissionResponse = pythonRunner.run(code, testCaseList, problem.getPythonExecuteTimeLimit());
            } else if (language == Language.CPP) {
                submissionResponse = cppRunner.run(code, testCaseList, problem.getCppExecuteTimeLimit());
            } else {
                throw new CustomException(ErrorCode.LANGUAGE_NOT_SUPPORTED);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CODE_RUN_FAIL);
        }
        Submission submission = Submission.createSubmission(request, submissionResponse, member, problem);
        submissionRepository.save(submission);

        return submissionResponse;
    }

    public void githubUpload(Member member, SubmissionRequest request) throws Exception {

        // 사용자 ID로 GitHub 정보를 조회

        if (member.getGithubName() == null) {
            // GitHub 정보가 없는 경우 처리
            return;
        }

        //문제 정보 받아오기
//         String problemTitle = problemRepository.findTitleById(request.getProblemId());

        //깃허브 업로드 (제목은 임시)
        RestTemplate restTemplate = new RestTemplate();
        Map<Language, String> languageExtensions = new HashMap<>();

        languageExtensions.put(Language.JAVA, "java");
        languageExtensions.put(Language.CPP, "cpp");
        languageExtensions.put(Language.PYTHON, "py");

        String extension = languageExtensions.getOrDefault(request.getLanguage(), "txt");
        String fullFilePath = "과제" + "/" + "solution" + "." + extension; // 추후에 문제 타이틀 추가 필요

        // GitHub API URL
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", member.getGithubName(),
            member.getRepositoryName(), fullFilePath);

        String fileContent = request.getCode();

        String encodedContent = Base64.getEncoder().encodeToString(fileContent.getBytes(StandardCharsets.UTF_8));

        // 요청 본문 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("message", "과제");
        requestBody.put("content", encodedContent);
        requestBody.put("branch", "main"); //main이 default

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "token " + member.getGithubToken());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Failed to upload file: " + response.getBody());
        }
    }

    public void createSubmission(SubmissionRequest request, SubmissionResponse response) throws Exception {

        Member member = memberRepository.findByCode(SecurityUtil.getCurrentMemberCode())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // todo: 컴파일 오류로 주석 처리 해뒀음. - 진수
//         submissionRepository.save(Submission.createSubmission(member, request, response));

        if (response.getScore() == 100) {
            githubUpload(member, request);
        }

    }

}