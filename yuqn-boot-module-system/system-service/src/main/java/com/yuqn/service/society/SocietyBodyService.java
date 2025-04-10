package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyBody;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyMkdirVo;
import com.yuqn.vo.SocietyUpdateVo;

/**
* @author yuqn
* @description 针对表【society_body】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SocietyBodyService extends IService<SocietyBody> {
    /**
     * @author: yuqn
     * @Date: 2025/3/5 20:20
     * @description:
     * 获取社团列表
     * @param: null
     * @return: null
     */
    public Result getSocietyList(String societyName);

    /**
     * @author: yuqn
     * @Date: 2025/3/5 20:20
     * @description:
     * 教师身份创建社团
     * @param: null
     * @return: null
     */
    Result mkdirSocietyTeacher(SocietyMkdirVo societyMkdirVo,String token);
    /**
     * @author: yuqn
     * @Date: 2025/3/5 20:20
     * @description:
     * 非教师身份创建社团
     * @param: null
     * @return: null
     */
    Result mkdirSocietyStudent(SocietyMkdirVo societyMkdirVo, String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:57
     * @description:
     * 获取社团成员数
     * @param: null
     * @return: null
     */
    Result getSocietyUserList(String societyId);

    /**
     * @author: yuqn
     * @Date: 2025/3/6 21:38
     * @description:
     * 根据社团id获取社团详情
     * @param: null
     * @return: null
     */
    Result getSocietyDetails(String societyId);

    /**
     * @author: yuqn
     * @Date: 2025/3/7 2:35
     * @description:
     * 编辑社团
     * @param: null
     * @return: null
     */
    Result societyUpdate(SocietyUpdateVo societyUpdateVo);

    /**
     * @author: yuqn
     * @Date: 2025/3/7 2:35
     * @description:
     * 编辑社团
     * @param: null
     * @return: null
     */
    Result societyUpdate(SocietyUpdateVo societyUpdateVo,String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/7 17:25
     * @description:
     * 根据id获取社团详细信息
     * @param: null
     * @return: null
     */
    Result getSocietyDetailsById(String societyId);
}
