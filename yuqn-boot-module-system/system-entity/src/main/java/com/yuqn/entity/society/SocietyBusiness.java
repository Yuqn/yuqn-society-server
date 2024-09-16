package com.yuqn.entity.society;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @TableName society_business
 */
@TableName(value ="society_business")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyBusiness implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *
     */
    private String userId;

    /**
     *
     */
    private String bodyId;

    /**
     *
     */
    private String department;

    /**
     *
     */
    private String roleId;

    /**
     *
     */
    private String introduce;

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
     *
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否需要审核
     */
    private String isExamine;

    /**
     * sql语句
     */
    private String command;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SocietyBusiness other = (SocietyBusiness) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBodyId() == null ? other.getBodyId() == null : this.getBodyId().equals(other.getBodyId()))
            && (this.getDepartment() == null ? other.getDepartment() == null : this.getDepartment().equals(other.getDepartment()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getIntroduce() == null ? other.getIntroduce() == null : this.getIntroduce().equals(other.getIntroduce()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsExamine() == null ? other.getIsExamine() == null : this.getIsExamine().equals(other.getIsExamine()))
            && (this.getCommand() == null ? other.getCommand() == null : this.getCommand().equals(other.getCommand()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBodyId() == null) ? 0 : getBodyId().hashCode());
        result = prime * result + ((getDepartment() == null) ? 0 : getDepartment().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getIntroduce() == null) ? 0 : getIntroduce().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsExamine() == null) ? 0 : getIsExamine().hashCode());
        result = prime * result + ((getCommand() == null) ? 0 : getCommand().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", bodyId=").append(bodyId);
        sb.append(", department=").append(department);
        sb.append(", roleId=").append(roleId);
        sb.append(", introduce=").append(introduce);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isExamine=").append(isExamine);
        sb.append(", command=").append(command);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
