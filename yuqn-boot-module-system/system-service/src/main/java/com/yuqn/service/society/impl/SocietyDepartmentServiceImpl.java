package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.dao.society.*;
import com.yuqn.entity.society.*;
import com.yuqn.enums.DelFalgEnum;
import com.yuqn.service.society.*;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.*;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
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

    // 定义字符集，包括数字、大小写字母
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_CODE_LENGTH = 6;

    @Autowired
    private SocietyDepartmentMapper societyDepartmentMapper;

    @Autowired
    private SocietyBodyMapper societyBodyMapper;

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

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SocietyUserMapper  societyUserMapper;
    @Autowired
    private SocietyCollegeMapper societyCollegeMapper;
    @Autowired
    private SocietyGradeMapper societyGradeMapper;
    @Autowired
    private SocietyMajorMapper societyMajorMapper;
    @Autowired
    private SocietyClassesMapper societyClassesMapper;
    @Autowired
    private SocietyBodyUserRoleMapper societyBodyUserRoleMapper;

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
        queryWrapper.eq("society_body_id",societyId).eq("del_flag",DelFalgEnum.NOTDEL);
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
            String departmentViceMinister = sysRoleMapper.getDepartmentMinisterOrBiceMinister(id, "7");
            String departmentMinister = sysRoleMapper.getDepartmentMinisterOrBiceMinister(id, "6");
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
        queryWrapper.eq("department_id", departmentId).eq("del_flag", DelFalgEnum.NOTDEL);
        List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(queryWrapper);
        ArrayList<Object> resultObj = new ArrayList<>();
        for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id",societyDepartmentUserRole.getUserId());
            // 获取名字和学号
            QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",societyDepartmentUserRole.getUserId());
            SysUser one = sysUserService.getOne(queryWrapper1);
            System.out.println("one = " + one);
            String nickName = one.getNickName();
            String userName = one.getUserName();
            map.put("name",nickName);
            map.put("userName",userName);
            // 获取职位
            QueryWrapper<SysRole> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("id",societyDepartmentUserRole.getRoleId());
            SysRole one1 = sysRoleService.getOne(queryWrapper2);
            map.put("role",one1.getRemark());
            map.put("roleId",one1.getId());
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

    /**
     * @author: yuqn
     * @Date: 2025/3/22 22:26
     * @description:
     * 生成六位字符验证码
     * @param: null
     * @return: null
     */
    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder(RANDOM_CODE_LENGTH);
        for (int i = 0; i < RANDOM_CODE_LENGTH; i++) {
            // 从字符集中随机选择一个字符
            int index = random.nextInt(CHARACTERS.length());
            codeBuilder.append(CHARACTERS.charAt(index));
        }
        return codeBuilder.toString();
    }

    @Override
    @Transactional
    public Result addDepartment(AddDepartmentVo addDepartmentVo, String token) {
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
        System.out.println("token = " + token);
        System.out.println("userid = " + userid);
        // 插入department表
        List<SocietyDepartment> objects = new ArrayList<>();
        for (UserDepartmentRole societyDepartment : addDepartmentVo.getDepartmentValue()) {
            SocietyDepartment societyDepartment1 = new SocietyDepartment();
            societyDepartment1.setSocietyBodyId(addDepartmentVo.getSocietyId());
            societyDepartment1.setName(societyDepartment.getDepartmentName());
            societyDepartment1.setIntroduce(societyDepartment.getDepartmentIntroduce());
            societyDepartment1.setCreateBy(userid);
            // 生成社团邀请码
            String s = generateRandomCode();
            System.out.println("邀请码 = " + s);
            societyDepartment1.setDepartmentCode(s);
            int insert = societyDepartmentMapper.insert(societyDepartment1);
            // 获取插入数据的id
            QueryWrapper<SocietyDepartment> queryWrapper = new QueryWrapper();
            queryWrapper.eq("name",societyDepartment.getDepartmentName())
                    .eq("society_body_id",addDepartmentVo.getSocietyId())
                    .eq("introduce",societyDepartment.getDepartmentIntroduce());
            SocietyDepartment societyDepartment2 = societyDepartmentMapper.selectOne(queryWrapper);
            objects.add(societyDepartment2);
        }
        // 根据部门名字和所属社团id，获取添加部门的id，用于新增部门的部长和副部长
        for (SocietyDepartment object : objects){
            for(int i = 0; i< 3 ; i++){
                SocietyDepartmentUserRole societyDepartmentUserRole = new SocietyDepartmentUserRole();
                societyDepartmentUserRole.setDepartmentId(object.getId());
                societyDepartmentUserRole.setUserId(userid);
                societyDepartmentUserRole.setCreateBy(userid);
                if (i == 0){
                    // 添加一个部长两个副部长，初始为创建人
                    societyDepartmentUserRole.setRoleId("6");
                }else{
                    // 添加一个部长两个副部长，初始为创建人
                    societyDepartmentUserRole.setRoleId("7");
                }
                int insert = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole);
            }
        }
        return Result.ok("添加成功");
    }

    @Override
    @Transactional
    public Result deleteDepartment(AddDepartmentVo addDepartmentVo, String token) {
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
        Result result = null;
        try{
            UpdateWrapper<SocietyBody> societyBodyUpdateWrapper = new UpdateWrapper<>();
            societyBodyUpdateWrapper.eq("id",addDepartmentVo.getSocietyId());
            SocietyBody societyBody = new SocietyBody();
            societyBody.setDelFlag(DelFalgEnum.DEL);
            societyBody.setUpdateBy(userid);
            int update = societyBodyMapper.update(societyBody, societyBodyUpdateWrapper);
            List<SocietyBodyUserRole> societyBodyUserRoles = societyBodyUserRoleMapper.selectList(new QueryWrapper<SocietyBodyUserRole>().eq("society_body_id", addDepartmentVo.getSocietyId())
                    .eq("del_flag", DelFalgEnum.NOTDEL));
            for (SocietyBodyUserRole societyBodyUserRole : societyBodyUserRoles){
                societyBodyUserRole.setDelFlag(DelFalgEnum.DEL);
                societyBodyUserRole.setUpdateBy(userid);
                int update1 = societyBodyUserRoleMapper.updateById(societyBodyUserRole);
            }
            List<SocietyDepartment> societyDepartments = societyDepartmentMapper.selectList(new QueryWrapper<SocietyDepartment>().eq("society_body_id", addDepartmentVo.getSocietyId())
                    .eq("del_flag", DelFalgEnum.NOTDEL));
            for (SocietyDepartment societyDepartment : societyDepartments){
                societyDepartment.setDelFlag(DelFalgEnum.DEL);
                societyDepartment.setUpdateBy(userid);
                societyDepartmentMapper.updateById(societyDepartment);
                List<SocietyDepartmentUserRole> societyDepartmentUserRoles = societyDepartmentUserRoleMapper.selectList(new QueryWrapper<SocietyDepartmentUserRole>()
                        .eq("department_id", societyDepartment.getId())
                        .eq("del_flag", DelFalgEnum.NOTDEL));
                for (SocietyDepartmentUserRole societyDepartmentUserRole : societyDepartmentUserRoles){
                    societyDepartmentUserRole.setDelFlag(DelFalgEnum.DEL);
                    societyDepartmentUserRole.setUpdateBy(userid);
                    societyDepartmentUserRoleMapper.updateById(societyDepartmentUserRole);
                }
            }
            result = Result.ok("删除成功");
        }catch (Exception e){
            result = Result.ok("删除失败");
        }
        return result;
    }

    @Override
    public Result getDepartmentUserDetailsById(String societyId, String departmentId, String userId, String roleId) {
        // 构造返回对象
        HashMap<String, String> resObj = new HashMap<>();
        SysUser sysuser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", userId));
        resObj.put("userId",userId);
        resObj.put("userName",sysuser.getUserName());
        resObj.put("userNickName",sysuser.getNickName());
        resObj.put("phoneNumber",sysuser.getPhonenumber());
        SocietyBody societyBody = societyBodyMapper.selectOne(new QueryWrapper<SocietyBody>().eq("id", societyId));
        resObj.put("societyId",societyId);
        resObj.put("societyName",societyBody.getName());
        SocietyDepartment societyDepartment = societyDepartmentMapper.selectOne(new QueryWrapper<SocietyDepartment>().eq("id", departmentId));
        resObj.put("departmentId",departmentId);
        resObj.put("departmentName",societyDepartment.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = societyDepartment.getCreateTime().format(formatter);
        resObj.put("joinTime",formattedDateTime);
        SocietyUser societyUser = societyUserMapper.selectOne(new QueryWrapper<SocietyUser>().eq("sys_user_id", userId));
        SocietyCollege college = societyCollegeMapper.selectOne(new QueryWrapper<SocietyCollege>().eq("id", societyUser.getSocietyCollegeId()));
        resObj.put("userCollege",college.getMessage());
        SocietyGrade societyGrade = societyGradeMapper.selectOne(new QueryWrapper<SocietyGrade>().eq("id", societyUser.getSocietyGradeId()));
        resObj.put("userGrade",societyGrade.getValue());
        SocietyMajor societyMajor = societyMajorMapper.selectOne(new QueryWrapper<SocietyMajor>().eq("id", societyUser.getSocietyMajorId()));
        resObj.put("userMajor",societyMajor.getValue());
        SocietyClasses classes = societyClassesMapper.selectOne(new QueryWrapper<SocietyClasses>().eq("id", societyUser.getSocietyClassesId()));
        resObj.put("userClass",classes.getValue());
        return Result.ok(resObj);
    }

}




