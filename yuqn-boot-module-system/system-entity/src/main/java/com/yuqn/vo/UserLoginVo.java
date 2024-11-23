package com.yuqn.vo;

import com.yuqn.entity.society.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * @author: yuqn
 * @Date: 2024/11/23 22:24
 * @description:
 * 用户登录vo
 * @version: 1.0
 */
public class UserLoginVo {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 电话
     */
    private String phonenumber;
    /**
     * 类型，0学生，1老师
     */
    private String identity;
    /**
     * 登录类型，0手机号码，1账号密码
     */
    private String loginType;
}
