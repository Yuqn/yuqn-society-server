package com.yuqn.controller.society;

import com.yuqn.service.society.ExamineAndApproveService;
import com.yuqn.vo.ExamineAndApproveVo;
import com.yuqn.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/society/examineAndApprove")
@Tag(name = "审批模块")
public class ExamineAndApproveController {
    @Autowired
    private ExamineAndApproveService examineAndApproveService;
    /**
     * @author: yuqn
     * @Date: 2025/3/6 20:46
     * @description:
     * 获取教师当前待审批记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getExamineAndApprove")
    @Operation(summary = "获取教师当前待审批记录")
    public Result getExamineAndApprove(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getExamineAndApproveForTeacher(token);
        return result;
    }
    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 驳回审批
     * @param: null
     * @return: null
     */
    @PostMapping("/rejectExamineAndApprove")
    @Operation(summary = "驳回审批")
    public Result rejectExamineAndApprove(@RequestHeader("Token") String token, @RequestBody ExamineAndApproveVo examineAndApproveVo){
        Result result = examineAndApproveService.rejectExamineAndApprove(token,examineAndApproveVo);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 审批通过
     * @param: null
     * @return: null
     */
    @PostMapping("/passExamineAndApprove")
    @Operation(summary = "审批通过")
    public Result passExamineAndApprove(@RequestHeader("Token") String token, @RequestBody ExamineAndApproveVo examineAndApproveVo){
        Result result = examineAndApproveService.passExamineAndApprove(token,examineAndApproveVo);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取拒绝同意记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getRejectList")
    @Operation(summary = "获取拒绝/同意记录")
    public Result getRejectList(@RequestHeader("Token") String token,@RequestParam String typeId){
        Result result = examineAndApproveService.getRejectList(token,typeId);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取头部信息
     * @param: null
     * @return: null
     */
    @GetMapping("/getAllExamineAndApprove")
    @Operation(summary = "获取头部信息")
    public Result getAllExamineAndApprove(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getAllExamineAndApprove(token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人待审批记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordByUserId")
    @Operation(summary = "获取个人待审批记录")
    public Result getApplicationRecordByUserId(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getApplicationRecordByUserId(token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人已通过的审批记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordPassByUserId")
    @Operation(summary = "获取个人待审批记录")
    public Result getApplicationRecordPassByUserId(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getApplicationRecordPassByUserId(token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人待审批详细记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordDetails")
    @Operation(summary = "获取详细记录")
    public Result getApplicationRecordDetails(@RequestHeader("Token") String token,@RequestParam String processInstanceId,@RequestParam String businessKey){
        Result result = examineAndApproveService.getApplicationRecordDetails(token,processInstanceId,businessKey);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人已通过详细记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordPassDetails")
    @Operation(summary = "获取已通过详细记录")
    public Result getApplicationRecordPassDetails(@RequestHeader("Token") String token,@RequestParam String processInstanceId,@RequestParam String businessKey){
        Result result = examineAndApproveService.getApplicationRecordPassDetails(token,processInstanceId,businessKey);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人被拒绝的审批记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordNoPassByUserId")
    @Operation(summary = "获取个人被拒绝的审批记录")
    public Result getApplicationRecordNoPassByUserId(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getApplicationRecordNoPassByUserId(token);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取被驳回详细记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordNoPassDetails")
    @Operation(summary = "获取被驳回详细记录")
    public Result getApplicationRecordNoPassDetails(@RequestHeader("Token") String token,@RequestParam String processInstanceId,@RequestParam String businessKey){
        Result result = examineAndApproveService.getApplicationRecordNoPassDetails(token,processInstanceId,businessKey);
        return result;
    }

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:05
     * @description:
     * 获取个人申请头部记录
     * @param: null
     * @return: null
     */
    @GetMapping("/getApplicationRecordTop")
    @Operation(summary = "获取被驳回详细记录")
    public Result getApplicationRecordTop(@RequestHeader("Token") String token){
        Result result = examineAndApproveService.getApplicationRecordTop(token);
        return result;
    }

}
