package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.dao.society.*;
import com.yuqn.entity.society.*;
import com.yuqn.enums.DelFalgEnum;
import com.yuqn.service.society.SocietyBodyUserRoleService;
import com.yuqn.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author yuqn
* @description 针对表【society_body_user_role】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Service
public class SocietyBodyUserRoleServiceImpl extends ServiceImpl<SocietyBodyUserRoleMapper, SocietyBodyUserRole>
    implements SocietyBodyUserRoleService{

    @Autowired
    private SocietyBodyUserRoleMapper societyBodyUserRoleMapper;
    @Autowired
    private SocietyDepartmentMapper societyDepartmentMapper;
    @Autowired
    private SocietyDepartmentUserRoleMapper societyDepartmentUserRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SocietyUserMapper societyUserMapper;
    @Autowired
    private SocietyCollegeMapper societyCollegeMapper;
    @Autowired
    private SocietyGradeMapper societyGradeMapper;
    @Autowired
    private SocietyMajorMapper societyMajorMapper;
    @Override
    public Result getUserListById(String societyId) {
        Result result = null;
        // 获取非主席副主席的所有用户
        ArrayList<HashMap<String, Object>> objects = new ArrayList<>();
        // 获取所有部门
        List<SocietyDepartment> societyDepartments = societyDepartmentMapper.selectList(new QueryWrapper<SocietyDepartment>()
                .eq("del_flag", DelFalgEnum.NOTDEL)
                .eq("society_body_id", societyId));
        // 根据部门获取每个部门的用户列表
        for (SocietyDepartment societyDepartment : societyDepartments){
            List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>()
                    .eq("del_flag", DelFalgEnum.NOTDEL)
                    .eq("department_id", societyDepartment.getId()));
            for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles){
                HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("userId",societyDepartmentUserRole.getUserId());
                objects.add(objectObjectHashMap);
            }
        }
        // 去重
        Set<Object> set = new HashSet<>();
        List<HashMap<String, Object>> res = objects.stream()
                .filter(obj -> set.add((Object) obj.get("userId")))
                .collect(Collectors.toList());
        // 查库拿用户信息
        List<HashMap<String, Object>> userList = new ArrayList<>();
        for (HashMap<String, Object> re : res) {
            HashMap<String, Object> userMap = new HashMap<>();
            // 用户名，昵称
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("del_flag", DelFalgEnum.NOTDEL)
                    .eq("id", re.get("userId")));
            if (sysUser != null){
                userMap.put("id",re.get("userId"));
                userMap.put("userName",sysUser.getUserName());
                userMap.put("userNickName",sysUser.getNickName());
            }
            // 查询班级专业等
            SocietyUser societyUser = societyUserMapper.selectOne(new QueryWrapper<SocietyUser>().eq("sys_user_id", re.get("userId"))
                    .eq("del_flag", DelFalgEnum.NOTDEL));
            if (societyUser != null){
                SocietyCollege college = societyCollegeMapper.selectOne(new QueryWrapper<SocietyCollege>().eq("id", societyUser.getSocietyCollegeId())
                        .eq("del_flag", DelFalgEnum.NOTDEL));
                userMap.put("userCollege",college.getMessage());
                SocietyGrade societyGrade = societyGradeMapper.selectOne(new QueryWrapper<SocietyGrade>().eq("id", societyUser.getSocietyGradeId())
                        .eq("del_flag", DelFalgEnum.NOTDEL));
                userMap.put("userGrade",societyGrade.getValue());
                SocietyMajor societyMajor = societyMajorMapper.selectOne(new QueryWrapper<SocietyMajor>().eq("id", societyUser.getSocietyMajorId())
                        .eq("del_flag", DelFalgEnum.NOTDEL));
                userMap.put("userMajor",societyMajor.getValue());
            }
            userList.add(userMap);
        }
        result = Result.ok(userList);
        return result;
    }
}




