package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyBodyUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.Result;

/**
* @author yuqn
* @description 针对表【society_body_user_role】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SocietyBodyUserRoleService extends IService<SocietyBodyUserRole> {

    /**
     * @author: yuqn
     * @Date: 2025/3/15 3:27
     * @description:
     * 获取社团成员列表，用于编辑社团
     * @param: null
     * @return: null
     */
    Result getUserListById(String societyId);
}
