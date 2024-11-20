package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yuqn
 * @Date: 2024/11/19 14:46
 * @description:
 * 用户类型，用于区分学生、教师
 * @version: 1.0
 */
@Getter
@AllArgsConstructor
public enum IdentityEnum implements IEnum<Integer> {

    STUDENT(0,"学生"),
    TEACHER(1,"教师");

    private final int value;
    private final String message;

    @Override
    public Integer getValue() {
        return this.value;
    }

}
