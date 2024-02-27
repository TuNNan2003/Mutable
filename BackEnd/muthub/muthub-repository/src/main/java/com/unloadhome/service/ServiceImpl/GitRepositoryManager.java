package com.unloadhome.service.ServiceImpl;

import com.unloadhome.config.MuthubRepoConfig;
import com.unloadhome.controller.MuthubRepositoryController;
import com.unloadhome.model.userInfo;
import org.apache.catalina.User;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitRepositoryManager {
    @Autowired
    private static Logger logger = LoggerFactory.getLogger(GitRepositoryManager.class);
    @Autowired
    private static MuthubRepoConfig repoConfig;
    public void createRepo(long userID, String repoName) throws IOException, GitAPIException {
        File GitPath = new File(
                        repoConfig.getRootPath() +
                        userID + File.separator +
                        repoName + File.separator +
                        ".git"
        );
        File RepoPath = GitPath.getParentFile();
        if(!RepoPath.exists()){
            try{
                RepoPath.mkdirs();
            }catch (SecurityException ex){
                logger.error(ex.getMessage());
                throw new IOException(ex.getMessage());
            }
        }

        try {
            Git git = Git.init().setDirectory(RepoPath).call();
            logger.info("try new Repo: " + git.getRepository().getDirectory());
        }
        catch (GitAPIException ex){
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public boolean gitAdd(long userID, String repoPath, String fileName)throws GitAPIException{
        File repoFile = new File(repoPath);
        Git git = Git.init().setDirectory(repoFile).call();
        git.add().addFilepattern(fileName).call();
        return true;
    }

    public boolean commit(userInfo user, String repoPath, String CommitMessage)throws GitAPIException{
        File repoFile = new File(repoPath);
        Git git = Git.init().setDirectory(repoFile).call();
        logger.info("[JGIT opertion]:" + repoPath + CommitMessage + " user: " + user.getEmail());
        git.add().addFilepattern(".").call();
        git.add().addFilepattern(".").setUpdate(true).call();
        git.commit().setMessage(CommitMessage).setAuthor(user.getName(), user.getEmail()).call();
        return true;
    }
}
