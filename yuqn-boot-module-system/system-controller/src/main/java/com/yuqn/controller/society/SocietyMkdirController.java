package com.yuqn.controller.society;

import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyMkdirVo;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/society/mkdir")
@Tag(name = "创建社团模块")
public class SocietyMkdirController {

    @Autowired
    private SocietyBodyService societyBodyService;

    /**
     * @author: yuqn
     * @Date: 2025/3/5 19:27
     * @description:
     * 创建社团
     * @param: null
     * @return: null
     */
    @PostMapping("/societyMkdir")
    @Operation(summary = "创建社团")
    public Result societyMkidr(@RequestBody SocietyMkdirVo societyMkdirVo,@RequestHeader("Token") String token){
        Result result = null;
        if("3".equals(societyMkdirVo.getUserRole().getRoleId())){
            // 如果是老师，直接创建
            String userid;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = claims.getSubject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token非法");
            }
            result = societyBodyService.mkdirSocietyTeacher(societyMkdirVo, userid);
        }else{
            // 非教师身份，需要进入审批
            result = societyBodyService.mkdirSocietyStudent(societyMkdirVo, token);
        }
        System.out.println("token = " + token);
        return result;
    }
}
