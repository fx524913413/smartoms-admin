package com.zorkdata.center.admin.util;


import com.zorkdata.center.admin.config.GitConfigration;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author SwinBlackSea
 * @date 2017/12/27 21:05
 */
@Component
public class GitUtil {
    @Autowired
    private GitConfigration gitConfigration;
    private  Logger log = Logger.getLogger(GitUtil.class);

    public GitUtil() {

    }



    public  String push() {
        Git git;
        try {
            git = Git.open(new File(gitConfigration.getLocalRepositoryConfigDir()));
            git.push().setCredentialsProvider(gitConfigration.credentialsProvider()).call();
            System.out.println("push success");
            return CommonEnum.SUCCESS.getValue();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            log.error("push代码到远程失败");
            return CommonEnum.FAILED.getValue();
        }
    }

    private  String commit() {
        Git git;
        try {
            git = Git.open(new File(gitConfigration.getLocalRepositoryConfigDir()));
            git.add().addFilepattern(".").call();
            CommitCommand commit = git.commit();
            commit.setCommitter("admin", "yingyongpeizhi@zork.com");
            commit.setAuthor("admin", "yingyongpeizhi@zork.com");
            commit.setAll(true);
            RevCommit addedPets = commit.setMessage("no messager").call();
            System.out.println("commit success");
            return CommonEnum.SUCCESS.getValue();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            log.error("commit失败");
            return CommonEnum.FAILED.getValue();
        }
    }

    /**
     * @return "success" "failed"
     */
    public  String commitPullAndPush() {
        String commit = commit();
        if (CommonEnum.SUCCESS.getValue().equals(commit)) {
            Boolean pull = pull();
            if (pull) {
                return push();
            }
            return CommonEnum.FAILED.getValue();
        }
        return commit;
    }

    /**
     * @return " success"、"failed"
     */
    public  String commitAndPush() {
        String commit = commit();
        if (CommonEnum.SUCCESS.getValue().equals(commit)) {
            return push();
        }
        return commit;
    }

    private  Boolean pull() {
        Git git;
        try {
            git = Git.open(new File(gitConfigration.getLocalRepositoryConfigDir()));
            git.pull().setCredentialsProvider(gitConfigration.credentialsProvider()).call();
            System.out.println("pull success");
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 只在系统启动的时候调用
     * 默认file文件下存在.git，如果.git文件不存在就会报错。
     */
    public  void setupRepo() {
        File file = new File(gitConfigration.getLocalRepositoryDir());
        File gitFile = new File(gitConfigration.getLocalRepositoryConfigDir());
        if (file.exists()) {
            if (gitFile.exists()) {
                if (pull()) {
                    return;
                }
                Boolean aBoolean = deleteFolder(file);
                if (!aBoolean){return;}
            } else {
                log.error("该目录下有同名文件，但不是git仓库");
                return;
            }
        }
        try {
            Git.cloneRepository().setURI(gitConfigration.getRemoteRepositoryURI()).setDirectory(new File(gitConfigration.getLocalRepositoryDir())).setCredentialsProvider(gitConfigration.credentialsProvider()).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private  Boolean deleteFolder(File file) {
        if (file.isFile() || file.list() == null) {
            boolean isDelete = file.delete();
            if (!isDelete) {
                return false;
            }
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    deleteFolder(file1);
                    boolean delete = file1.delete();
                    if (!delete) {
                        return false;
                    }
                }
            }
        }
        return false;
    }


    /*
    public static String newBranch(String branchName) {
        String newBranchIndex = "refs/heads/" + branchName;
        String gitPathURI = "";
        Git git = null;
        try {
            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
            List<Ref> refs = git.branchList().call();
            for (Ref ref : refs) {
                if (ref.getName().equals(newBranchIndex)) {
                    System.out.println("Removing branch before");
                    git.branchDelete().setBranchNames(branchName).setForce(true)
                            .call();
                    break;
                }
            }
            //新建分支
            Ref ref = git.branchCreate().setName(branchName).call();
            //推送到远程
            git.push().add(ref).call();
            gitPathURI = getGitProperty().getRemoteRepositoryURI() + " " + "feature/" + branchName;
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return gitPathURI;
    }*/

}
