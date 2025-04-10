package com.yuqn.vo;

import com.yuqn.entity.society.SocietyBody;
import com.yuqn.entity.society.SysUser;
import com.yuqn.entity.society.SysUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyUpdateVo {
    private SocietyBody societyBody;
    private SysUserRole chairman;
    private List<SysUserRole> viceChairman;
    private String societyId;
    private String societyName;
    private String societyIntroduce;
    private List<UserBodyRole> viceChairmanId;
    private String chairmanId;
}
