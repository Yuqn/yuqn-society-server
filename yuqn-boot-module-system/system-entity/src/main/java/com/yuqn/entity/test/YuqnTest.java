package com.yuqn.entity.test;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("yuqn_test")
public class YuqnTest {
    private String id;
    private String message;
}

