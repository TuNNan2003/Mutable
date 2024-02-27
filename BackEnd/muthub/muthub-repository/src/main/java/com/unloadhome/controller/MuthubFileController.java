package com.unloadhome.controller;

import com.unloadhome.common.Result;
import com.unloadhome.service.ServiceImpl.RepoServiceImpl;
import com.unloadhome.service.ServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MuthubFileController {
    @Autowired
    private RepoServiceImpl repoService;
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/AddFiles")
    public Result AddNew(@RequestParam("File")MultipartFile[] file,
                         @RequestParam("userID")long userID,
                         @RequestParam("RepoName")String RepoName,
                         @RequestParam("CommitMessage")String commitMessage,
                         @RequestParam("Description")String description){
        if(userService.checkId(userID)) {
            boolean resu = repoService.AddFiles(file, userID, RepoName, commitMessage, description);
            if(resu){
                return Result.succ("success add and commit");
            }
        }
        return Result.fail("can't upload new file");
    }

}
