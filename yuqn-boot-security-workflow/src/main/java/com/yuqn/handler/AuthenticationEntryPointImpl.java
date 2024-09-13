package com.yuqn.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuqn.utils.WebUtils;
import com.yuqn.vo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: yuqn
 * @Date: 2024/5/26 15:49
 * @description:
 * 自定义异常，用于让security返回的异常符合自己定义的异常类型，方便前端用户解析
 * 这个类只处理认证异常，不处理授权异常
 * @version: 1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //ResponseResult是我们在domain目录写好的实体类。HttpStatus是spring提供的枚举类，UNAUTHORIZED表示401状态码
        Result result =Result.error(302,"用户认证失败，请重新登录");
        //把上面那行拿到的result对象转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        //WebUtils是我们在utils目录写好的类
        WebUtils.renderString(response,json);
    }
}
