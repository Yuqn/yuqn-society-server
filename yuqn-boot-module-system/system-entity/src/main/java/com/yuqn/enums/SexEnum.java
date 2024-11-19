package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SexEnum implements IEnum<Integer> {
    MALE(0,"男"),
    FEMALE(1,"女"),
    UNKNOWN(2,"未知");

    private final int value;
    private final String message;


    @Override
    public Integer getValue() {
        return this.value;
    }
}