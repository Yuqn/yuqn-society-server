package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum implements IEnum<Integer> {
    ENABLE(0,"启用"),
    DISABLE(1,"禁用");

    private final int value;
    private final String message;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
