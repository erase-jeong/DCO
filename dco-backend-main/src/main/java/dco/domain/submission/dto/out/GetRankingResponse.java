package dco.domain.submission.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetRankingResponse {

	private String memberName;
	private double score;
	private String submitDate;
}
