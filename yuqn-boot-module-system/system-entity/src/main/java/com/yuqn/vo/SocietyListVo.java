package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: yuqn
 * @Date: 2025/2/28 23:58
 * @description:
 * 映射社团列表
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyListVo {
    private String societyId;
    private String societyName;
    private String createBy;
    private String societyIntroduce;
    private String createTime;
    private String societyByName;
    private String roleId;
    private String roleName;
    private String chairman;
    private String viceChairman;
    private Integer societyUserCount;
    private Integer societyDepartmentCount;
}
