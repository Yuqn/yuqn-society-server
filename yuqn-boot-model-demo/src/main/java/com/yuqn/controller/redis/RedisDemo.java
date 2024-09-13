package com.yuqn.controller.redis;

import com.yuqn.entity.RedisObj;
import com.yuqn.utils.ActivitiCache;
import com.yuqn.utils.RedisCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yuqn
 * @Date: 2024/4/23 22:33
 * @description:
 * redis demo
 * @version: 1.0
 */
@RestController
@RequestMapping("/demo")
@Tag(name = "demo模块")
public class RedisDemo {

    private RedisCache redisCache;
    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @GetMapping("/set")
    @Operation(summary = "保存redis")
    public String setmes(){
        RedisObj redisObj = new RedisObj("01","yuqn",23);
        try {
            redisCache.setCacheObject("name","余其楠");
            redisCache.setCacheObject("cacheObj",redisObj);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("保存失败");
            return "保存失败";
        }
        return "保存成功";
    }
    @GetMapping("/get")
    @Operation(summary = "获取redis")
    public String getmes(){
        try {
            String name = redisCache.getCacheObject("name");
            RedisObj cacheObj = redisCache.getCacheObject("cacheObj");
            System.out.println("name = " + name);
            System.out.println("cacheObj = " + cacheObj);
        }catch (Exception e){
            System.out.println("保存失败");
            return "获取失败";
        }
        return "获取成功";
    }
}
