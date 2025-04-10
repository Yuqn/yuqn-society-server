package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyUserListVo {
    private String userId;
    private String userName;
    private String nickName;
    private String userCollege;
    private String userMajor;
    private String userGrade;
    private String isJoin;
}
