package dco.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /*
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 정보를 찾을 수 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    VALIDATION_FAILURE(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문이 유효하지 않습니다."),


    // 인증 관련
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레시 토큰을 찾지 못했습니다."),
    NOT_MATCHED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "DB의 리프레시 토큰값과 일치하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰이 만료되었습니다."),
    ACCOUNT_NOT_MATCHED(HttpStatus.BAD_REQUEST, "로그인 된 회원정보와 다른 유저의 요청입니다."),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 잘못되었습니다."),
    STUDENT_ID_DUPLICATION(HttpStatus.CONFLICT, "이미 사용 중인 학번입니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구로 추가된 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 회원을 찾을 수 없습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    // 권한 관련
    NO_AUTHORITY_FOUND(HttpStatus.NOT_FOUND, "회원의 권한 정보를 찾을 수 없습니다"),
    UNAUTHORIZED_MEMBER(HttpStatus.NOT_FOUND, "접근 권한이 없습니다."),
    UNAUTHORIZED_TASK(HttpStatus.UNAUTHORIZED, "허용되지 않은 권한입니다."),

    // 그룹 관련
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹이 존재하지 않습니다."),
    INVALID_GROUP_LINK(HttpStatus.NOT_FOUND, "해당 그룹에 대한 링크가 존재하지 않습니다."),
    ALREADY_JOINED_MEMBER(HttpStatus.CONFLICT, "이미 그룹에 가입된 회원입니다."),
    NOT_JOINED_MEMBER(HttpStatus.NOT_FOUND, "그룹에 소속되지 않은 회원입니다."),

    //문제 관련
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문제를 찾을 수 없습니다."),

    //공지
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공지를 찾을 수 없습니다."),

    //질문
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문을 찾을 수 없습니다."),

    //댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 업습니다."),

    // 채점
    CODE_RUN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "소스 코드 컴파일 중 에러가 발생했습니다."),
    LANGUAGE_NOT_SUPPORTED(HttpStatus.INTERNAL_SERVER_ERROR, "소스 코드 컴파일 중 에러가 발생했습니다."),
    TESTCASE_EMPTY(HttpStatus.NOT_FOUND, "채점 데이터가 존재하지 않습니다."),
    SUBMISSION_INVALID_TIME(HttpStatus.BAD_REQUEST, "제출 가능한 시간이 아닙니다."),
    SUBMISSION_LIMIT_OVER(HttpStatus.BAD_REQUEST, "제출 가능한 횟수를 초과했습니다."),
    ;
    // test
    private final HttpStatus status;
    private final String message;

}
