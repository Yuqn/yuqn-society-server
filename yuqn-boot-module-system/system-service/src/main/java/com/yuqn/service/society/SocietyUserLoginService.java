package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyGrade;
import com.yuqn.vo.CollegeMajorClass;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyUserVo;
import com.yuqn.vo.UserLoginVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yuqn
 * @Date: 2024/11/20 8:58
 * @description:
 * 用户登录接口
 * @version: 1.0
 */
public interface SocietyUserLoginService {
    /**
     * @author: yuqn
     * @Date: 2024/11/20 19:32
     * @description:
     * 获取年级信息
     * @param: null
     * @return: null
     */
    List<CollegeMajorClass> getDataTree();

    /**
     * @author: yuqn
     * @Date: 2024/11/20 23:22
     * @description:
     * 获取年级数据
     * @param: null
     * @return: null
     */
    List<CollegeMajorClass> getDataGrade();

    /**
     * @author: yuqn
     * @Date: 2024/11/21 11:40
     * @description:
     * 用户注册功能
     * @param: SocietyUserVo
     * @return: 0：失败，1：成功
     */
    int registerUser(SocietyUserVo societyUserVo);

    /**
     * @author: yuqn
     * @Date: 2024/11/23 22:55
     * @description:
     * 用户登录
     * @param: null
     * @return: null
     */
    Result userLogin(UserLoginVo userLoginVo);

    /**
     * @author: yuqn
     * @Date: 2024/11/23 22:55
     * @description:
     * 教师登录
     * @param: null
     * @return: null
     */
    Result teacherLogin(UserLoginVo userLoginVo);

    /**
     * @author: yuqn
     * @Date: 2024/11/23 22:55
     * @description:
     * 判断是否有该号码的用户
     * @param: null
     * @return: null
     */
    Result isRegister(String phonenumber);

    /**
     * @author: yuqn
     * @Date: 2025/2/28 2:06
     * @description:
     * 更改密码
     * @param: null
     * @return: null
     */
    Result changePassword(String phonenumber,String newPassword);

    /**
     * @author: yuqn
     * @Date: 2025/2/28 22:38
     * @description:
     * 获取token
     * @param: null
     * @return: null
     */
    Result getRole(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/20 2:09
     * @description:
     * 退出登录
     * @param: null
     * @return: null
     */
    Result logout(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/22 0:19
     * @description:
     * 获取用户信息
     * @param: null
     * @return: null
     */
    Result getUserDetail(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/22 15:58
     * @description:
     * 更改用户信息
     * @param: null
     * @return: null
     */
    Result updateUser(String token, SocietyUserVo societyUserVo);
}
