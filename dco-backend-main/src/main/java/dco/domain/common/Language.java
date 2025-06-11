package dco.domain.common;

import lombok.Getter;

@Getter
public enum Language {
    JAVA("자바"), PYTHON("파이썬"), CPP("C++");

    private final String description;

    Language(String description) {
        this.description = description;
    }
}
