package com.yuqn.filter;

import com.yuqn.entity.LoginUser;
import com.yuqn.utils.JwtUtil;
import com.yuqn.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String token = request.getHeader("token");
        System.out.println("token----" + token);
        if (!StringUtils.hasText(token)) {
            // 放行，后面还有security过滤器拦截
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            System.out.println("userid = " + userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        System.out.println("loginUser = " + loginUser);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }
        // 存入到SecurityContextHolder，其中UsernamePasswordAuthenticationToken有三个参数，最后一个表示是否已认证状态，这里已经获取到用户token，那必须是已经登录认证状态，所以用三个参数
        // 获取权限信息封装到authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request,response);
    }
}
