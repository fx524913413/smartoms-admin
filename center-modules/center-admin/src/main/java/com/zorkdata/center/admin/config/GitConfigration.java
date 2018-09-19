package com.zorkdata.center.admin.config;

import lombok.Data;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class GitConfigration {
    @Value("${git.gitpath}")
    private String gitPath;
    @Value("${git.localRepositoryDir}")
    private String localRepositoryDir;
    @Value("${git.localRepositoryConfigDir}")
    private String localRepositoryConfigDir;
    @Value("${git.remoteRepositoryURI}")
    private String remoteRepositoryURI;
    @Value("${git.saltPath}")
    private String saltPath;
    @Value("${git.gitUser}")
    private String gitUser;
    @Value("${git.gitPass}")
    private String gitPass;

    @Bean(name = "credentialsProvider")
    public CredentialsProvider credentialsProvider(){
        return new UsernamePasswordCredentialsProvider(gitUser,gitPass);
    }
}
