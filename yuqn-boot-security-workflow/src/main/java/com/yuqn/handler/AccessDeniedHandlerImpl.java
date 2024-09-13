package com.yuqn.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuqn.utils.WebUtils;
import com.yuqn.vo.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: yuqn
 * @Date: 2024/5/26 16:11
 * @description:
 * 自定义异常，用于让security返回的异常符合自己定义的异常类型，方便前端用户解析
 * 这个类只处理授权异常，不处理认证异常
 * @version: 1.0
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //ResponseResult是我们在domain目录写好的实体类。HttpStatus是spring提供的枚举类，FORBIDDEN表示403状态码
        Result result = Result.error(302,"您没有权限进行访问");
        //把上面那行拿到的result对象转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        //WebUtils是我们在utils目录写好的类
        WebUtils.renderString(response,json);
    }
}
