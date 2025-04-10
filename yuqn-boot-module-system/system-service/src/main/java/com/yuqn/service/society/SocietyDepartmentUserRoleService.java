package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyDepartment;
import com.yuqn.entity.society.SocietyDepartmentUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.DepartmentUpdateVo;
import com.yuqn.vo.Result;

/**
* @author yuqn
* @description 针对表【society_department_user_role】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SocietyDepartmentUserRoleService extends IService<SocietyDepartmentUserRole> {

    /**
     * @author: yuqn
     * @Date: 2025/3/8 20:38
     * @description:
     * 获取部门列表
     * @param: null
     * @return: null
     */
    Result getDepartmentuserList(String societyId, String departmentId);

    /**
     * @author: yuqn
     * @Date: 2025/3/8 22:04
     * @description:
     * 部门新增成员
     * @param: null
     * @return: null
     */
    Result addDepartmentUser(String societyId, String departmentId, String userId,String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/9 1:41
     * @description:
     * 移除部门
     * @param: null
     * @return: null
     */
    Result deleteDepartment(String departmentId, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/9 2:19
     * @description:
     * 根据id获取帮忙详情
     * @param: null
     * @return: null
     */
    Result getDepartmentDetailsById(String departmentId);
    /**
     * @author: yuqn
     * @Date: 2025/3/9 3:50
     * @description:
     * 获取部门用户
     * @param: null
     * @return: null
     */
    Result getDepartmentUserMember(String departmentId);

    /**
     * @author: yuqn
     * @Date: 2025/3/14 0:36
     * @description:
     * 更改部门信息
     * @param: null
     * @return: null
     */
    Result updateDepartment(DepartmentUpdateVo departmentUpdateVo, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/19 21:30
     * @description:
     * 将用户移除部门
     * @param: null
     * @return: null
     */
    Result deleteDepartmentUserById(SocietyDepartmentUserRole societyDepartmentUserRole, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/22 21:47
     * @description:
     * 获取部门邀请码
     * @param: null
     * @return: null
     */
    Result getDepartmentCode(String departmentId);

    /**
     * @author: yuqn
     * @Date: 2025/3/22 23:56
     * @description:
     * 根据邀请码获取部门信息
     * @param: null
     * @return: null
     */
    Result getDepartmentDetailsByCode(String departmentCode, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/23 1:18
     * @description:
     * 加入社团
     * @param: null
     * @return: null
     */
    Result joinDepartment(String departmentId, String token);
}
