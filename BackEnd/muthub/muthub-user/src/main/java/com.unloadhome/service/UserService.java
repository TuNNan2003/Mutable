package com.unloadhome.service;

import com.unloadhome.Mapper.UserMapper;
import com.unloadhome.model.loginStatus;
import com.unloadhome.model.userInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 要求前端对两个参数做非空检验
     * @param email
     * @param password
     * @return loginStatus
     */
    public loginStatus login(String email, String password){
        List<userInfo> result = userMapper.login(email, password);
        return result.size() == 1 ? loginStatus.SUCCESS : loginStatus.ERROR;
    }

    //TODO: implement
    public userInfo getByUserName(String username){
        return null;
    }

    public String getUserAuthorityInfo(long userId){
        return null;
    }

}
