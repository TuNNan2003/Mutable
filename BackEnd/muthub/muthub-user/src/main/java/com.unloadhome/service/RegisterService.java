package com.unloadhome.service;

import com.unloadhome.Mapper.UserMapper;
import com.unloadhome.model.regStatus;
import com.unloadhome.model.userInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private UserMapper userMapper;
    public regStatus Register(userInfo info){
        if(info.getName().length() > 40){
            return regStatus.Name_TOOLONG;
        }
        if (info.getPassword().length() > 72) {
            return regStatus.Password_TOOLONG;
        }
        if(info.getEmail().length() > 72){
            return regStatus.Email_TOOLONG;
        }
        if(userMapper.checkEmailExist(info.getEmail()).isEmpty()){
            userMapper.register(info);
            return regStatus.SUCCESS;
        }else{
            return regStatus.Email_EXIST;
        }
    }
}
