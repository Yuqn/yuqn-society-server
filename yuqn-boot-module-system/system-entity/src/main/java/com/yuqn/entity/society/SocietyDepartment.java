package com.yuqn.entity.society;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.yuqn.enums.DelFalgEnum;
import com.yuqn.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName society_department
 */
@TableName(value ="society_department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyDepartment implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String societyBodyId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 角色状态（0正常 1停用）
     */
    private StatusEnum status;

    /**
     *
     */
    private DelFalgEnum delFlag;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     *
     */
    private String updateBy;

    /**
     * @author: yuqn
     * @Date: 2025/3/22 21:55
     * @description:
     * 部门邀请码
     * @param: null
     * @return: null
     */
    private String departmentCode;

    /**
     *
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

//    @Override
//    public boolean equals(Object that) {
//        if (this == that) {
//            return true;
//        }
//        if (that == null) {
//            return false;
//        }
//        if (getClass() != that.getClass()) {
//            return false;
//        }
//        SocietyDepartment other = (SocietyDepartment) that;
//        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
//            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
//            && (this.getSocietyBodyId() == null ? other.getSocietyBodyId() == null : this.getSocietyBodyId().equals(other.getSocietyBodyId()))
//            && (this.getIntroduce() == null ? other.getIntroduce() == null : this.getIntroduce().equals(other.getIntroduce()))
//            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
//            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
//            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
//            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
//            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
//            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
//        result = prime * result + ((getSocietyBodyId() == null) ? 0 : getSocietyBodyId().hashCode());
//        result = prime * result + ((getIntroduce() == null) ? 0 : getIntroduce().hashCode());
//        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
//        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
//        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
//        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
//        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
//        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getClass().getSimpleName());
//        sb.append(" [");
//        sb.append("Hash = ").append(hashCode());
//        sb.append(", id=").append(id);
//        sb.append(", name=").append(name);
//        sb.append(", societyBodyId=").append(societyBodyId);
//        sb.append(", introduce=").append(introduce);
//        sb.append(", status=").append(status);
//        sb.append(", delFlag=").append(delFlag);
//        sb.append(", createBy=").append(createBy);
//        sb.append(", createTime=").append(createTime);
//        sb.append(", updateBy=").append(updateBy);
//        sb.append(", updateTime=").append(updateTime);
//        sb.append(", serialVersionUID=").append(serialVersionUID);
//        sb.append("]");
//        return sb.toString();
//    }
}
