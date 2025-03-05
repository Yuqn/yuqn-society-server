package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yuqn.dao.society.SocietyUserLoginMapper;
import com.yuqn.dao.society.SocietyUserMapper;
import com.yuqn.dao.society.SysUserMapper;
import com.yuqn.entity.User;
import com.yuqn.entity.society.*;
import com.yuqn.service.LoginService;
import com.yuqn.service.society.*;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Slf4j
@Service
public class SocietyUserLoginServiceImpl implements SocietyUserLoginService {

    @Autowired
    private SocietyClassesService societyClassesService;
    @Autowired
    private SocietyCollegeService societyCollegeService;
    @Autowired
    private SocietyGradeService societyGradeService;
    @Autowired
    private SocietyMajorService societyMajorService;
    @Autowired
    private SocietyUserMapper societyUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SocietyBodyUserRoleService societyBodyUserRoleService;
    @Autowired
    private SocietyDepartmentService societyDepartmentService;
    @Autowired
    private SocietyUserLoginMapper societyUserLoginMapper;

    @Override
    public List<CollegeMajorClass> getDataTree() {
        List<CollegeMajorClass> collegeMajorClassList = new ArrayList<>();
        List<SocietyCollege> collegeList = societyCollegeService.list();
        List<SocietyMajor> majorList = societyMajorService.list();
        List<SocietyClasses> classesList = societyClassesService.list();

        for (SocietyCollege college:collegeList){
            List<CollegeMajorClass> collegeMajorClassList_major = new ArrayList<>();
            for (SocietyMajor major:majorList){
                List<CollegeMajorClass> collegeMajorClassList_classes = new ArrayList<>();
                if (major.getSocietyCollegeId().equals(college.getId())){
                    for (SocietyClasses classes:classesList){
                        if (classes.getSocietyMajorId().equals(major.getId())){
                            CollegeMajorClass collegeMajorClassClasses = new CollegeMajorClass();
                            collegeMajorClassClasses.setText(classes.getValue());
                            collegeMajorClassClasses.setValue(classes.getId());
                            collegeMajorClassList_classes.add(collegeMajorClassClasses);
                        }
                    }
                    CollegeMajorClass collegeMajorClassMagor = new CollegeMajorClass();
                    collegeMajorClassMagor.setText(major.getValue());
                    collegeMajorClassMagor.setValue(major.getId());
                    collegeMajorClassMagor.setChildren(collegeMajorClassList_classes);
                    collegeMajorClassList_major.add(collegeMajorClassMagor);
                }
            }
            CollegeMajorClass collegeMajorClass = new CollegeMajorClass();
            collegeMajorClass.setText(college.getMessage());
            collegeMajorClass.setValue(college.getId());
            collegeMajorClass.setChildren(collegeMajorClassList_major);
            collegeMajorClassList.add(collegeMajorClass);
        }

        System.out.println("collegeMajorClassList" + collegeMajorClassList);
        return collegeMajorClassList;
    }

    @Override
    public List<CollegeMajorClass> getDataGrade() {
        List<SocietyGrade> societyGradesList =  societyGradeService.list();
        List<CollegeMajorClass> collegeMajorClassList = new ArrayList<>();
        for (SocietyGrade societyGrade:societyGradesList){
            CollegeMajorClass collegeMajorClass = new CollegeMajorClass();
            collegeMajorClass.setValue(societyGrade.getId());
            collegeMajorClass.setText(societyGrade.getValue());
            collegeMajorClassList.add(collegeMajorClass);
        }
        return collegeMajorClassList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int registerUser(SocietyUserVo societyUserVo) {
        // 将参数封装到SysUser
        SysUser sysUser = new SysUser();
        sysUser.setUserName(societyUserVo.getStudentId());
        sysUser.setEmail(societyUserVo.getEmail());
        sysUser.setIdentity(societyUserVo.getIdentity());
        sysUser.setNickName(societyUserVo.getNickName());
        // 加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(societyUserVo.getPassword());
        sysUser.setPassword(password);
        sysUser.setSex(societyUserVo.getSex());
        sysUser.setPhonenumber(societyUserVo.getPhonenumber());
        sysUser.setRoles("ROLE_ACTIVITI_USER");
        try {
            // 尝试插入到sysUser表
            int insert = sysUserMapper.insert(sysUser);
            if (insert == 1){
                log.info("插入数据，数据内容为:{}",sysUser);
                // sysUser表插入成功后，查询该条数据
                LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysUser::getUserName,societyUserVo.getStudentId()).eq(SysUser::getNickName,societyUserVo.getNickName());
                SysUser sysUserOne = sysUserMapper.selectOne(queryWrapper);
                // 有该数据，则进一步插入到societyUser表
                if (!Objects.isNull(sysUserOne)){
                    // 插入成功后，插入society表
                    SocietyUser societyUser = new SocietyUser();
                    societyUser.setSysUserId(sysUserOne.getId());
                    societyUser.setCard(societyUserVo.getCard());
                    societyUser.setSocietyCollegeId(societyUserVo.getSocietyCollegeId());
                    societyUser.setSocietyMajorId(societyUserVo.getSocietyMajorId());
                    societyUser.setSocietyClassesId(societyUserVo.getSocietyClassesId());
                    societyUser.setSocietyGradeId(societyUserVo.getSocietyGradeId());
                    log.info("准备插入表society-user，表数据为：{}",societyUser);
                    int addSocietyUser = societyUserMapper.insert(societyUser);
                    if (addSocietyUser == 1){
                        log.info("插入表society-user成功");
                        return 1;
                    }else{
                        log.warn("尝试插入数据到表society-user，但是错误了，{}",societyUser);
                    }
                }else{
                    log.warn("已填写信息到sys-user表，但是读取不到，插入内容为:{}",sysUser);
                }
            }else{
                log.warn("尝试插入数据到表sys-user，但是没成功，" + sysUser);
            }
        }catch (Exception e){
            log.error("插入数据失败，报错信息" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 0;
    }

    @Override
    public Result userLogin(UserLoginVo userLoginVo){
        System.out.println("userLoginVo = " + userLoginVo);
        Result result = null;
        if ("0".equals(userLoginVo.getLoginType())) {
            User user = new User();
            // 将手机号码当用户名，通过拼接，赋给username{ "userName":"phone:136900873"}，拼接为了自定义认证器能够区分登录类型
            user.setUserName("phone:" + userLoginVo.getPhonenumber());
            result = loginService.login(user);
            System.out.println("result = " + result);
            log.info("用户登录成功，用户信息为：{}",user);
        } else if ("1".equals(userLoginVo.getLoginType())) {
            User user = new User();
            user.setUserName("username:" + userLoginVo.getUserName());
            user.setPassword(userLoginVo.getPassword());
            result = loginService.login(user);
            System.out.println("result = " + result);
            log.info("用户登录成功，用户信息为：{}",user);
        } else {
            result = Result.error(500, "登录类型错误");
        }
        return result;
    }

    @Override
    public Result teacherLogin(UserLoginVo userLoginVo) {
        Result result = null;
        User user = new User();
        user.setUserName("username:" + userLoginVo.getUserName());
        user.setPassword(userLoginVo.getPassword());
        // 提前查库，用于确定是否有这个教师身份的用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getIdentity,1);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        // 如果有这个身份，则开始进行登录验证，这个过程为了兼容安全框架，后期需要改
        if (Objects.isNull(sysUser)){
            return Result.error(500,"没有此教师身份的用户");
        }
        result = loginService.login(user);
        System.out.println("result = " + result);
        log.info("用户登录成功，用户信息为：{}",user);
        return result;
    }

    @Override
    public Result isRegister(String phonenumber) {
        Result result = null;
        // 提前查库，用于确定是否有这个教师身份的用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhonenumber,phonenumber);
        SysUser sysUser = null;
        try{
            sysUser = sysUserMapper.selectOne(queryWrapper);
            if(Objects.isNull(sysUser)){
                result = Result.ok("可以注册");
            }else{
                result = Result.fail("该用户已注册");
            }
        }catch (Exception e){
            log.error("查询用户信息，报错了{}",e.getMessage());
        }finally {
            return result;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2025/2/28 2:06
     * @description:
     * 更改密码
     * @param: null
     * @return: null
     */
    @Override
    public Result changePassword(String phonenumber,String password) {
        Result result = null;
        // 加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = encoder.encode(password);
        // 构造条件
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("phonenumber", phonenumber).set("password", newPassword);
        // 更改
        try{
            boolean update = sysUserService.update(updateWrapper);
            if(update){
                log.info("用户更改信息成功，手机号码为：{},更改密码为{}",phonenumber,password);
                result =  Result.ok("更改密码成功");
            }else{
                log.info("用户更改信息失败，请查看日志");
                result =  Result.fail("更改密码失败");
            }
        }catch (Exception e){
            log.error("查询用户信息，报错了{}",e.getMessage());
        }finally {
            return result;
        }
    }

    @Override
    public Result getRole(String token) {
        Result result = null;
        // 解析token获取id
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
        // 获取用户角色
        List<UserBodyRole> societyRole = societyUserLoginMapper.getSocietyRole("1895163679985389570");
        List<UserBodyRole> departmentRole = societyUserLoginMapper.getDepartmentRole("1895163679985389570");
        // 构建返回数据

        HashMap<String, Object> map = new HashMap<>();
        map.put("societyRole",societyRole);
        map.put("departmentRole",departmentRole);
        // 构造学生身份
        HashMap<String,Object> otherRole = new HashMap<>();
        otherRole.put("roleId","9");
        otherRole.put("roleKey","6");
        otherRole.put("roleName","student");
        otherRole.put("userId",userid);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(otherRole);
        map.put("otherRole",objects);
        result = Result.ok(map);
        return result;
    }
}
