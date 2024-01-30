package com.unloadhome;

//import org.apache.dubbo.config.annotation.DubboReference;
//import org.apache.dubbo.remoting.IdleSensible;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unloadhome.model.*;

@RestController
public class MuthubUserController {
//    @DubboReference
//    private IdService idService;
    @RequestMapping("/login")
    public loginResponse login(@RequestBody loginRequest request){
        System.out.println(request);
        return new loginResponse(true, "2004/1/12");
    }

    @RequestMapping("/register")
    public regResponse register(@RequestBody regRequest request){
        System.out.println(request);
//        String id = idService.getId();
        String id = "";
        System.out.println("get id" + id);
        return new regResponse();
    }

}
