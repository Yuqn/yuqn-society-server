package com.yuqn.service.impl;

import com.yuqn.entity.LoginUser;
import com.yuqn.entity.User;
import com.yuqn.service.LoginService;
import com.yuqn.utils.JwtUtil;
import com.yuqn.utils.RedisCache;
import com.yuqn.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: yuqn
 * @Date: 2024/5/23 22:46
 * @description:
 * 登录业务，通过自定义的登录方式进行验证，成功保存
 * @version: 1.0
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Result login(User user) {
        //authenticationManager authenticate 进行用户认证，通过封装的authenticationToken进行验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        System.out.println("authenticationToken = " + authenticationToken);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        System.out.println("authenticate = " + authenticate);
        // 如果认证没提过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过，使用userid生成一个jwt，jwt存入responseresult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        System.out.println("loginuser：" + loginUser);
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整的用户信息存入到redis userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        return Result.OK("登录成功",map);
    }

    @Override
    public Result logout() {
        // 获取securitycontextholder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userid = loginUser.getUser().getId();
        // 删除redis中的值
        redisCache.deleteObject("login:" + userid);
        return Result.OK("注销成功");
    }
}
