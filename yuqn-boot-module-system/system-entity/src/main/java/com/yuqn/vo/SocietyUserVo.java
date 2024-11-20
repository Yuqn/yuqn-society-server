package com.yuqn.vo;

import com.yuqn.enums.SexEnum;
import com.yuqn.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: yuqn
 * @Date: 2024/9/18 8:39
 * @description:
 * 映射用户-学生信息
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyUserVo implements Serializable {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private SexEnum sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 身份证
     */
    private String card;

    /**
     * 所在学院
     */
    private String college;

    /**
     * 所在专业
     */
    private String major;

    /**
     * 所在班级
     */
    private String classes;

    /**
     * 学号
     */
    private String studentId;
}
