package com.unloadhome.service;

import com.unloadhome.common.Result;

public interface MessageProduceService {
    public Result Watchnotification(long userID, long ownerId, String repoName);
}
