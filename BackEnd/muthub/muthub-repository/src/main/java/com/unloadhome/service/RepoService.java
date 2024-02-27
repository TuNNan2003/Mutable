package com.unloadhome.service;

import com.unloadhome.common.RepoVisible;
import com.unloadhome.common.Result;
import com.unloadhome.service.ServiceImpl.RepoServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.stream.Stream;

public interface RepoService {
    boolean TestReponame(long userID, String testName);
    boolean NewRepo(long userID, String repoName, RepoVisible visible);

    String getRepoPath(long userID, String repoName);

    boolean AddFiles(MultipartFile[] files, long userID, String repoName,
                     String commitMessage, String description);

    Result getRepoVisible(long userID, String repoName);

    boolean checkRepoExist(long userID, String repoName);
}
