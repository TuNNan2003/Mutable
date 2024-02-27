package com.unloadhome.controller;

import com.unloadhome.common.RepoVisible;
import com.unloadhome.common.Result;
import com.unloadhome.service.ServiceImpl.MessageProducerServiceImpl;
import com.unloadhome.service.ServiceImpl.RepoActServiceImpl;
import com.unloadhome.service.ServiceImpl.RepoServiceImpl;
import com.unloadhome.service.ServiceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MuthubRepositoryController {
    @Autowired
    private RepoServiceImpl repoService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RepoActServiceImpl repoActService;

    @Autowired
    private MessageProducerServiceImpl messageProducerService;

    @RequestMapping("/new")
    public Result newRepo(@RequestParam Map<String, String> param){
        System.out.println(param);
        Long userID = Long.parseLong(param.get("userID"));
        String repoName = param.get("repo-name").toString();
        RepoVisible visible = RepoVisible.valueOf(param.get("visible").toString());
        if(repoName.matches("^[\u4E00-\u9FA5A-Za-z0-9_]+$")){
            if(userService.checkId(userID)) {
                if (repoService.NewRepo(userID, repoName, visible)) {
                    return Result.succ("success");
                }
            }else {
                return Result.fail("invalid userID");
            }
            return Result.fail("create repo error");
        }else {
            return Result.fail("invalid repo name");
        }
    }

    @RequestMapping("/star")
    public Result StarRepo(@RequestParam("userID") long userID,
                           @RequestParam("ownerID") long ownerID,
                           @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.Star(userID, ownerID, repoName)) {
                    return Result.succ("star success");
                }else{
                    return Result.fail("can't star this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/watch")
    public Result WatchRepo(@RequestParam("userID") long userID,
                           @RequestParam("ownerID") long ownerID,
                           @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.Watch(userID, ownerID, repoName)) {
                    messageProducerService.Watchnotification(userID, ownerID, repoName);
                    return Result.succ("watch success");
                }else{
                    return Result.fail("can't watch this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/collaborate")
    public Result CollaborateRepo(@RequestParam("userID") long userID,
                            @RequestParam("ownerID") long ownerID,
                            @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.Collaborate(userID, ownerID, repoName)) {
                    return Result.succ("colloborate success");
                }else{
                    return Result.fail("can't watch this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/Unstar")
    public Result UnStarRepo(@RequestParam("userID") long userID,
                           @RequestParam("ownerID") long ownerID,
                           @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.UnStar(userID, ownerID, repoName)) {
                    return Result.succ("Unstar success");
                }else{
                    return Result.fail("can't Unstar this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/Unwatch")
    public Result UnWatchRepo(@RequestParam("userID") long userID,
                            @RequestParam("ownerID") long ownerID,
                            @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.UnWatch(userID, ownerID, repoName)) {
                    return Result.succ("Unwatch success");
                }else{
                    return Result.fail("can't Unwatch this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/Uncollaborate")
    public Result UnCollaborateRepo(@RequestParam("userID") long userID,
                                  @RequestParam("ownerID") long ownerID,
                                  @RequestParam("repoName") String repoName)
    {
        if(repoService.checkRepoExist(ownerID, repoName)){
            if(repoService.getRepoVisible(ownerID, repoName).getData().equals(RepoVisible.PUBLIC)){
                if(repoActService.UnCollaborate(userID, ownerID, repoName)) {
                    return Result.succ("Uncolloborate success");
                }else{
                    return Result.fail("can't Unwatch this repo");
                }
            }else {
                return Result.fail(400, "can't do this operation, private repo", null);
            }
        }
        return Result.fail(400, "invalid argument", null);
    }

    @RequestMapping("/testRepoName")
    public Result testRepoName(@RequestParam Map<String, String> param){
        Long userID = Long.parseLong(param.get("userID"));
        String TempName = param.get("TempName").toString();
        if(repoService.TestReponame(userID, TempName)){
            return Result.succ("able to use this name");
        }
        return Result.fail("this name already used");
    }

    @RequestMapping("/test")
    public void test(@RequestHeader Map<String, String> header, @RequestParam Map<String , String > param){
        System.out.println(param);
        System.out.println("header:\n" + header);
    }
}
/**
 * 同一个用户 repo-name 不能重复
 * 可选是否需要desc
 * 可选是否需要自动添加README
 * README自动添加内容为:
 * ---------------------------------------
 * # repo-name
 * desc
 * ---------------------------------------
 * 目前先不考虑license和.gitignore创建
 */