package dco.domain.submission.entity;

import lombok.Getter;

@Getter
public enum Result {
    CORRECT("정답"),
    INCORRECT("오답"),
    TIMEOUT("시간초과");

    private final String description;

    Result(String description) {
        this.description = description;
    }
}
