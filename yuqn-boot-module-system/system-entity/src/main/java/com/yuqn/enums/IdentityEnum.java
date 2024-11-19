package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
