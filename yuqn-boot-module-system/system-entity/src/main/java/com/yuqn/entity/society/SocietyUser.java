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
 * @TableName society_user
 */
@TableName(value ="society_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyUser implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 系统用户id
     */
    private String sysUserId;

    /**
     * 身份证
     */
    private String card;

    /**
     * 所在学院
     */
    private String societyCollegeId;

    /**
     * 所在专业
     */
    private String societyMajorId;

    /**
     * 所在班级
     */
    private String societyClassesId;

    /**
     * 年级
     */
    private String societyGradeId;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 角色状态（0正常 1停用）
     */
    private StatusEnum status;

    /**
     * del_flag
     */
    private DelFalgEnum delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

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
        SocietyUser other = (SocietyUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSysUserId() == null ? other.getSysUserId() == null : this.getSysUserId().equals(other.getSysUserId()))
            && (this.getCard() == null ? other.getCard() == null : this.getCard().equals(other.getCard()))
            && (this.getSocietyCollegeId() == null ? other.getSocietyCollegeId() == null : this.getSocietyCollegeId().equals(other.getSocietyCollegeId()))
            && (this.getSocietyMajorId() == null ? other.getSocietyMajorId() == null : this.getSocietyMajorId().equals(other.getSocietyMajorId()))
            && (this.getSocietyClassesId() == null ? other.getSocietyClassesId() == null : this.getSocietyClassesId().equals(other.getSocietyClassesId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSysUserId() == null) ? 0 : getSysUserId().hashCode());
        result = prime * result + ((getCard() == null) ? 0 : getCard().hashCode());
        result = prime * result + ((getSocietyCollegeId() == null) ? 0 : getSocietyCollegeId().hashCode());
        result = prime * result + ((getSocietyMajorId() == null) ? 0 : getSocietyMajorId().hashCode());
        result = prime * result + ((getSocietyClassesId() == null) ? 0 : getSocietyClassesId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", studentId=").append(sysUserId);
        sb.append(", card=").append(card);
        sb.append(", societyCollegeId=").append(societyCollegeId);
        sb.append(", societyMajorId=").append(societyMajorId);
        sb.append(", societyClassesId=").append(societyClassesId);
        sb.append(", societyGradeId=").append(societyGradeId);
        sb.append(", status=").append(status);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
