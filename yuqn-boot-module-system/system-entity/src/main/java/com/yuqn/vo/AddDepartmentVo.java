package com.yuqn.vo;

import com.yuqn.entity.society.SocietyDepartment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDepartmentVo {
    private String societyId;
    private String userRole;
    private List<UserDepartmentRole> departmentValue;
}
