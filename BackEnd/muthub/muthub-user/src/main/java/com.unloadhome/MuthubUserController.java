package com.unloadhome;

import com.unloadhome.common.Result;
import com.unloadhome.common.Status;
import com.unloadhome.dubbointerface.IdResponse;
import com.unloadhome.service.RegisterService;
import com.unloadhome.service.UserService;
import org.apache.catalina.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.remoting.IdleSensible;
import com.unloadhome.dubbointerface.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unloadhome.model.*;

@RestController
public class MuthubUserController {
    @DubboReference
    private IdService idService;
    @Autowired
    RegisterService registerService;
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public Result login(@RequestBody loginRequest request){
        loginStatus status = userService.login(request.getEmail(), request.getPassword());
        if(status == loginStatus.SUCCESS){
            return Result.succ(200, "login success", status);
        }else{
            return Result.fail("email or password error");
        }
    }

    @RequestMapping("/register")
    public Result register(@RequestBody regRequest request){
        IdResponse idResponse = idService.getId();
        if(idResponse.getStatus() == Status.SUCCESS){
            userInfo newUser = new userInfo(
                    idResponse.getId(),
                    request.getName(),
                    request.getPassword(),
                    request.getEmail()
            );
            regStatus status = registerService.Register(newUser);
            if(status == regStatus.SUCCESS){
                return Result.succ(newUser);
            }else{
                return Result.fail(500, "register failed", status);
            }
        }else{
            return Result.fail("id service failed");
        }
    }

}
