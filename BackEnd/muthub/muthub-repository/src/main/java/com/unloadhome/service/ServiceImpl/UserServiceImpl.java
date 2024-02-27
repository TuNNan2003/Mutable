package com.unloadhome.service.ServiceImpl;

import com.unloadhome.Mapper.UserMapper;
import com.unloadhome.model.userInfo;
import com.unloadhome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public userInfo getUser(long userID) {
        if(userMapper.checkIdExist(userID).size() == 1){
            return userMapper.selectUser(userID);
        }
        return null;
    }

    @Override
    public boolean checkId(long userID) {
        return !userMapper.checkIdExist(userID).isEmpty();
    }
}
