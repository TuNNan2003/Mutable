package com.unloadhome.service;

public interface RepoActService {
    boolean Star(long userID, long ownerID, String repoName);
    boolean Watch(long userID, long ownerID, String repoName);
    boolean Collaborate(long userID, long ownerID, String repoName);

    boolean UnStar(long userID, long ownerID, String repoName);

    boolean UnWatch(long userID, long ownerID, String repoName);

    boolean UnCollaborate(long userID, long ownerID, String repoName);
}
