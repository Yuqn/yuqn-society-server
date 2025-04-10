package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuqn.dao.society.*;
import com.yuqn.entity.society.*;
import com.yuqn.enums.DelFalgEnum;
import com.yuqn.enums.TypeEnum;
import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.utils.ActivitiCache;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
* @author yuqn
* @description 针对表【society_body】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Service
@Slf4j
public class SocietyBodyServiceImpl extends ServiceImpl<SocietyBodyMapper, SocietyBody>
    implements SocietyBodyService{

    // 定义字符集，包括数字、大小写字母
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_CODE_LENGTH = 6;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SocietyBodyMapper societyBodyMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SocietyBodyUserRoleMapper societyBodyUserRoleMapper;

    @Autowired
    private SocietyDepartmentMapper societyDepartmentMapper;

    @Autowired
    private SocietyDepartmentUserRoleMapper societyDepartmentUserRoleMapper;

    @Autowired
    private SocietyBusinessMapper societyBusinessMapper;

    @Autowired
    private ActivitiCache activitiCache;

    /**
     * @author: yuqn
     * @Date: 2025/3/2 23:06
     * @description:
     * 获取社团列表
     * @param: null
     * @return: null
     */
    @Override
    public Result getSocietyList(String societyName) {
        Result result = null;
        List<SocietyListVo> societyList = societyBodyMapper.getSocietyList(societyName);
        List<SocietyListVo> resultObj = new ArrayList<>();
        for (SocietyListVo societyListVo : societyList){
            SocietyListVo newSocietyListVo = new SocietyListVo();
            newSocietyListVo.setSocietyId( societyListVo.getSocietyId());
            newSocietyListVo.setSocietyName( societyListVo.getSocietyName());
            newSocietyListVo.setSocietyIntroduce( societyListVo.getSocietyIntroduce());
            newSocietyListVo.setCreateBy( societyListVo.getCreateBy());
            newSocietyListVo.setCreateTime( societyListVo.getCreateTime());
            newSocietyListVo.setSocietyByName( societyListVo.getSocietyByName());
            // 查询主席和副主席
            String chairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "4");
            String viceChairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "5");
            newSocietyListVo.setChairman(chairman);
            newSocietyListVo.setViceChairman(viceChairman);
            // 查询社团人数
            int societyUserCount = societyBodyMapper.getSocietyUserCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyUserCount(societyUserCount);
            // 查看社团部门数
            int societyDepartmentCount = societyBodyMapper.getSocietyDepartmentCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyDepartmentCount(societyDepartmentCount);
            System.out.println("newSocietyListVo = " + newSocietyListVo);
            // 整合数据
            resultObj.add(newSocietyListVo);
            System.out.println("resultObj = " + resultObj);
        }
        System.out.println("----resultObj = " + resultObj);
        result = Result.ok(resultObj);
        return result;
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
    public Result mkdirSocietyTeacher(SocietyMkdirVo societyMkdirVo,String userid) {
        Result result = null;
        // 添加社团表
        SocietyBody societyBody = new SocietyBody();
        societyBody.setName(societyMkdirVo.getSocietyName());
        societyBody.setIntroduce(societyMkdirVo.getSocietyIntroduce());
        societyBody.setType(TypeEnum.TEACHER_LEVEL);
        societyBody.setCreateBy(userid);
        int insert = societyBodyMapper.insert(societyBody);
        String societyId = "";
        if (insert == 1){
            // 获取社团id
            QueryWrapper<SocietyBody> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",societyMkdirVo.getSocietyName()).eq("introduce",societyMkdirVo.getSocietyIntroduce());
            SocietyBody societyBody1 = societyBodyMapper.selectOne(queryWrapper);
            societyId = societyBody1.getId();
            // 添加societybodyuserrole表，初始默认创建人为社团主席副主席，各部门部长和副部长
            for (int i=0;i<3;i++){
                SocietyBodyUserRole societyBodyUserRole = new SocietyBodyUserRole();
                societyBodyUserRole.setSocietyBodyId(societyId);
                societyBodyUserRole.setUserId(userid);
                societyBodyUserRole.setCreateBy(userid);
                if(i==0){
                    societyBodyUserRole.setRoleId("4");
                }else{
                    societyBodyUserRole.setRoleId("5");
                }
                int insert1 = societyBodyUserRoleMapper.insert(societyBodyUserRole);
            }
        }
        // 添加部门表
        if (societyMkdirVo.getDepartmentValue() != null){
            for (SocietyMkdirVo.DepartmentValueVo departmentValueVo : societyMkdirVo.getDepartmentValue()){
                SocietyDepartment societyDepartment = new SocietyDepartment();
                societyDepartment.setName(departmentValueVo.getDepartmentName());
                societyDepartment.setIntroduce(departmentValueVo.getDepartmentIntroduce());
                societyDepartment.setCreateBy(userid);
                societyDepartment.setSocietyBodyId(societyId);
                // 生成社团邀请码
                String s = generateRandomCode();
                System.out.println("邀请码 = " + s);
                societyDepartment.setDepartmentCode(s);
                int insert1 = societyDepartmentMapper.insert(societyDepartment);
                if(insert1 == 1){
                    // 获取部门id
                    QueryWrapper<SocietyDepartment> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("name",societyDepartment.getName())
                            .eq("introduce",societyDepartment.getIntroduce())
                            .eq("society_body_id",societyId);
                    SocietyDepartment societyDepartment1 = societyDepartmentMapper.selectOne(queryWrapper);
                    // 添加societydepartmentuserrole表，初始默认创建人为社团主席副主席，各部门部长和副部长
                    for (int i=0;i<3;i++){
                        SocietyDepartmentUserRole societyDepartmentUserRole = new SocietyDepartmentUserRole();
                        societyDepartmentUserRole.setDepartmentId(societyDepartment1.getId());
                        societyDepartmentUserRole.setUserId(userid);
                        societyDepartmentUserRole.setCreateBy(userid);
                        if(i==0){
                            societyDepartmentUserRole.setRoleId("6");
                        }else{
                            societyDepartmentUserRole.setRoleId("7");
                        }
                        int insert2 = societyDepartmentUserRoleMapper.insert(societyDepartmentUserRole);
                    }
                }
            }
        }
        result = Result.ok("添加成功");
        return result;
    }

    @Override
    public Result mkdirSocietyStudent(SocietyMkdirVo societyMkdirVo, String token) {
        // 解析当前用户id
        String userid = "";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            log.info("测试获取用户id，为：{}",userid);
            System.out.println("userid = " + userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 添加业务操作到业务表
        SocietyBusiness societyBusiness = new SocietyBusiness();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(societyMkdirVo);
            // json转string ==》 objectMapper.readValue(jsonString, MyEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        societyBusiness.setCommand(jsonString);
        societyBusiness.setCreateBy(userid);
        societyBusiness.setIntroduce("create_society");
        int insert = 0 ;
        try {
            insert = societyBusinessMapper.insert(societyBusiness);
        }catch (Exception e){
            e.printStackTrace();
        }
        String commandId = "";
        if (insert == 1){
            // 获取业务id
            QueryWrapper<SocietyBusiness> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("command",societyBusiness.getCommand())
                    .eq("create_by",societyBusiness.getCreateBy());
            SocietyBusiness societyBusiness1 = societyBusinessMapper.selectOne(queryWrapper);
            commandId = societyBusiness1.getId();
        }
        // 获取老师信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identity",1);
        SysUser sysUser = null;
        try {
            sysUser = sysUserMapper.selectOne(queryWrapper);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 启动工作流
        Map<String,Object> assigneeMap = new HashMap<>();
        assigneeMap.put("teacherId",sysUser.getId());
        // 流程key，业务key，审核人
        activitiCache.startProcess("society_mkdir",commandId,assigneeMap);
        return Result.ok("添加到业务表中，待审核即可");
    }

    @Override
    public Result getSocietyUserList(String societyId) {
        List<SocietyUserListVo> list = societyBodyMapper.getSocietyUserList(societyId);
        List<SocietyUserListVo> departmentUserList = societyDepartmentMapper.getDepartmentUserList(societyId);
        // 去重
        List<SocietyUserListVo> mergedList = mergeLists(list, departmentUserList);
        System.out.println("list = " + mergedList);
        Result result = Result.ok(mergedList);
        return result;
    }

    public static List<SocietyUserListVo> mergeLists(List<SocietyUserListVo> list1, List<SocietyUserListVo> list2) {
        List<SocietyUserListVo> mergedList = new ArrayList<>();
        Set<String> seenUserIds = new HashSet<>();
        // 辅助方法，用于将列表中的元素添加到mergedList中，同时跳过重复的元素
        Consumer<List<SocietyUserListVo>> addUniqueElements = (list) -> {
            for (SocietyUserListVo user : list) {
                String userId = user.getUserId();
                if (!seenUserIds.contains(userId)) {
                    mergedList.add(user);
                    seenUserIds.add(userId);
                }
            }
        };
        // 添加第一个列表中的元素
        addUniqueElements.accept(list1);
        // 添加第二个列表中的元素
        addUniqueElements.accept(list2);
        return mergedList;
    }

    @Override
    public Result getSocietyDetails(String societyId) {
        QueryWrapper<SocietyBody> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",societyId);
        SocietyBody societyBody = societyBodyMapper.selectOne(queryWrapper);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("societyId",societyId);
        QueryWrapper<SysUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id",societyBody.getCreateBy());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper1);
        objectObjectHashMap.put("societyCreateBy",sysUser.getNickName());
        objectObjectHashMap.put("societyName",societyBody.getName());
        objectObjectHashMap.put("societyCreateTime",societyBody.getCreateTime().toString());
        objectObjectHashMap.put("societyIntroduce",societyBody.getIntroduce());
        // 查询主席
        SocietyBodyUserRole societyBodyUserRole = societyBodyUserRoleMapper.selectOne(new QueryWrapper<SocietyBodyUserRole>().eq("society_body_id", societyId)
                .eq("role_id", "4").eq("del_flag", DelFalgEnum.NOTDEL));
        SysUser sysUser1 = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBodyUserRole.getUserId()));
        HashMap<Object, Object> objectObjectHashMap1 = new HashMap<>();
        objectObjectHashMap1.put("userId",societyBodyUserRole.getUserId());
        objectObjectHashMap1.put("nickName",sysUser1.getNickName());
        objectObjectHashMap1.put("roleId",societyBodyUserRole.getRoleId());
        objectObjectHashMap.put("chairman",objectObjectHashMap1);
        // 查询副主席
        List<SocietyBodyUserRole> societyBodyUserRoles = societyBodyUserRoleMapper.selectList(new QueryWrapper<SocietyBodyUserRole>().eq("society_body_id", societyId)
                .eq("role_id", "5").eq("del_flag", DelFalgEnum.NOTDEL));
        List<Object> viceChairman = new ArrayList<>();
        for (SocietyBodyUserRole societyBodyUserRole2 : societyBodyUserRoles){
            HashMap<Object, Object> objectObjectHashMap2 = new HashMap<>();
            sysUser1 = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBodyUserRole2.getUserId()));
            objectObjectHashMap2.put("userId",societyBodyUserRole2.getUserId());
            objectObjectHashMap2.put("nickName",sysUser1.getNickName());
            objectObjectHashMap2.put("roleId",societyBodyUserRole2.getRoleId());
            viceChairman.add(objectObjectHashMap2);
        }
        objectObjectHashMap.put("viceChairman",viceChairman);
        Result result = Result.ok(objectObjectHashMap);
        return result;
    }

    @Override
    @Transactional
    public Result societyUpdate(SocietyUpdateVo societyUpdateVo) {
        UpdateWrapper<SocietyBody> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",societyUpdateVo.getSocietyId());
        SocietyBody societyBody = new SocietyBody();
        societyBody.setName(societyUpdateVo.getSocietyName());
        societyBody.setIntroduce(societyUpdateVo.getSocietyIntroduce());
        int update = societyBodyMapper.update(societyBody, updateWrapper);
        // 设置主席
        if(update == 1 && societyUpdateVo.getChairmanId()!=null){
            UpdateWrapper<SocietyBodyUserRole> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("society_body_id",societyUpdateVo.getSocietyId())
                            .eq("role_id","4");
            SocietyBodyUserRole societyBodyUserRole = new SocietyBodyUserRole();
            societyBodyUserRole.setUserId(societyUpdateVo.getChairmanId());
            int update1 = societyBodyUserRoleMapper.update(societyBodyUserRole,updateWrapper1);
        }
        // 设置副主席
        for(UserBodyRole societyupdate: societyUpdateVo.getViceChairmanId()){
            QueryWrapper<SocietyBodyUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("society_body_id",societyUpdateVo.getSocietyId())
                    .eq("role_id","5");
            List<SocietyBodyUserRole> societyBodyUserRoles = societyBodyUserRoleMapper.selectList(queryWrapper);
            for(SocietyBodyUserRole societyBodyUserRole: societyBodyUserRoles){
                UpdateWrapper<SocietyBodyUserRole> updateWrapper2 = new UpdateWrapper<>();
                updateWrapper2.eq("society_body_id",societyUpdateVo.getSocietyId())
                        .eq("role_id","5")
                        .eq("id",societyBodyUserRole.getId());
                SocietyBodyUserRole societyBodyUserRole1 = new SocietyBodyUserRole();
                societyBodyUserRole1.setUserId(societyupdate.getUserId());
                int update1 = societyBodyUserRoleMapper.update(societyBodyUserRole1, updateWrapper2);
            }
        }
        return Result.ok("修改成功");
    }

    @Override
    @Transactional
    public Result societyUpdate(SocietyUpdateVo societyUpdateVo, String token) {
        Result result = null;
        // 解析当前用户id
        String userid = "";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            log.info("测试获取用户id，为：{}",userid);
            System.out.println("userid = " + userid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        SocietyBody societyBody = new SocietyBody();
        societyBody.setName(societyUpdateVo.getSocietyBody().getName());
        societyBody.setIntroduce(societyUpdateVo.getSocietyBody().getIntroduce());
        societyBody.setUpdateBy(userid);
        int update = societyBodyMapper.update(societyBody, new UpdateWrapper<SocietyBody>().eq("id",societyUpdateVo.getSocietyBody().getId()));
        if (update == 1){
            // 更改原部长信息
            SocietyBodyUserRole societyBodyUserRole = societyBodyUserRoleMapper.selectOne(new QueryWrapper<SocietyBodyUserRole>()
                    .eq("society_body_id", societyUpdateVo.getSocietyBody().getId())
                    .eq("del_flag", DelFalgEnum.NOTDEL)
                    .eq("role_id", "4"));
            if (societyBodyUserRole != null){
                societyBodyUserRole.setDelFlag(DelFalgEnum.DEL);
                societyBodyUserRole.setUpdateBy(userid);
                int update1 = societyBodyUserRoleMapper.updateById(societyBodyUserRole);
            }
            // 插入新部长信息
            SocietyBodyUserRole societyBodyUserRole1 = new SocietyBodyUserRole();
            societyBodyUserRole1.setSocietyBodyId(societyUpdateVo.getSocietyBody().getId());
            societyBodyUserRole1.setRoleId("4");
            societyBodyUserRole1.setUserId(societyUpdateVo.getChairman().getUserId());
            societyBodyUserRole1.setDelFlag(DelFalgEnum.NOTDEL);
            societyBodyUserRole1.setCreateBy(userid);
            int insert = societyBodyUserRoleMapper.insert(societyBodyUserRole1);
            // 更改原副部长信息
            List<SocietyBodyUserRole> societyBodyUserRoles = societyBodyUserRoleMapper.selectList(new QueryWrapper<SocietyBodyUserRole>()
                    .eq("del_flag", DelFalgEnum.NOTDEL)
                    .eq("society_body_id", societyUpdateVo.getSocietyBody().getId())
                    .eq("role_id", "5"));
            for(SocietyBodyUserRole societyBodyUserVice: societyBodyUserRoles){
                societyBodyUserVice.setDelFlag(DelFalgEnum.DEL);
                societyBodyUserVice.setUpdateBy(userid);
                int update1 = societyBodyUserRoleMapper.updateById(societyBodyUserVice);
            }
            // 插入新的副部长信息
            for (SysUserRole viceChairman : societyUpdateVo.getViceChairman()){
                SocietyBodyUserRole societyBodyUserRole2 = new SocietyBodyUserRole();
                societyBodyUserRole2.setDelFlag(DelFalgEnum.NOTDEL);
                societyBodyUserRole2.setSocietyBodyId(societyUpdateVo.getSocietyBody().getId());
                societyBodyUserRole2.setRoleId("5");
                societyBodyUserRole2.setUserId(viceChairman.getUserId());
                societyBodyUserRole2.setCreateBy(userid);
                int insert1 = societyBodyUserRoleMapper.insert(societyBodyUserRole2);
            }
            result = Result.ok("修改成功");
        }
        return result;
    }

    @Override
    public Result getSocietyDetailsById(String societyId) {
        Result result = null;
        List<SocietyListVo> societyList = societyBodyMapper.getSocietyDetailsById(societyId);
        List<SocietyListVo> resultObj = new ArrayList<>();
        for (SocietyListVo societyListVo : societyList){
            SocietyListVo newSocietyListVo = new SocietyListVo();
            newSocietyListVo.setSocietyId( societyListVo.getSocietyId());
            newSocietyListVo.setSocietyName( societyListVo.getSocietyName());
            newSocietyListVo.setSocietyIntroduce( societyListVo.getSocietyIntroduce());
            newSocietyListVo.setCreateBy( societyListVo.getCreateBy());
            newSocietyListVo.setCreateTime( societyListVo.getCreateTime());
            newSocietyListVo.setSocietyByName( societyListVo.getSocietyByName());
            // 查询主席和副主席
            String chairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "4");
            String viceChairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "5");
            newSocietyListVo.setChairman(chairman);
            newSocietyListVo.setViceChairman(viceChairman);
            // 查询社团人数
            int societyUserCount = societyBodyMapper.getSocietyUserCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyUserCount(societyUserCount);
            // 查看社团部门数
            int societyDepartmentCount = societyBodyMapper.getSocietyDepartmentCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyDepartmentCount(societyDepartmentCount);
            System.out.println("newSocietyListVo = " + newSocietyListVo);
            // 整合数据
            resultObj.add(newSocietyListVo);
            System.out.println("resultObj = " + resultObj);
        }
        System.out.println("----resultObj = " + resultObj);
        result = Result.ok(resultObj);
        return result;
    }
}




