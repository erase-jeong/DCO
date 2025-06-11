package dco.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private final T data;
    private final String message;

    // message만 반환하는 생성자
    public Response(String message) {
        this.data = null;
        this.message = message;
    }
}