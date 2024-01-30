package com.unloadhome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unloadhome.common.Response;
import com.unloadhome.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.unloadhome.dubbointerface.IdService;

@RestController
@ComponentScan
public class MuthubIdController implements IdService{
    @Autowired
    private SnowflakeService snowflakeService;

    @RequestMapping("/getId")
    @Override
    public String getId(){
        return String.valueOf(snowflakeService.getId(""));
    }
}
