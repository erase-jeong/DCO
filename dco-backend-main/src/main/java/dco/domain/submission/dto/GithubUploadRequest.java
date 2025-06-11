package dco.domain.submission.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class GithubUploadRequest {

    private String githubToken;
    private String repositoryName;
    private String githubName;


}
