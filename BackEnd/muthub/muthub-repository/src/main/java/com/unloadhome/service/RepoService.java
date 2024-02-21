package com.unloadhome.service;

import com.unloadhome.common.RepoVisible;

public interface RepoService {
    boolean TestReponame(long userID, String testName);
    boolean NewRepo(long userID, String repoName, RepoVisible visible);
}
