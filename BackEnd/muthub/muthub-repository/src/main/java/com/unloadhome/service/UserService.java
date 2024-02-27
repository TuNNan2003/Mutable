package com.unloadhome.service;

import com.unloadhome.model.userInfo;

public interface UserService {
    public boolean checkId(long userID);

    public userInfo getUser(long userID);
}
