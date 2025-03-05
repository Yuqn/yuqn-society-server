package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.dao.society.SocietyBodyMapper;
import com.yuqn.dao.society.SocietyDepartmentUserRoleMapper;
import com.yuqn.dao.society.SysRoleMapper;
import com.yuqn.entity.society.*;
import com.yuqn.service.society.*;
import com.yuqn.dao.society.SocietyDepartmentMapper;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.DepartmentVo;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyListVo;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author yuqn
* @description 针对表【society_department】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Service
@Slf4j
public class SocietyDepartmentServiceImpl extends ServiceImpl<SocietyDepartmentMapper, SocietyDepartment>
    implements SocietyDepartmentService{

    @Autowired
    private SocietyDepartmentMapper societyDepartmentMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SocietyBodyService societyBodyService;

    @Autowired
    private SocietyDepartmentUserRoleMapper societyDepartmentUserRoleMapper;

    @Autowired
    private SocietyDepartmentUserRoleService societyDepartmentUserRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SocietyCollegeService societyCollegeService;

    @Autowired
    private SocietyUserService societyUserService;

    @Override
    public Result getDepartmentById(String societyId,String token) {
        Result result = null;
        // 解析当前用户id
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            log.info("测试获取用户id，为：{}",userid);
            System.out.println("userid = " + userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 获取部门信息
        QueryWrapper<SocietyDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("society_body_id",societyId);
        List<SocietyDepartment> societyDepartments = societyDepartmentMapper.selectList(queryWrapper);
        List<DepartmentVo> resultObj = new ArrayList<>();
        // 获取社团名字
        String byId = societyBodyService.getById(societyId).getName();
        // 获取部长副部长信息
        for (SocietyDepartment societyDepartment : societyDepartments) {
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setId(societyDepartment.getId());
            departmentVo.setName(societyDepartment.getName());
            departmentVo.setCreateTime(societyDepartment.getCreateTime().toString());
            departmentVo.setIntroduce(societyDepartment.getIntroduce());
            String id = societyDepartment.getId();
            QueryWrapper<SocietyDepartment> queryWrapper1 = new QueryWrapper<>();
            String departmentViceMinister = sysRoleMapper.getDepartmentMinisterOrBiceMinister(id, "6");
            String departmentMinister = sysRoleMapper.getDepartmentMinisterOrBiceMinister(id, "5");
            departmentVo.setMinister(departmentMinister);
            departmentVo.setViceMinister(departmentViceMinister);
            int departmentUserCount = societyDepartmentMapper.getDepartmentUserCount(id);
            departmentVo.setCount(departmentUserCount);
            departmentVo.setSocietyName(byId);
            // 查找用户是否在该部门中
            QueryWrapper<SocietyDepartmentUserRole> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("user_id", userid).eq("department_id", societyDepartment.getId());
            int count = (int) societyDepartmentUserRoleService.count(queryWrapper2);
            if (count > 0){
                departmentVo.setIsJoin(1);
            }else{
                departmentVo.setIsJoin(0);
            }
            System.out.println("count = " + count);
            resultObj.add(departmentVo);
        }
        result = Result.ok(resultObj);
        return result;
    }

    @Override
    public Result getDepartmentUserById(String departmentId) {
        Result resutl = null;
        // 获取用户列表和加入时间
        QueryWrapper<SocietyDepartmentUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(queryWrapper);
        ArrayList<Object> resultObj = new ArrayList<>();
        for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id",societyDepartmentUserRole.getUserId());
            // 获取名字和学号
            QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",societyDepartmentUserRole.getUserId());
            SysUser one = sysUserService.getOne(queryWrapper1);
            String nickName = one.getNickName();
            String userName = one.getUserName();
            map.put("name",nickName);
            map.put("userName",userName);
            // 获取职位
            QueryWrapper<SysRole> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("id",societyDepartmentUserRole.getRoleId());
            SysRole one1 = sysRoleService.getOne(queryWrapper2);
            map.put("role",one1.getRemark());
            // 获取学院
            QueryWrapper<SocietyUser> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("sys_user_id",societyDepartmentUserRole.getUserId());
            SocietyUser one2 = societyUserService.getOne(queryWrapper3);
            QueryWrapper<SocietyCollege> queryWrapper4 = new QueryWrapper<>();
            if (one2 == null){
                System.out.println("未查到用户身份信息");
            }
            queryWrapper4.eq("id",one2.getSocietyCollegeId());
            SocietyCollege one3 = societyCollegeService.getOne(queryWrapper4);
            map.put("college",one3.getMessage());
            resultObj.add(map);
        }
        resutl = Result.ok(resultObj);
        return resutl;
    }
}




