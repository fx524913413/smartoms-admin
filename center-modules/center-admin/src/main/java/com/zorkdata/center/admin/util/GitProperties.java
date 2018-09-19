package com.zorkdata.center.admin.util;

import com.zorkdata.center.admin.config.FileConfiguration;
import com.zorkdata.center.admin.config.GitConfigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GitProperties {
    private String localRepositoryDir;
    private String localRepositoryConfigDir;
    private String remoteRepositoryURI;
    private String saltPath;
    private String gitUser;
    private String gitPass;
    private String localRepositoryDefaultFileDir;
    private String localRepositoryTempFileDir;
    private String localRepositoryPublicFileDir;
    private String localRepositoryFastFile;

    public GitProperties(){
        this.localRepositoryDir = "D:\\GitRepository\\FileCenter";
        this.localRepositoryConfigDir = "D:\\GitRepository\\FileCenter\\.git";
        this.remoteRepositoryURI = "http://210.22.155.10:8888/File/FileCenter.git";
        this.saltPath = "salt://";
        this.gitUser = "cairuixiang";
        this.gitPass = "cs1991118";
    }

    public GitProperties getGitProperties(){
        GitProperties gitProperties = new GitProperties();
        return gitProperties;
    }

    public String getLocalRepositoryDir() {
        return localRepositoryDir;
    }

    public void setLocalRepositoryDir(String localRepositoryDir) {
        this.localRepositoryDir = localRepositoryDir;
    }

    public String getLocalRepositoryConfigDir() {
        return localRepositoryConfigDir;
    }

    public void setLocalRepositoryConfigDir(String localRepositoryConfigDir) {
        this.localRepositoryConfigDir = localRepositoryConfigDir;
    }

    public String getRemoteRepositoryURI() {
        return remoteRepositoryURI;
    }

    public void setRemoteRepositoryURI(String remoteRepositoryURI) {
        this.remoteRepositoryURI = remoteRepositoryURI;
    }

    public String getSaltPath() {
        return saltPath;
    }

    public void setSaltPath(String saltPath) {
        this.saltPath = saltPath;
    }

    public String getGitUser() {
        return gitUser;
    }

    public void setGitUser(String gitUser) {
        this.gitUser = gitUser;
    }

    public String getGitPass() {
        return gitPass;
    }

    public void setGitPass(String gitPass) {
        this.gitPass = gitPass;
    }

    public String getLocalRepositoryDefaultFileDir() {
        return localRepositoryDefaultFileDir;
    }

    public void setLocalRepositoryDefaultFileDir(String localRepositoryDefaultFileDir) {
        this.localRepositoryDefaultFileDir = localRepositoryDefaultFileDir;
    }

    public String getLocalRepositoryFastFile() {
        return localRepositoryFastFile;
    }

    public void setLocalRepositoryFastFile(String localRepositoryFastFile) {
        this.localRepositoryFastFile = localRepositoryFastFile;
    }

    public String getLocalRepositoryTempFileDir() {
        return localRepositoryTempFileDir;
    }

    public void setLocalRepositoryTempFileDir(String localRepositoryTempFileDir) {
        this.localRepositoryTempFileDir = localRepositoryTempFileDir;
    }

    public String getLocalRepositoryPublicFileDir() {
        return localRepositoryPublicFileDir;
    }

    public void setLocalRepositoryPublicFileDir(String localRepositoryPublicFileDir) {
        this.localRepositoryPublicFileDir = localRepositoryPublicFileDir;
    }
}
