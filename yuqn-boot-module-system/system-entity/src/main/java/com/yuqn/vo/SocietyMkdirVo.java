package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: yuqn
 * @Date: 2025/3/5 19:37
 * @description:
 * 创建社团的类
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyMkdirVo {
    private String societyName;
    private String createTime;
    private String createBy;
    private String societyIntroduce;
    private List<DepartmentValueVo> departmentValue;
    private UserRole userRole;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentValueVo{
        private String departmentName;
        private String departmentIntroduce;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRole{
        private String roleId;
        private String departmentId;
        private String societyBodyId;
    }
}

