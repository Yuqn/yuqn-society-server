package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: yuqn
 * @Date: 2025/2/28 23:58
 * @description:
 * 映射用户社团角色
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDepartmentRole {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 社团id
     */
    private String departmentId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 社团名字
     */
    private String departmentName;
    /**
     * 角色名字
     */
    private String roleName;
    /**
     * 角色key
     */
    private String roleKey;
}
