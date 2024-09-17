package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum implements IEnum<Integer> {
    TEACHER(0,"老师"),
    SOCIAL_FEDERANTION(1,"社联"),
    VICE_PRESIDENT(2,"副主席"),
    CHAIRMAN(3,"主席"),
    DEPUTY_MINISTER(4,"副部长"),
    MINISTER(5,"部长"),
    OFFICER(6,"干事"),
    STUDENT(7,"学生");

    private final int value;
    private final String message;


    @Override
    public Integer getValue() {
        return this.value;
    }
}
