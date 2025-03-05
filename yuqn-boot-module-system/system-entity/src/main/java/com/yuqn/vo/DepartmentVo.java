package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVo {
    private String id;
    private String societyName;
    private String name;
    private String createTime;
    private String introduce;
    private String minister;
    private String viceMinister;
    private Integer count;
    // 成员是否加入社团
    private Integer isJoin;
}
