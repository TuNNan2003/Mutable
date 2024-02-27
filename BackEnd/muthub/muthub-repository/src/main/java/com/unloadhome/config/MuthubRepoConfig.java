package com.unloadhome.config;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MuthubRepoConfig {
    public String getRootPath() {
        return "F:" + File.separator + "Muthub" + File.separator;
    }

}
