package com.yuqn.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yuqn
 * @Date: 2024/4/23 15:32
 * @description:
 * 配置 Knife4j
 * @version: 1.0
 */
@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI springShopOpenApi(){
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("接口文档")
                        // 接口文档简介
                        .description("接口文档简介")
                        // 接口文档版本
                        .version("1.0.0版本")
                        // 开发者联系方式
                        .contact(new Contact().name("yuqn")
                                .email("2572655497@qq.com")));
    }
}
