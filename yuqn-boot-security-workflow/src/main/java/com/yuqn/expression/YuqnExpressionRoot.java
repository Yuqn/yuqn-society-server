package com.yuqn.expression;

import com.yuqn.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: yuqn
 * @Date: 2024/5/26 17:45
 * @description:
 * 自定义security权限认证
 * @version: 1.0
 */
@Component("ex")
public class YuqnExpressionRoot {
    public boolean hasAuthority(String authority){
        // 获取当前用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        // 判断用户权限集合中是否存在authority
        return permissions.contains(authority);
    }
}
