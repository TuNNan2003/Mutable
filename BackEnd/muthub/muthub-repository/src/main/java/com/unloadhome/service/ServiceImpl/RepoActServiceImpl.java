package com.unloadhome.service.ServiceImpl;

import com.unloadhome.Mapper.UserRepoActMapper;
import com.unloadhome.model.UserRepoAct;
import com.unloadhome.service.RepoActService;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoActServiceImpl implements RepoActService{
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(RepoActServiceImpl.class);
    @Autowired
    private UserRepoActMapper userRepoActMapper;
    @Override
    public boolean Star(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.StarRepo(userID, ownerID, repoName);
        }else{
            userRepoActMapper.InsertEntity(userID, ownerID, repoName);
            userRepoActMapper.StarRepo(userID, ownerID, repoName);
        }
        LOGGER.info(userID + " stared repo: " + ownerID + "-" + repoName);
        return true;
    }

    @Override
    public boolean Watch(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.WatchRepo(userID, ownerID, repoName);
        }else{
            userRepoActMapper.InsertEntity(userID, ownerID, repoName);
            userRepoActMapper.WatchRepo(userID, ownerID, repoName);
        }
        LOGGER.info(userID + " watched repo: " + ownerID + "-" + repoName);
        return true;
    }

    @Override
    public boolean Collaborate(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.CollaborateRepo(userID, ownerID, repoName);
        }else{
            userRepoActMapper.InsertEntity(userID, ownerID, repoName);
            userRepoActMapper.CollaborateRepo(userID, ownerID, repoName);
        }
        LOGGER.info(userID + " collaborate repo: " + ownerID + "-" + repoName);
        return true;
    }

    @Override
    public boolean UnStar(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.UnStarRepo(userID, ownerID, repoName);
            LOGGER.info(userID + " stared repo: " + ownerID + "-" + repoName);
            return true;
        }else{
            LOGGER.info(userID + "trying to unstar a repo never stared");
            return false;
        }
    }

    @Override
    public boolean UnWatch(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.UnWatchRepo(userID, ownerID, repoName);
            LOGGER.info(userID + " Unwatched repo: " + ownerID + "-" + repoName);
            return true;
        }else{
            LOGGER.info(userID + "trying to unwatch a repo never watched");
            return false;
        }
    }

    @Override
    public boolean UnCollaborate(long userID, long ownerID, String repoName) {
        if(userRepoActMapper.CheckEntity(userID, ownerID, repoName).size() == 1) {
            userRepoActMapper.UnCollaborateRepo(userID, ownerID, repoName);
            LOGGER.info(userID + " uncollaborate repo: " + ownerID + "-" + repoName);
            return true;
        }else{
            LOGGER.info(userID + "trying to uncollaborate a repo never collaborated");
            return false;
        }
    }
}
