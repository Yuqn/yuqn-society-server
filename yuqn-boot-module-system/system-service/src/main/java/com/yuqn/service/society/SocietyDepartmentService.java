package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.Result;

import java.util.List;

/**
* @author yuqn
* @description 针对表【society_department】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SocietyDepartmentService extends IService<SocietyDepartment> {
    /**
     * @author: yuqn
     * @Date: 2025/3/5 2:47
     * @description:
     * 获取部门列表
     * @param: null
     * @return: null
     */
    Result getDepartmentById(String societyId, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/5 3:23
     * @description:
     * 获取部门成员列表
     * @param: null
     * @return: null
     */
    Result getDepartmentUserById(String departmentId);
}
