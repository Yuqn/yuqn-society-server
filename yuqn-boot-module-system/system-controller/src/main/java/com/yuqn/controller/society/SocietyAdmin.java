package com.yuqn.controller.society;

import com.yuqn.dao.society.SocietyAdminMapper;
import com.yuqn.enums.DelFalgEnum;
import com.yuqn.enums.StatusEnum;
import com.yuqn.service.society.SocietyAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "society接口")
public class SocietyAdmin {

    @Autowired
    private SocietyAdminService societyAdminService;

    @Autowired
    private SocietyAdminMapper societyAdminMapper;

    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public String addAdmin(){
        com.yuqn.entity.society.SocietyAdmin societyAdmin = new com.yuqn.entity.society.SocietyAdmin();
        societyAdmin.setNumber("123456");
        societyAdmin.setUsername("老师");
        societyAdmin.setPassword("123456");
        societyAdmin.setStatus(StatusEnum.ENABLE);
        societyAdmin.setDelFlag(DelFalgEnum.NOTDEL);
        System.out.println("societyAdminsocietyAdminsocietyAdminsocietyAdmin"+societyAdmin);
        int save = societyAdminMapper.insert(societyAdmin);
        if (save != 1){
            return "新增失败";
        }
        return "新增成功";
    }
}
