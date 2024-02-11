package com.unloadhome.controller;

import com.unloadhome.dubbointerface.IdResponse;
import com.unloadhome.service.SnowflakeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unloadhome.dubbointerface.IdService;

@RestController
@ComponentScan
@DubboService
public class MuthubIdController implements IdService{
    @Autowired
    private SnowflakeService snowflakeService;

    @RequestMapping("/getId")
    @Override
    public IdResponse getId(){
        return snowflakeService.getId("");
    }
}
