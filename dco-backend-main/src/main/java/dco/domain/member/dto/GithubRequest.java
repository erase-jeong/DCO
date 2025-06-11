package dco.domain.member.dto;

import lombok.Getter;

@Getter
public class GithubRequest {
    private String githubToken;
    private String githubName;
    private String repositoryName;
}
