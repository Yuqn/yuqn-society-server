package com.yuqn.service.society;

import com.yuqn.vo.ExamineAndApproveVo;
import com.yuqn.vo.Result;

public interface ExamineAndApproveService {
    /**
     * @author: yuqn
     * @Date: 2025/3/7 22:01
     * @description:
     * 获取教师待审批记录
     * @param: null
     * @return: null
     */
    Result getExamineAndApproveForTeacher(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/18 0:08
     * @description:
     * 驳回审批
     * @param: null
     * @return: null
     */
    Result rejectExamineAndApprove(String token, ExamineAndApproveVo examineAndApproveVo);

    /**
     * @author: yuqn
     * @Date: 2025/3/18 17:24
     * @description:
     * 审批通过
     * @param: null
     * @return: null
     */
    Result passExamineAndApprove(String token, ExamineAndApproveVo examineAndApproveVo);

    /**
     * @author: yuqn
     * @Date: 2025/3/18 18:58
     * @description:
     * 获取驳回记录
     * @param: null
     * @return: null
     */
    Result getRejectList(String token,String typeId);

    /**
     * @author: yuqn
     * @Date: 2025/3/18 21:15
     * @description:
     * 获取头部信息
     * @param: null
     * @return: null
     */
    Result getAllExamineAndApprove(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/20 17:31
     * @description:
     * 获取个人待审批记录
     * @param: null
     * @return: null
     */
    Result getApplicationRecordByUserId(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/20 18:35
     * @description:
     * 获取流程详细信息
     * @param: null
     * @return: null
     */
    Result getApplicationRecordDetails(String token, String processInstanceId,String businessKey);

    /**
     * @author: yuqn
     * @Date: 2025/3/21 2:40
     * @description:
     * 获取个人申请通过的记录
     * @param: null
     * @return: null
     */
    Result getApplicationRecordPassByUserId(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/21 2:40
     * @description:
     * 获取流程已经通过详细信息
     * @param: null
     * @return: null
     */
    Result getApplicationRecordPassDetails(String token, String processInstanceId, String businessKey);

    /**
     * @author: yuqn
     * @Date: 2025/3/21 16:42
     * @description:
     * 获取个人审批不通过的记录
     * @param: null
     * @return: null
     */
    Result getApplicationRecordNoPassByUserId(String token);

    /**
     * @author: yuqn
     * @Date: 2025/3/21 16:53
     * @description:
     * 获取被驳回的信息
     * @param: null
     * @return: null
     */
    Result getApplicationRecordNoPassDetails(String token, String processInstanceId, String businessKey);

    /**
     * @author: yuqn
     * @Date: 2025/3/21 17:16
     * @description:
     * 获取个人申请页面头部记录
     * @param: null
     * @return: null
     */
    Result getApplicationRecordTop(String token);
}
