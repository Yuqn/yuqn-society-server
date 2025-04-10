package com.yuqn.vo;

import com.yuqn.entity.society.SocietyBusiness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamineAndApproveVo {
    private SocietyBusiness societyBusiness;
    private String businessKey;
    private String taskId;
    private String processInstanceId;
    private String createName;
    /**
     * @author: yuqn
     * @Date: 2025/3/18 18:46
     * @description:
     * 审批理由
     * @param: null
     * @return: null
     */
    private String reason;
}
