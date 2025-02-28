package com.unloadhome.service;

import cn.hutool.core.collection.CollUtil;
import com.unloadhome.common.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ResourceServiceImpl {
    private Map<String, List<String>> resourceRolesMap;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initData() {
        resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/user/hello", CollUtil.toList("guest"));
        resourceRolesMap.put("/repo/test", CollUtil.toList("guest"));
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }
}
