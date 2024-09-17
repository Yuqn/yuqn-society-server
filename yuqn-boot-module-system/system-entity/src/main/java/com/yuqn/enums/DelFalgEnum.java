package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelFalgEnum implements IEnum<Integer> {

    NOTDEL(0,"正常"),
    DEL(1,"删除");

    private final int value;
    private final String message;


    @Override
    public Integer getValue() {
        return this.value;
    }
}
