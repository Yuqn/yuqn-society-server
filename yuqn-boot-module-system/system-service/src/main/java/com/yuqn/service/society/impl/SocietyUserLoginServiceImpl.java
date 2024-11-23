package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuqn.dao.society.SocietyUserMapper;
import com.yuqn.dao.society.SysUserMapper;
import com.yuqn.entity.User;
import com.yuqn.entity.society.*;
import com.yuqn.service.LoginService;
import com.yuqn.service.society.*;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.CollegeMajorClass;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyUserVo;
import com.yuqn.vo.UserLoginVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        sysUser.setPassword(JwtUtil.createJWT(societyUserVo.getPassword()));
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
        Result login = null;
        if ("0".equals(userLoginVo.getLoginType())) {
            System.out.println(" = ");
            // 根据电话获取用户
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getPhonenumber, userLoginVo.getPhonenumber());
            SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
            if (!Objects.isNull(sysUser)) {
                User user = new User();
                user.setUserName(sysUser.getUserName());
                // 先解密码，为了兼容已经封装的security框架
                try {
                    Claims claims = JwtUtil.parseJWT(sysUser.getPassword());
                    String subject = claims.getSubject();
                    user.setPassword(subject);
                }catch (Exception e){
                    log.error("尝试解密密码，但是错误了，错误信息：{}",e.getMessage());
                }
                login = loginService.login(user);

            }
        } else if ("1".equals(userLoginVo.getLoginType())) {
            return null;
        } else {
            login = Result.error(500, "登录类型错误");
        }
        return login;
    }
}
