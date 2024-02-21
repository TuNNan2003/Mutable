package com.unloadhome.service;

import cn.hutool.core.collection.CollUtil;
import com.unloadhome.Mapper.UserMapper;
import com.unloadhome.common.Message;
import com.unloadhome.model.SecurityUser;
import com.unloadhome.model.userInfo;
import org.apache.http.MessageConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import javax.security.auth.login.CredentialExpiredException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<userInfo> findList = userMapper.checkEmailExist(username);
        if(CollUtil.isEmpty(findList)){
            throw new UsernameNotFoundException(Message.EMAIL_ERROR);
        }
        //TODO: 不规范：数据库明文存储密码
        userInfo user = findList.get(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new SecurityUser(user);

    }
}
