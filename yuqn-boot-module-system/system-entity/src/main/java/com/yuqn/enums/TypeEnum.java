package com.yuqn.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeEnum implements IEnum<Integer> {
    // 社团创建类型（0教师创建 1社联创建 2学生创建）
    TEACHER_LEVEL(0,"教师创建"),
    SOCIAL_FEDERATION(1,"社联创建"),
    STUDENT_LEVEL(2,"学生创建"),;

    private final int value;
    private final String message;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
