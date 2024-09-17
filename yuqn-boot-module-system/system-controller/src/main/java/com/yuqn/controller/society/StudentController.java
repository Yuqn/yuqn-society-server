package com.yuqn.controller.society;

import com.yuqn.vo.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: yuqn
 * @Date: 2024/9/17 16:22
 * @description:
 * 学生类，提供学生基本功能，如注册、注销等功能查询等功能
 * @version: 1.0
 */
@RestController
@RequestMapping("/student")
@Tag(name = "学生接口")
public class StudentController {
    /**
     * @author: yuqn
     * @Date: 2024/9/17 16:23
     * @description:
     * 注册用户
     * @param: null
     * @return: null
     */
    public Result addStudent(){
        return Result.OK();
    }
}
