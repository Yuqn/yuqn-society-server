package com.yuqn.vo;

import com.yuqn.entity.society.SocietyDepartment;
import com.yuqn.entity.society.SysUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUpdateVo {
    private SocietyDepartment societyDepartment;
    /**
     * @author: yuqn
     * @Date: 2025/3/14 0:16
     * @description:
     * 部长信息
     * @param: null
     * @return: null
     */
    private SysUserRole minister;
    /**
     * @author: yuqn
     * @Date: 2025/3/14 0:16
     * @description:
     * 副部长信息
     * @param: null
     * @return: null
     */
    private List<SysUserRole> viceMinister;
}
