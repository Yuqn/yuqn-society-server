package com.yuqn.config;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
/**
 * @author: yuqn
 * @Date: 2024/4/23 14:36
 * @description:
 * 处理 springboot3.x 整合 mybatisplus 启动项目后报错的问题
 * @version: 1.0
 */
@Configuration
public class DdlApplicationRunnerConfig {
    @Bean
    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List ddlList) {
        return new DdlApplicationRunner(ddlList);
    }
}
