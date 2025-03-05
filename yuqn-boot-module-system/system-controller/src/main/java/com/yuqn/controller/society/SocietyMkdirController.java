package com.yuqn.controller.society;

import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyMkdirVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/society/mkdir")
@Tag(name = "创建社团模块")
public class SocietyMkdirController {
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
        System.out.println("societyMkdirVo = " + societyMkdirVo);
        System.out.println("token = " + token);
        return null;
    }
}
