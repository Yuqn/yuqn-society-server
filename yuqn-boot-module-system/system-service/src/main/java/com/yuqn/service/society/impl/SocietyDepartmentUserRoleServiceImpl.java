package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.dao.society.*;
import com.yuqn.entity.society.*;
import com.yuqn.enums.DelFalgEnum;
import com.yuqn.service.society.SocietyDepartmentUserRoleService;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.DepartmentUpdateVo;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyUserListVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author yuqn
* @description 针对表【society_department_user_role】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Service
@Slf4j
public class SocietyDepartmentUserRoleServiceImpl extends ServiceImpl<SocietyDepartmentUserRoleMapper, SocietyDepartmentUserRole>
    implements SocietyDepartmentUserRoleService{

    @Autowired
    private SocietyBodyMapper societyBodyMapper;

    @Autowired
    private SocietyDepartmentMapper societyDepartmentMapper;

    @Autowired
    private SocietyDepartmentUserRoleMapper societyDepartmentUserRoleMapper;

    @Autowired
    private SocietyBodyUserRoleMapper societyBodyUserRoleMapper;

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
    public Result getDepartmentuserList(String societyId, String departmentId) {
        Result result = null;
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        // 只查找学生
        queryWrapper.eq("del_flag",DelFalgEnum.NOTDEL).eq("identity",0);
        List<SysUser> sysUsers = sysUserMapper.selectList(queryWrapper);
        List<SocietyUserListVo> societyUserListVoList = new ArrayList<>();
        for(SysUser sysUser:sysUsers){
            SocietyUserListVo societyUserListVo = new SocietyUserListVo();
            societyUserListVo.setUserId(sysUser.getId());
            societyUserListVo.setUserName(sysUser.getUserName());
            societyUserListVo.setNickName(sysUser.getNickName());
            // 查询年级专业班级
            SocietyUser societyUserOne = societyUserMapper.selectOne(new QueryWrapper<SocietyUser>().eq("sys_user_id", sysUser.getId()));
            if (societyUserOne != null){
                SocietyCollege college = societyCollegeMapper.selectOne(new QueryWrapper<SocietyCollege>().eq("id", societyUserOne.getSocietyCollegeId()));
                societyUserListVo.setUserCollege(college.getMessage());
                SocietyGrade societyGrade = societyGradeMapper.selectOne(new QueryWrapper<SocietyGrade>().eq("id", societyUserOne.getSocietyGradeId()));
                societyUserListVo.setUserGrade(societyGrade.getValue());
                SocietyMajor societyMajor = societyMajorMapper.selectOne(new QueryWrapper<SocietyMajor>().eq("id", societyUserOne.getSocietyMajorId()));
                societyUserListVo.setUserMajor(societyMajor.getValue());
            }
            // 判断用户是否加入社团或者部门
            List<SocietyDepartmentUserRole> user_id1 = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>()
                    .eq("user_id", sysUser.getId())
                    .eq("department_id", departmentId)
                    .eq("del_flag",DelFalgEnum.NOTDEL));
            if (user_id1.size() > 0){
                societyUserListVo.setIsJoin("1");
            }else{
                societyUserListVo.setIsJoin("0");
            }
            societyUserListVoList.add(societyUserListVo);
        }
        result = Result.ok(societyUserListVoList);
        return result;
    }

    @Override
    @Transactional
    public Result addDepartmentUser(String societyId, String departmentId, String userId,String token) {
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
        SocietyDepartmentUserRole societyDepartmentUserRole = new SocietyDepartmentUserRole();
        societyDepartmentUserRole.setDepartmentId(departmentId);
        societyDepartmentUserRole.setUserId(userId);
        // 新增用户默认干事
        societyDepartmentUserRole.setRoleId("8");
        societyDepartmentUserRole.setCreateBy(userid);
        int insert = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole);
        if (insert > 0){
            result = Result.ok("添加成功");
        }else{
            result = Result.fail("添加失败");
        }
        return result;
    }

    @Override
    @Transactional
    public Result deleteDepartment(String departmentId, String token) {
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
        // 删除部门
        SocietyDepartment societyDepartment = new SocietyDepartment();
        societyDepartment.setDelFlag(DelFalgEnum.DEL);
        societyDepartment.setUpdateBy(userid);
        int id = societyDepartmentMapper.update(societyDepartment, new QueryWrapper<SocietyDepartment>().eq("id", departmentId));
        // 删除用户
        List<SocietyDepartmentUserRole> department_id = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>().eq("department_id", departmentId));
        for (SocietyDepartmentUserRole societyDepartmentUserRole1 : department_id){
            SocietyDepartmentUserRole societyDepartmentUserRole = new SocietyDepartmentUserRole();
            societyDepartmentUserRole.setDelFlag(DelFalgEnum.DEL);
            societyDepartmentUserRole.setUpdateBy(userid);
            societyDepartmentUserRoleMapper.update(societyDepartmentUserRole, new QueryWrapper<SocietyDepartmentUserRole>().eq("id", societyDepartmentUserRole1.getId()));
        }
        result=Result.ok("删除成功");
        return result;
    }

    @Override
    public Result getDepartmentDetailsById(String departmentId) {
        Result result = null;
        SocietyDepartment societyDepartment = societyDepartmentMapper.selectOne(new QueryWrapper<SocietyDepartment>().eq("id", departmentId));
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("departmentId",societyDepartment.getId());
        objectObjectHashMap.put("departmentName",societyDepartment.getName());
        objectObjectHashMap.put("departmentIntroduce",societyDepartment.getIntroduce());
        System.out.println("-------------------------- = " + societyDepartment);
        SysUser id = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartment.getCreateBy()));
        System.out.println("id ----------------------------= " + id);
        objectObjectHashMap.put("createName",id.getNickName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 使用formatter将LocalDateTime转换为字符串
        String formattedDateTime = societyDepartment.getCreateTime().format(formatter);
        objectObjectHashMap.put("createTime",formattedDateTime);
        // 拿到部长信息
        SocietyDepartmentUserRole societyDepartmentUserRole1 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                .eq("department_id", departmentId)
                .eq("role_id", "6")
                .eq("del_flag",DelFalgEnum.NOTDEL));
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartmentUserRole1.getUserId()));
        HashMap<Object, Object> minister = new HashMap<>();
        minister.put("userId",societyDepartmentUserRole1.getUserId());
        minister.put("nickName",sysUser.getNickName());
        minister.put("roleId",societyDepartmentUserRole1.getRoleId());
        objectObjectHashMap.put("minister",minister);

        // 拿到副部长信息
        List<SocietyDepartmentUserRole> societyDepartmentUserRoleList = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>()
                .eq("department_id", departmentId)
                .eq("del_flag",DelFalgEnum.NOTDEL)
                .eq("role_id", "7"));
        ArrayList<Object> viceMinister = new ArrayList<>();
        for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoleList){
            HashMap<Object, Object> viceMinisterMap = new HashMap<>();
            viceMinisterMap.put("userId",societyDepartmentUserRole.getUserId());
            viceMinisterMap.put("nickName",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartmentUserRole.getUserId())).getNickName());
            viceMinisterMap.put("roleId",societyDepartmentUserRole.getRoleId());
            viceMinister.add(viceMinisterMap);
        }
        objectObjectHashMap.put("viceMinister",viceMinister);
        result = Result.ok(objectObjectHashMap);
        return result;
    }

    @Override
    public Result getDepartmentUserMember(String departmentId) {
        Result result = null;
        QueryWrapper<SocietyDepartmentUserRole> queryWrapper = new QueryWrapper();
        queryWrapper.eq("department_id",departmentId).eq("del_flag",DelFalgEnum.NOTDEL)
                .eq("role_id","8");
        List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(queryWrapper);
        ArrayList<Object> objects = new ArrayList<>();
        for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles){
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId",societyDepartmentUserRole.getUserId());
            map.put("roleId",societyDepartmentUserRole.getRoleId());
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartmentUserRole.getUserId()));
            map.put("nickName",sysUser.getNickName());
            map.put("userName",sysUser.getUserName());
            SocietyUser societyUser = societyUserMapper.selectOne(new QueryWrapper<SocietyUser>().eq("sys_user_id", societyDepartmentUserRole.getUserId()));
            SocietyCollege college = societyCollegeMapper.selectOne(new QueryWrapper<SocietyCollege>().eq("id", societyUser.getSocietyCollegeId()));
            map.put("userCollege",college.getMessage());
            SocietyGrade societyGrade = societyGradeMapper.selectOne(new QueryWrapper<SocietyGrade>().eq("id", societyUser.getSocietyGradeId()));
            map.put("userGrade",societyGrade.getValue());
            SocietyMajor societyMajor = societyMajorMapper.selectOne(new QueryWrapper<SocietyMajor>().eq("id", societyUser.getSocietyMajorId()));
            map.put("userMajor",societyMajor.getValue());
            objects.add(map);
        }
        result = Result.ok(objects);
        return result;
    }

    @Override
    @Transactional
    public Result updateDepartment(DepartmentUpdateVo departmentUpdateVo, String token) {
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
        // 更改名字简介
        SocietyDepartment societyDepartment = new SocietyDepartment();
        societyDepartment.setName(departmentUpdateVo.getSocietyDepartment().getName());
        societyDepartment.setIntroduce(departmentUpdateVo.getSocietyDepartment().getIntroduce());
        societyDepartment.setUpdateBy(userid);
        int upCount = societyDepartmentMapper.update(societyDepartment, new UpdateWrapper<SocietyDepartment>()
                .eq("id", departmentUpdateVo.getSocietyDepartment().getId()));
        if (upCount == 1){
            // 更改原部长信息
            SocietyDepartmentUserRole societyDepartmentUserRole = societyDepartmentUserRoleMapper.selectOne(
                    new QueryWrapper<SocietyDepartmentUserRole>()
                            .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                            .eq("role_id", "6")
                            .eq("del_flag", DelFalgEnum.NOTDEL));
            SocietyDepartmentUserRole societyDepartmentUserRole4 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                    .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                    .eq("role_id", "8").eq("user_id", societyDepartmentUserRole.getUserId())
                    .eq("del_flag", DelFalgEnum.NOTDEL));
            if (societyDepartmentUserRole4 != null){
                societyDepartmentUserRole.setDelFlag(DelFalgEnum.DEL);
                societyDepartmentUserRole.setUpdateBy(userid);
                int i = societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole);
            }else{
                // 更改为干事
                societyDepartmentUserRole.setRoleId("8");
                societyDepartmentUserRole.setUpdateBy(userid);
                int i = societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole);
            }
            // 更改原副部长信息
            List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>()
                    .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                    .eq("role_id", "7").eq("del_flag", DelFalgEnum.NOTDEL));
            for (SocietyDepartmentUserRole departmentUserRole : societyDepartmentUserRoles) {
                SocietyDepartmentUserRole societyDepartmentUserRole3 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                        .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                        .eq("role_id", "8").eq("del_flag",DelFalgEnum.NOTDEL).eq("user_id", departmentUserRole.getUserId()));
                if (societyDepartmentUserRole3 != null){
                    departmentUserRole.setDelFlag(DelFalgEnum.DEL);
                    departmentUserRole.setUpdateBy(userid);
                    int i1 = societyDepartmentUserRoleMapper.updateById(departmentUserRole);
                }else{
                    // 更改为干事
                    departmentUserRole.setRoleId("8");
                    departmentUserRole.setUpdateBy(userid);
                    int i2 = societyDepartmentUserRoleMapper.updateById(departmentUserRole);
                }
            }
            // 设置新部长
            SocietyDepartmentUserRole societyDepartmentUserRole2 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                    .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                    .eq("role_id", "8")
                    .eq("del_flag",DelFalgEnum.NOTDEL)
                    .eq("user_id", departmentUpdateVo.getMinister().getUserId()));
            if(societyDepartmentUserRole2!=null){
                // 更改为部长
                societyDepartmentUserRole2.setRoleId("6");
                societyDepartmentUserRole2.setUpdateBy(userid);
                int i1 = societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole2);
            }else{
                SocietyDepartmentUserRole societyDepartmentUserRole1 = new SocietyDepartmentUserRole();
                societyDepartmentUserRole1.setDepartmentId(departmentUpdateVo.getSocietyDepartment().getId());
                societyDepartmentUserRole1.setUserId(departmentUpdateVo.getMinister().getUserId());
                societyDepartmentUserRole1.setCreateBy(userid);
                societyDepartmentUserRole1.setRoleId("6");
                societyDepartmentUserRole1.setDelFlag(DelFalgEnum.NOTDEL);
                int insert = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole1);
            }
            // 设置新副部长
            for (SysUserRole viceMinister : departmentUpdateVo.getViceMinister()){
                // 设置新部长
                SocietyDepartmentUserRole societyDepartmentUserRole1 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                        .eq("department_id", departmentUpdateVo.getSocietyDepartment().getId())
                        .eq("role_id", "8")
                        .eq("del_flag",DelFalgEnum.NOTDEL)
                        .eq("user_id", viceMinister.getUserId()));
                if (societyDepartmentUserRole1 != null){
                    // 更改为副部长
                    societyDepartmentUserRole1.setRoleId("7");
                    societyDepartmentUserRole1.setUpdateBy(userid);
                    int i2 = societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole1);
                }else{
                    // 如果没有这个人，直接插入新数据
                    SocietyDepartmentUserRole societyDepartmentUserRole3 = new SocietyDepartmentUserRole();
                    societyDepartmentUserRole3.setDepartmentId(departmentUpdateVo.getSocietyDepartment().getId());
                    societyDepartmentUserRole3.setUserId(viceMinister.getUserId());
                    societyDepartmentUserRole3.setCreateBy(userid);
                    societyDepartmentUserRole3.setRoleId(viceMinister.getRoleId());
                    societyDepartmentUserRole3.setDelFlag(DelFalgEnum.NOTDEL);
                    int insert = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole3);
                }
            }
            result = Result.ok("修改成功");
        }
        return result;
    }

    @Override
    @Transactional
    public Result deleteDepartmentUserById(SocietyDepartmentUserRole societyDepartmentUserRole, String token) {
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
        // 逻辑删除，查找一个普通用户，普通用户只存在一条记录
        SocietyDepartmentUserRole societyDepartmentUserRole1 = societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                .eq("department_id", societyDepartmentUserRole.getDepartmentId())
                .eq("user_id", societyDepartmentUserRole.getUserId())
                .eq("role_id", "8")
                .eq("del_flag", DelFalgEnum.NOTDEL));
        if (societyDepartmentUserRole1 != null){
            societyDepartmentUserRole1.setDelFlag(DelFalgEnum.DEL);
            societyDepartmentUserRole1.setUpdateBy(userid);
            // 更改数据库表
            int i = societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole1);
            if (i>0){
                result = Result.ok("删除成功");
            }else{
                result = Result.fail("删除失败");
            }
        }
        return result;
    }

    @Override
    public Result getDepartmentCode(String departmentId) {
        Result result = null;
        SocietyDepartment societyDepartment = societyDepartmentMapper.selectById(departmentId);
        result = Result.ok(societyDepartment.getDepartmentCode());
        return result;
    }

    @Override
    public Result getDepartmentDetailsByCode(String departmentCode, String token) {
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
        // 获取部门
        SocietyDepartment societyDepartment = societyDepartmentMapper.selectOne(new QueryWrapper<SocietyDepartment>().eq("department_code", departmentCode));
        if (societyDepartment != null){
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("departmentId",societyDepartment.getId());
            objectObjectHashMap.put("departmentName",societyDepartment.getName());
            objectObjectHashMap.put("introduce",societyDepartment.getIntroduce());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 使用formatter将LocalDateTime转换为字符串
            String formattedDateTime = societyDepartment.getCreateTime().format(formatter);
            objectObjectHashMap.put("createTime",formattedDateTime);
            objectObjectHashMap.put("createBy",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartment.getCreateBy())).getNickName());
            // 获取部长
            String minister = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                    .eq("id", societyDepartmentUserRoleMapper.selectOne(new QueryWrapper<SocietyDepartmentUserRole>()
                            .eq("department_id", societyDepartment.getId())
                    .eq("role_id", "6")).getUserId())).getNickName();
            objectObjectHashMap.put("minister",minister);
            // 获取副部长
            List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>().eq("department_id", societyDepartment.getId())
                    .eq("role_id", "7"));
            String viceMinister = "";
            for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles){
                SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyDepartmentUserRole.getUserId()));
                if ("".equals(viceMinister)){
                    viceMinister = sysUser.getNickName();
                }else{
                    viceMinister = viceMinister + "-" + sysUser.getNickName();
                }
            }
            objectObjectHashMap.put("viceMinister",viceMinister);
            // 判断是否在该部门，1是，0否
            List<SocietyDepartmentUserRole> societyDepartmentUserRoles1 = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>().eq("user_id", userid)
                    .eq("department_id", societyDepartment.getId()));
            if (societyDepartmentUserRoles1.size()>0){
                objectObjectHashMap.put("isJoin",1);
            }else{
                objectObjectHashMap.put("isJoin",0);
            }
            // 查看人数
            List<SocietyDepartmentUserRole> department_id = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>().eq("department_id", societyDepartment.getId()));
            int size = department_id.stream().map(SocietyDepartmentUserRole::getUserId).collect(Collectors.toSet()).size();
            objectObjectHashMap.put("count",size);
            // 所属社团
            String societyName = societyBodyMapper.selectOne(new QueryWrapper<SocietyBody>().eq("id", societyDepartment.getSocietyBodyId())).getName();
            objectObjectHashMap.put("societyName",societyName);
            result = Result.ok(objectObjectHashMap);
        }
        return result;
    }

    @Override
    @Transactional
    public Result joinDepartment(String departmentId, String token) {
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
        // 判断是否已经在社团
        Long aLong = societyDepartmentUserRoleMapper.selectCount(new QueryWrapper<SocietyDepartmentUserRole>().eq("user_id", userid).eq("department_id", departmentId));
        if (aLong>0){
            result = Result.fail("你已经加入过该社团");
        }else {
            SocietyDepartmentUserRole societyDepartmentUserRole = new SocietyDepartmentUserRole();
            societyDepartmentUserRole.setDepartmentId(departmentId);
            societyDepartmentUserRole.setUserId(userid);
            societyDepartmentUserRole.setCreateBy(userid);
            societyDepartmentUserRole.setRoleId("8");
            societyDepartmentUserRole.setDelFlag(DelFalgEnum.NOTDEL);
            int insert = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole);
            if (insert>0){
                result = Result.ok("加入成功");
            }else {
                result = Result.fail("加入失败");
            }
        }
        return result;
    }
}




