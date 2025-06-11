package dco.domain.submission.dto;

import dco.domain.submission.entity.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GradeResponse {
    private Integer number;
    private Result result;
}
