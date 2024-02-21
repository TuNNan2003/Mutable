package com.unloadhome.service.ServiceImpl;

import com.unloadhome.Mapper.RepoMapper;
import com.unloadhome.common.RepoVisible;
import com.unloadhome.service.RepoService;
import org.apache.ibatis.exceptions.PersistenceException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.List;

@Service
public class RepoServiceImpl implements RepoService {
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoServiceImpl.class);
    @Autowired
    RepoMapper repoMapper;

    @Autowired
    GitRepositoryManager gitRepositoryManager;
    @Override
    public boolean TestReponame(long userID, String testName) {
        List<String> allname = repoMapper.FindAllRepoName(userID);
        return !allname.contains(testName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean NewRepo(long userID, String repoName, RepoVisible visible) {
        try{
            repoMapper.NewRepo(userID, repoName, visible);
            gitRepositoryManager.createRepo("/repo" + userID + repoName);
        }
        catch (PersistenceException | IOException | GitAPIException e){
            LOGGER.error("Failed to insert table repo", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}
