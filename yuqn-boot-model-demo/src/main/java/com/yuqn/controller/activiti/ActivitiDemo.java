package com.yuqn.controller.activiti;

import com.yuqn.entity.Evection;
import com.yuqn.utils.ActivitiCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: yuqn
 * @Date: 2024/4/23 22:33
 * @description:
 * activiti demo
 * @version: 1.0
 */
@RestController
@RequestMapping("/activiti")
@Tag(name = "activiti模块")
public class ActivitiDemo {

    @Autowired
    private ActivitiCache activitiCache;

    /**
     * 启动任务
     */
    @PostMapping("/startProcess")
    @Operation(summary = "启动实例")
    @PreAuthorize("@ex.hasAuthority('system:test:list')")
    public void startProcess(@RequestParam("key") String key){
        Map<String,Object> map = new HashMap<>();
        map.put("assignee0","evection_yuqn_0");
        map.put("assignee1","evection_yuqn_1");
        map.put("assignee2","evection_yuqn_2");
        Evection evection = new Evection();
        evection.setNum(2d);
        map.put("evection",evection);
        ProcessInstance processInstance = activitiCache.startProcess(key,map);
        if (Objects.nonNull(processInstance)){
            System.out.println("启动成功");
            System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
            System.out.println("流程实例id：" + processInstance.getId());
            System.out.println("当前活动的id：" + processInstance.getActivityId());
        }
    }

    /**
     * 完成个人任务
     */
    @PostMapping("/completTask")
    @Operation(summary = "完成任务")
    @PreAuthorize("@ex.hasAuthority('system:test:list')")
    public void completTask(@RequestParam("key") String key,@RequestParam("assignee") String assignee){
        boolean isok = activitiCache.completTask(key,assignee);
        if (isok){
            System.out.println("任务完成");
        }
    }

    /**
     * 删除部署任务
     */
    @GetMapping("/deleteDeployMentCascade")
    @Operation(summary = "删除任务")
    @PreAuthorize("@ex.hasAuthority('system:test:list')")
    public void deleteDeployMentCascade(){
        boolean isok = activitiCache.deleteDeployMentCascade("4b5fc1ec-20b9-11ef-b56b-2279185380b8");
        boolean t = activitiCache.deleteDeployMentCascade("7252e9ee-2030-11ef-85a1-2279185380b8");
        if (isok){
            System.out.println("删除完成");
        }
    }

}
