package com.unloadhome.service.ServiceImpl;

import com.unloadhome.Mapper.RepoMapper;
import com.unloadhome.common.RepoVisible;
import com.unloadhome.common.Result;
import com.unloadhome.config.MuthubRepoConfig;
import com.unloadhome.model.userInfo;
import com.unloadhome.service.RepoService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class RepoServiceImpl implements RepoService {
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoServiceImpl.class);
    @Autowired
    private RepoMapper repoMapper;

    @Autowired
    private GitRepositoryManager gitRepositoryManager;

    @Autowired
    private MuthubRepoConfig repoConfig;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private UserServiceImpl userService;
    @Override
    public boolean TestReponame(long userID, String testName) {
        List<String> allname = repoMapper.FindAllRepoName(userID);
        return !allname.contains(testName);
    }

    @Override
    public String getRepoPath(long userID, String repoName) {
        return repoConfig.getRootPath() +
                userID + File.separator +
                repoName + File.separator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean NewRepo(long userID, String repoName, RepoVisible visible) {
        try{
            String visi = visible == RepoVisible.PRIVATE ? "F" : "T";
            repoMapper.NewRepo(userID, repoName, visi);
            gitRepositoryManager.createRepo(userID, repoName);
        }
        catch (PersistenceException | IOException | GitAPIException e){
            LOGGER.error("Failed to insert table repo", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean AddFiles(MultipartFile[] files, long userID, String repoName, String commitMessage, String description) {
        userInfo user = userService.getUser(userID);
        if(user == null){
            LOGGER.error("user not found: " + userID);
            return false;
        }
        String path = this.getRepoPath(userID, repoName);
        try {
            for(MultipartFile file : files){
                String filePath = path + file.getOriginalFilename();
                fileService.addfile(filePath, file);
                gitRepositoryManager.gitAdd(userID, path, file.getOriginalFilename());
            }
            gitRepositoryManager.commit(user, path, commitMessage);
        }catch (GitAPIException | IOException ex){
            LOGGER.error("Filed to add file and commit");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public Result getRepoVisible(long userID, String repoName) {
        if(checkRepoExist(userID, repoName)){
            if(repoMapper.getVisible(userID, repoName).equals("T")){
                return Result.succ(200, "public repo", RepoVisible.PUBLIC);
            }else {
                return Result.succ(200, "private repo", RepoVisible.PRIVATE);
            }
        }else{
            return Result.fail(500, "invalid userID or repoName", null);
        }
    }

    @Override
    public boolean checkRepoExist(long userID, String repoName) {
        if(repoMapper.getRepo(userID, repoName).size() == 1){
            return true;
        }else{
            return false;
        }
    }
}
