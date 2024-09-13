package com.yuqn.utils;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class ActivitiCache {

    private final static String SUCCESS = "成功";
    private final static String FAIL = "失败";

    /**
     * 因为使用了整合框架，所以可以直接使用注入的方式来使用相关的类
     */
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;


    /**
     * @author: yuqn
     * @Date: 2024/6/2 18:25
     * @description:
     * 统一日志打印
     * @param: taskName：任务名字
     * @param: summary：任务摘要
     * @param: status：任务状态
     * @param: mes：日志信息
     * @return: null
     */
    public void logInfo(String taskName,String summary,String status,String mes){
        log.info("Activiti 日志打印开始：");
        log.info("任务名字：" + taskName);
        log.info("日志摘要：" + summary);
        log.info("任务状态：" + status);
        log.info("日志信息：" + mes);
        log.info("Activiti 日志打印结束！");
    }
    public void logError(String taskName,String summary,String status,String mes){
        log.error("Activiti 日志打印开始：");
        log.error("任务名字：" + taskName);
        log.error("日志摘要：" + summary);
        log.error("任务状态：" + status);
        log.error("日志信息：" + mes);
        log.error("Activiti 日志打印结束！");
    }

    /**
     * @author: yuqn
     * @Date: 2024/4/29 17:22
     * @description:
     * 根据流程key启动流程实例
     * @param: proc_key:任务key
     * @return: ProcessInstance
     */
    public ProcessInstance startProcess(String proc_key){
        ProcessInstance instance = null;
        try {
            // 根据流程定义的id启动流程
            instance = runtimeService.startProcessInstanceByKey(proc_key);
            String mes = "流程定义id：" + instance.getProcessDefinitionId() + " " + "流程实例id：" + instance.getId() + " " + "当前活动的id：" + instance.getActivityId();
            logInfo(instance.getName(), "启动任务",SUCCESS, mes);
        } catch (Exception e) {
            e.printStackTrace();
            logError(proc_key,"启动失败",FAIL,e.getMessage());
        } finally {
            return instance;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:09
     * @description:
     * 启动流程，同时添加业务key到 activiti 启动的记录表中
     * @param: proc_key：流程定义的key
     * @param: businessKsy：自定义业务key
     * @return: ProcessInstance
     */
    public ProcessInstance startProcess(String proc_key, String businessKsy){
        ProcessInstance instance = null;
        try {
            // 启动流程的过程中，添加 businesskey
            instance = runtimeService.startProcessInstanceByKey(proc_key, businessKsy);
            String mes = "流程定义id：" + instance.getProcessDefinitionId() + " " + "流程实例id：" + instance.getId() + " " + "当前活动的id：" + instance.getActivityId();
            logInfo(instance.getName(), "启动任务",SUCCESS, mes);
        } catch (Exception e) {
            e.printStackTrace();
            logError(proc_key,"启动失败",FAIL,e.getMessage());
        } finally {
            return instance;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/11 15:52
     * @description:
     * 启动流程，基于assignee uel模式，在启动流程时传入uel模式变量
     * Map<String,Object> assigneeMap = new HashMap<>();
     *             assigneeMap.put("assignee0","yuqn");
     *             assigneeMap.put("assignee1","李经理");
     *             assigneeMap.put("assignee2","王总经理");
     *             assigneeMap.put("assignee3","赵财务");
     *             startAssigneeUel(String proc_key,assigneeMap)
     * 或添加一个对象，用于保存流程变量
     * HashMap<String, Object> variables = new HashMap<>();
     *         // 设置流程变量
     *         Evection evection = new Evection();
     *         // 设置出差日期
     *         evection.setNum(3d);
     *         // 把流程变量的pojo放入map
     *         variables.put("evection",evection);
     *         // 设定任务负责人
     *         variables.put("assignee0","yuqn-lobal-0");
     *         variables.put("assignee1","yuqn-lobal-1");
     *         variables.put("assignee2","yuqn-lobal-2");
     *         variables.put("assignee3","yuqn-lobal-3");
     *
     * @param: proc_key：自定义业务key
     * @param: assigneeMap：用于保存流程变量和流程审核人名字
     * @return: ProcessInstance
     */
    public ProcessInstance startProcess(String proc_key, Map<String,Object> assigneeMap){
        ProcessInstance instance = null;
        try {
            // 启动流程实例
            instance = runtimeService.startProcessInstanceByKey(proc_key,assigneeMap);
            String mes = "流程定义id：" + instance.getProcessDefinitionId() + " " + "流程实例id：" + instance.getId() + " " + "当前活动的id：" + instance.getActivityId();
            logInfo(instance.getName(), "启动任务",SUCCESS, mes);
        } catch (Exception e) {
            e.printStackTrace();
            logError(proc_key,"启动失败",FAIL,e.getMessage());
        } finally {
            return instance;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:09
     * @description:
     * 启动流程，同时添加业务key到 activiti 启动的记录表中，添加任务审核人活流程变量
     * @param: proc_key：流程id
     * @param: businessKsy：自定义业务key
     * @param: assigneeMap：用于保存流程变量和流程审核人名字
     * @return: ProcessInstance
     */
    public ProcessInstance startProcess(String proc_key, String businessKsy, Map<String,Object> assigneeMap){
        ProcessInstance instance = null;
        try {
            // 启动流程的过程中，添加 businesskey
            instance = runtimeService.startProcessInstanceByKey(proc_key, businessKsy, assigneeMap);
            String mes = "流程定义id：" + instance.getProcessDefinitionId() + " " + "流程实例id：" + instance.getId() + " " + "当前活动的id：" + instance.getActivityId();
            logInfo(instance.getName(), "启动任务",SUCCESS, mes);
        } catch (Exception e) {
            e.printStackTrace();
            logError(proc_key,"启动失败",FAIL,e.getMessage());
        } finally {
            return instance;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/5 23:41
     * @description:
     * 查询所有定义流程
     * @param: null
     * @return: null
     */
    public List<org.activiti.engine.repository.ProcessDefinition> queryProcessDefinition(String proc_key){
        List<org.activiti.engine.repository.ProcessDefinition> definitionList = null;
        try {
            // 获取processdifinitionquery对象
            ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询当前所有的流程定义，返回流程定义信息的集合
            // processDefinitionKey(里路程定义的key)
            // orderbyprocessdefinitionversion进行排序
            // desc 倒序
            // list 查询出所有内容
            definitionList = definitionQuery.processDefinitionKey(proc_key)
                    .orderByProcessDefinitionVersion()
                    .desc()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            logError(proc_key,"查询所有定义流程",FAIL,e.getMessage());
        } finally {
            // 输出信息
            return definitionList;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/4/29 18:18
     * @description:
     * 查询个人待执行任务
     * @param: proc_key：流程key
     * @param: assignee：任务负责人
     * @return: null
     */
    public List<Task> findPersonalTaskList(String proc_key,String assignee){
        List<Task> taskList = null;
        try {
            // 根据 流程key 和 任务负责人 查询任务
            taskList = taskService.createTaskQuery()
                    .processDefinitionKey(proc_key)
                    .taskAssignee(assignee)
                    .list();
            logInfo("用户" + assignee + "查找个人待执行任务","成功",SUCCESS,"查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            logError("用户" + assignee + "查找个人待执行任务","失败",FAIL,e.getMessage());
        } finally {
            return taskList;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/9 17:37
     * @description:
     * 流程历史信息查看
     * @param: processInstanceId：编号
     * @return: List
     */
    public List<HistoricActivityInstance> findHistoryInfo(String processInstanceId){
        List<HistoricActivityInstance> activityInstanceList = null;
        try {
            // 获取actinst表的查询对象
            HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
            // 查询actinst表
            instanceQuery.processInstanceId(processInstanceId);
            instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
            // 查询所有内容
            activityInstanceList = instanceQuery.list();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            return activityInstanceList;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:50
     * @description:
     * 全部流程实例的挂起和激活
     * suspend暂停
     * @param: null
     * @return: null
     */
    public boolean suspendAllProcessInstance(String proc_key){
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(proc_key)
                .singleResult();
        // 获取读取流程定义的实力是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 获取流程定义的id
        String definitionId = processDefinition.getId();
        if(suspended){
            /*
             * 如果是挂起状态，改为激活状态
             * 参数一：流程定义id
             * 参数二：是否激活
             * 参数三：激活时间
             * */
            repositoryService.activateProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id：" + definitionId + "已激活");
            return true;
        }else{
            /*
             * 如果是激活状态，改为挂起状态
             * 参数一：流程定义id
             * 参数二：是否暂停
             * 参数三：暂停时间
             * */
            repositoryService.suspendProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id：" + definitionId + "已挂起");
            return false;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/10 16:50
     * @description:
     * 单个流程实例的挂起和激活
     * suspend暂停
     * @param: processInstanceId
     * @return: null
     */
    public boolean suspendSingleProcessInstance(String processInstanceId){
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、通过runtimeservice获取流程实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        // 4、得到当前流程实例的暂停状态
        boolean suspended = instance.isSuspended();
        // 5、获取流程实例id
        String instanceId = instance.getId();
        // 6、如果已经暂停，执行激活操作
        if(suspended){
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id：" + instanceId + "已经激活");
            return true;
        }else{
            // 7、如果已经激活，执行暂停操作
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id：" + instanceId + "已经暂停");
            return false;
        }

    }

    /**
     * @author: yuqn
     * @Date: 2024/5/9 16:41
     * @description:
     * 下载资源文件
     * 方案一：使用activiti提供的api下载资源文件，保存到文件目录
     * 方案二：自己写代码从数据库中下载文件
     * @param: null
     * @return: null
     */
    public void getDeployment(String proc_key,String filePath) throws IOException {
        // 获取查询对象 processdefinitionquery，查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(proc_key)
                .singleResult();
        // 通过流程定义信息，获取部署id
        String deploymentId = processDefinition.getDeploymentId();
        // 通过repositoryservice，传递部署id参数，读取资源信息（png和bpmn）
        // 获取png图片的流
        String pngName = processDefinition.getDiagramResourceName();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        // 获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 构造outputstream流
        FileOutputStream pngOutStream = null;
        FileOutputStream bpmnOutStream = null;
        try {
            File pngFile = new File(filePath + "/evectionflow01.png");
            File bpmnFile = new File(filePath + "/evectionflow01.bpmn");
            pngOutStream = new FileOutputStream(pngFile);
            bpmnOutStream = new FileOutputStream(bpmnFile);
            // 输入流、输出流的转换
            IOUtils.copy(pngInput,pngOutStream);
            IOUtils.copy(bpmnInput,bpmnOutStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            // 关闭流
            pngOutStream.close();
            bpmnOutStream.close();
            pngInput.close();
            bpmnInput.close();
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/6/2 17:05
     * @description:
     * 根据任务id完成对应的流程实例
     * @param: 任务id
     * @return: null
     */
    public boolean completTask(String taskId){
        // 根据任务id完成对应的流程实例
        try {
            taskService.complete(taskId);
            logInfo(taskId,"完成任务",SUCCESS,"根据id完成任务完成");
            return true;
        }catch (Exception e){
            logError(taskId,"完成任务失败",FAIL,e.getMessage());
            return false;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/6/2 16:58
     * @description:
     * 完成个人任务
     * @param: 流程id，完成人
     * @return: null
     */
    public boolean completTask(String proc_key,String assignee){
        // 根据key和完成人获取对应的流程实例
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(proc_key)
                .taskAssignee(assignee)
                .singleResult();
        if (Objects.nonNull(task)){
            // 完成jerry的任务
            taskService.complete(task.getId());
            logInfo(task.getName(),"完成个人任务",SUCCESS,"任务完成");
            log.info("任务：" + task.getId() + " 状态：已完成");
            return true;
        }else {
            log.info("找不到"+ proc_key + "->" + assignee + " 对应的任务");
            return false;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/12 1:04
     * @description:
     * 完成个人任务，同时设置流程变量，注：如果当前审批节点完成后，需要分支，则需要设置流程变量了
     * Evection evection = new Evection();
     *         evection.setNum(2d);
     *         HashMap<String, Object> map = new HashMap<>();
     *         map.put("evection",evection);
     * @param: proc_key：流程key
     * @param: assignee：任务执行人
     * @param: map：流程变量，对象存在 evection 属性
     * @return: null
     */
    public void completTask(String proc_key, String assignee, HashMap<String, Object> map){
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(proc_key)
                .taskAssignee(assignee)
                .singleResult();
        if(task != null){
            taskService.complete(task.getId(),map);
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/14 23:44
     * @description:
     * 拾取任务，执行后，指定的人就能变为当前审批任务的负责人
     * 用于设置分组的情况，组中有多个审批者，这要先拾取任务，表中的任务才能有审批者
     * @param: taskId：当前任务id，为act_ru_task的id值
     * @param: candidateUser：任务候选人
     * @return: boolean
     */
    public boolean claimTask(String taskId,String candidateUser){
        // 查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if (Objects.nonNull(task)){
            // 拾取任务
            taskService.claim(taskId,candidateUser);
            System.out.println( "taskId-" + taskId + "- 用户 -" + candidateUser + " - 任务拾取完成");
            return true;
        }
        return false;
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/15 22:14
     * @description:
     * 任务归还
     * 如果任务拾取后，想把该任务的审批者重新置空
     * @param: taskId：当前任务id，为act_ru_task的id值
     * @param: assignee：任务审批者
     * @return: null
     */
    public boolean assigneeToGroupTask(String taskId,String assignee){
        // 根据key和负责人查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(Objects.nonNull(task)){
            // 归还任务，就是把负责人设置为空
            taskService.setAssignee(taskId,null);
            System.out.println("taskid-" + taskId + "-归还任务完成");
            return true;
        }
        return false;
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/15 22:25
     * @description:
     * 任务交接，切换任务候选人
     * @param: taskId：当前任务id，为act_ru_task的id值
     * @param: assignee：任务审批者
     * @param: candidateUser：任务候选人
     * @return: null
     */
    public boolean assigneeToCandidateUser(String taskId, String assignee, String candidateUser){
        // 根据key和负责人来查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(Objects.nonNull(task)){
            taskService.setAssignee(taskId,candidateUser);
            System.out.println("taskid-" + taskId + "-交接任务完成");
            return true;
        }
        return false;
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/5 23:41
     * @description:
     * 删除流程部署信息
     * @param: null
     * @return: null
     * 当前流程如果没有全部完成，想要删除的话需要使用特殊方式，原理就是 级联删除
     */
    public boolean deleteDeployMentCascade(String deploymentId){
        try {
            // 级联删除，流程没有完全完成也能删除
            repositoryService.deleteDeployment(deploymentId,true);
            logInfo(deploymentId,"删除任务",SUCCESS,"联级删除任务成功");
            return true;
        }catch (Exception e){
            logError(deploymentId,"删除任务",FAIL,e.getMessage());
            return false;
        }
    }

    /**
     * @author: yuqn
     * @Date: 2024/5/5 23:41
     * @description:
     * 删除流程部署信息
     * @param: null
     * @return: null
     * 当前流程如果没有全部完成，想要删除的话需要使用特殊方式，原理就是 级联删除
     */
    public void deleteDeployMentNoCascade(String deploymentId){
        // 非级联删除
        repositoryService.deleteDeployment(deploymentId);
    }

}
