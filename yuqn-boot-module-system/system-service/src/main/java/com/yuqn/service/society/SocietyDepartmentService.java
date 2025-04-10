package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.AddDepartmentVo;
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

    /**
     * @author: yuqn
     * @Date: 2025/3/7 19:40
     * @description:
     * 新建部门
     * @param: null
     * @return: null
     */
    Result addDepartment(AddDepartmentVo addDepartmentVo, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/7 21:12
     * @description:
     * 解散社团
     * @param: null
     * @return: null
     */
    Result deleteDepartment(AddDepartmentVo addDepartmentVo, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/9 0:27
     * @description:
     * 查看用户详细信息
     * @param: null
     * @return: null
     */
    Result getDepartmentUserDetailsById(String societyId, String departmentId, String userId, String roleId);
}
