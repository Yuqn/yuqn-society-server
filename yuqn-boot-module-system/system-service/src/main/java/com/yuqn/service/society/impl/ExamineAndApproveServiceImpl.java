package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuqn.dao.society.SocietyBusinessMapper;
import com.yuqn.dao.society.SysUserMapper;
import com.yuqn.entity.society.SocietyBusiness;
import com.yuqn.entity.society.SysUser;
import com.yuqn.service.society.ExamineAndApproveService;
import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.utils.ActivitiCache;
import com.yuqn.utils.JwtUtil;
import com.yuqn.vo.ExamineAndApproveVo;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyMkdirVo;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: yuqn
 * @Date: 2025/3/7 21:59
 * @description:
 * 审批模块
 * @version: 1.0
 */
@Service
@Slf4j
public class ExamineAndApproveServiceImpl implements ExamineAndApproveService {

    @Autowired
    private ActivitiCache activitiCache;

    /**
     * 因为使用了整合框架，所以可以直接使用注入的方式来使用相关的类
     */
    @Resource
    private ProcessEngine processEngine;

    @Resource
    private ProcessRuntime processRuntime;

    @Resource
    private TaskRuntime taskRuntime;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private SocietyBusinessMapper societyBusinessMapper;

    @Autowired
    private SocietyBodyService societyBodyService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Result getExamineAndApproveForTeacher(String token) {
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

        List<Task> taskList = null;
        try {
            // 根据 流程key 和 任务负责人 查询任务
            taskList = taskService.createTaskQuery()
                    .taskAssignee(userid)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (taskList != null && taskList.size() > 0) {
            List<ExamineAndApproveVo> societyBusinessList = new ArrayList<>();
            for (Task task : taskList) {
                System.out.println("task --------------= " + task);
                System.out.println("task = " + task.getProcessInstanceId());
                System.out.println("task = " + task.getBusinessKey());
                // 查找数据库获取业务表数据
                QueryWrapper<SocietyBusiness> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",task.getBusinessKey());
                queryWrapper.eq("is_examine","0");
                SocietyBusiness societyBusiness = societyBusinessMapper.selectOne(queryWrapper);
                ExamineAndApproveVo examineAndApproveVo = new ExamineAndApproveVo();
                examineAndApproveVo.setSocietyBusiness(societyBusiness);
                examineAndApproveVo.setProcessInstanceId(task.getProcessInstanceId());
                examineAndApproveVo.setTaskId(task.getId());
                SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBusiness.getCreateBy()));
                examineAndApproveVo.setCreateName(sysUser.getNickName());
                societyBusinessList.add(examineAndApproveVo);
            }
            result = Result.ok(societyBusinessList);
        }
        return result;
    }

    @Override
    @Transactional
    public Result rejectExamineAndApprove(String token, ExamineAndApproveVo examineAndApproveVo) {
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
        // 开始驳回
        result = Result.ok("请求失败");
        try {
            runtimeService.deleteProcessInstance(examineAndApproveVo.getProcessInstanceId(),examineAndApproveVo.getReason());
            SocietyBusiness societyBusiness = societyBusinessMapper.selectById(examineAndApproveVo.getBusinessKey());
            societyBusiness.setIsExamine("2");
            societyBusiness.setUpdateBy(userid);
            int id = societyBusinessMapper.update(societyBusiness, new UpdateWrapper<SocietyBusiness>().eq("id", examineAndApproveVo.getBusinessKey()));
            result = Result.ok("驳回成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional
    public Result passExamineAndApprove(String token, ExamineAndApproveVo examineAndApproveVo) {
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
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("approvalReason", examineAndApproveVo.getReason());
            variables.put("userId",userid);
            taskService.complete(examineAndApproveVo.getTaskId(),variables);
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(examineAndApproveVo.getProcessInstanceId()).singleResult();
            // 如果流程已经执行完毕，则会完成业务操作
            if (processInstance == null){
                // 获取业务key
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(examineAndApproveVo.getProcessInstanceId())
                        .singleResult();
                String businessKey = historicProcessInstance.getBusinessKey();
                SocietyBusiness societyBusiness = societyBusinessMapper.selectById(businessKey);
                // 创建社团
                ObjectMapper objectMapper = new ObjectMapper();
                SocietyMkdirVo societyMkdirVo = objectMapper.readValue(societyBusiness.getCommand(), SocietyMkdirVo.class);
                Result result1 = societyBodyService.mkdirSocietyTeacher(societyMkdirVo, societyBusiness.getCreateBy());
                // 更改业务表状态
                societyBusiness.setIsExamine("1");
                societyBusiness.setUpdateBy(userid);
                int updateCount = societyBusinessMapper.update(societyBusiness,
                        new UpdateWrapper<SocietyBusiness>().eq("id", businessKey));
            }
            result = Result.ok("已经审批");
        }catch (Exception e){
            e.printStackTrace();
            result = Result.ok("审批失败");
        }
        // 通过任务
        return result;
    }

    @Override
    public Result getRejectList(String token, String typeId) {
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
        List<SocietyBusiness> societyBusinesses = societyBusinessMapper.selectList(new QueryWrapper<SocietyBusiness>()
                .eq("is_examine", typeId).eq("update_by", userid));
        System.out.println("societyBusinesses = " + societyBusinesses);
        List<ExamineAndApproveVo> societyBusinessList = new ArrayList<>();
        for (SocietyBusiness societyBusiness : societyBusinesses){
            ExamineAndApproveVo examineAndApproveVo = new ExamineAndApproveVo();
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBusiness.getCreateBy()));
            examineAndApproveVo.setSocietyBusiness(societyBusiness);
            examineAndApproveVo.setCreateName(sysUser.getNickName());
            societyBusinessList.add(examineAndApproveVo);
        }
        return Result.ok(societyBusinessList);
    }

    @Override
    public Result getAllExamineAndApprove(String token) {
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
        // 获取待审批数量
        Long reviewCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "0"));
        Long passCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "1"));
        Long refuseCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "2"));
        Long count = reviewCount + passCount + refuseCount;
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("reviewCount",reviewCount.toString());
        objectObjectHashMap.put("passCount",passCount.toString());
        objectObjectHashMap.put("refuseCount",refuseCount.toString());
        objectObjectHashMap.put("count",count.toString());
        return Result.ok(objectObjectHashMap);
    }

    @Override
    public Result getApplicationRecordByUserId(String token) {
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
        List<SocietyBusiness> societyBusinesses = societyBusinessMapper.selectList(new QueryWrapper<SocietyBusiness>()
                .eq("create_by", userid)
                .eq("is_examine", "0"));
        List<ExamineAndApproveVo> societyBusinessList = new ArrayList<>();
        for (SocietyBusiness societyBusiness : societyBusinesses){
            ExamineAndApproveVo examineAndApproveVo = new ExamineAndApproveVo();
            examineAndApproveVo.setSocietyBusiness(societyBusiness);
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBusiness.getCreateBy()));
            examineAndApproveVo.setCreateName(sysUser.getNickName());
            examineAndApproveVo.setBusinessKey(societyBusiness.getId());
            // 获取key实例
            Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(societyBusiness.getId()).singleResult();
            examineAndApproveVo.setProcessInstanceId(execution.getProcessInstanceId());
            societyBusinessList.add(examineAndApproveVo);
        }
        result = Result.ok(societyBusinessList);
        return result;
    }

    @Override
    public Result getApplicationRecordDetails(String token, String processInstanceId, String businessKey) {
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
        // 根据流程key，查询流程的任务，待审批的流程，基本都在任务表中
        List objects = new ArrayList<>();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        for (HistoricActivityInstance historicActivityInstance : list){
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("id",historicActivityInstance.getActivityId());
            if(historicActivityInstance.getEndTime() != null){
                objectObjectHashMap.put("time",historicActivityInstance.getEndTime());
            }else{
                objectObjectHashMap.put("time","");
            }
            objectObjectHashMap.put("actType",historicActivityInstance.getActivityType());
            if (historicActivityInstance.getAssignee()!=null){
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",historicActivityInstance.getAssignee())).getNickName());
            }else{
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",userid)).getNickName());
            }
            // 记录类型方便前端对接，0发起，1审批中，2通过
            if ("startEvent".equals(historicActivityInstance.getActivityType())){
                objectObjectHashMap.put("isType",0);
            }else{
                if(historicActivityInstance.getEndTime() == null){
                    objectObjectHashMap.put("isType",1);
                }else {
                    objectObjectHashMap.put("isType",2);
                }
            }
            objects.add(objectObjectHashMap);
        }
        result = Result.ok(objects);
        return result;
    }

    @Override
    public Result getApplicationRecordPassByUserId(String token) {
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
        List<SocietyBusiness> societyBusinesses = societyBusinessMapper.selectList(new QueryWrapper<SocietyBusiness>()
                .eq("create_by", userid)
                .eq("is_examine", "1"));
        List<ExamineAndApproveVo> societyBusinessList = new ArrayList<>();
        for (SocietyBusiness societyBusiness : societyBusinesses){
            ExamineAndApproveVo examineAndApproveVo = new ExamineAndApproveVo();
            examineAndApproveVo.setSocietyBusiness(societyBusiness);
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBusiness.getCreateBy()));
            examineAndApproveVo.setCreateName(sysUser.getNickName());
            examineAndApproveVo.setBusinessKey(societyBusiness.getId());
            // 获取key实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(societyBusiness.getId()).singleResult();
            examineAndApproveVo.setProcessInstanceId(historicProcessInstance.getId());
            societyBusinessList.add(examineAndApproveVo);
        }
        result = Result.ok(societyBusinessList);
        return result;
    }

    @Override
    public Result getApplicationRecordPassDetails(String token, String processInstanceId, String businessKey) {
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
        // 根据流程key，查询流程的任务，待审批的流程，基本都在任务表中
        List objects = new ArrayList<>();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        list.sort(Comparator.comparing(HistoricActivityInstance::getStartTime));
        for (HistoricActivityInstance historicActivityInstance : list){
            if ("endEvent".equals(historicActivityInstance.getActivityType())){
                continue;
            }
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("id",historicActivityInstance.getActivityId());
            if(historicActivityInstance.getEndTime() != null){
                objectObjectHashMap.put("time",historicActivityInstance.getEndTime());
            }else{
                objectObjectHashMap.put("time","");
            }
            objectObjectHashMap.put("actType",historicActivityInstance.getActivityType());
            if (historicActivityInstance.getAssignee()!=null){
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",historicActivityInstance.getAssignee())).getNickName());
            }else{
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",userid)).getNickName());
            }
            // 记录类型方便前端对接，0发起，1审批中，2通过
            if ("startEvent".equals(historicActivityInstance.getActivityType())){
                objectObjectHashMap.put("isType",0);
            }else if ("userTask".equals(historicActivityInstance.getActivityType())){
                objectObjectHashMap.put("isType",2);
            }
            objects.add(objectObjectHashMap);
        }
        result = Result.ok(objects);
        return result;
    }

    @Override
    public Result getApplicationRecordNoPassByUserId(String token) {
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
        List<SocietyBusiness> societyBusinesses = societyBusinessMapper.selectList(new QueryWrapper<SocietyBusiness>()
                .eq("create_by", userid)
                .eq("is_examine", "2"));
        List<ExamineAndApproveVo> societyBusinessList = new ArrayList<>();
        for (SocietyBusiness societyBusiness : societyBusinesses){
            ExamineAndApproveVo examineAndApproveVo = new ExamineAndApproveVo();
            examineAndApproveVo.setSocietyBusiness(societyBusiness);
            SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id", societyBusiness.getCreateBy()));
            examineAndApproveVo.setCreateName(sysUser.getNickName());
            examineAndApproveVo.setBusinessKey(societyBusiness.getId());
            // 获取key实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(societyBusiness.getId()).singleResult();
            examineAndApproveVo.setProcessInstanceId(historicProcessInstance.getId());
            societyBusinessList.add(examineAndApproveVo);
        }
        result = Result.ok(societyBusinessList);
        return result;
    }

    @Override
    public Result getApplicationRecordNoPassDetails(String token, String processInstanceId, String businessKey) {
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
        // 根据流程key，查询流程的任务，待审批的流程，基本都在任务表中
        List objects = new ArrayList<>();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        list.sort(Comparator.comparing(HistoricActivityInstance::getStartTime));
        for (HistoricActivityInstance historicActivityInstance : list){
            if ("endEvent".equals(historicActivityInstance.getActivityType())){
                continue;
            }
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("id",historicActivityInstance.getActivityId());
            if(historicActivityInstance.getEndTime() != null){
                objectObjectHashMap.put("time",historicActivityInstance.getEndTime());
            }else{
                objectObjectHashMap.put("time","");
            }
            objectObjectHashMap.put("actType",historicActivityInstance.getActivityType());
            if (historicActivityInstance.getAssignee()!=null){
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",historicActivityInstance.getAssignee())).getNickName());
            }else{
                objectObjectHashMap.put("assignee",sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("id",userid)).getNickName());
            }
            // 记录类型方便前端对接，0发起，1审批中，2通过,3拒绝
            if ("startEvent".equals(historicActivityInstance.getActivityType())){
                objectObjectHashMap.put("isType",0);
            }else if ("userTask".equals(historicActivityInstance.getActivityType())){
                objectObjectHashMap.put("isType",3);
            }
            objects.add(objectObjectHashMap);
        }
        result = Result.ok(objects);
        return result;
    }

    @Override
    public Result getApplicationRecordTop(String token) {
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
        // 获取待审批数量
        Long reviewCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "0")
                .eq("create_by",userid));
        Long passCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "1")
                .eq("create_by",userid));
        Long refuseCount = societyBusinessMapper.selectCount(new QueryWrapper<SocietyBusiness>().eq("is_examine", "2")
                .eq("create_by",userid));
        Long count = reviewCount + passCount + refuseCount;
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("reviewCount",reviewCount.toString());
        objectObjectHashMap.put("passCount",passCount.toString());
        objectObjectHashMap.put("refuseCount",refuseCount.toString());
        objectObjectHashMap.put("count",count.toString());
        return Result.ok(objectObjectHashMap);
    }
}
