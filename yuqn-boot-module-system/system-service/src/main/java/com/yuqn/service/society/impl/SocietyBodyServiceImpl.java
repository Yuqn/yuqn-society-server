package com.yuqn.service.society.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuqn.dao.society.SysRoleMapper;
import com.yuqn.entity.society.SocietyBody;
import com.yuqn.service.society.SocietyBodyService;
import com.yuqn.dao.society.SocietyBodyMapper;
import com.yuqn.vo.Result;
import com.yuqn.vo.SocietyListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author yuqn
* @description 针对表【society_body】的数据库操作Service实现
* @createDate 2024-09-16 16:03:30
*/
@Service
public class SocietyBodyServiceImpl extends ServiceImpl<SocietyBodyMapper, SocietyBody>
    implements SocietyBodyService{


    @Autowired
    private SocietyBodyMapper societyBodyMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * @author: yuqn
     * @Date: 2025/3/2 23:06
     * @description:
     * 获取社团列表
     * @param: null
     * @return: null
     */
    @Override
    public Result getSocietyList(String societyName) {
        Result result = null;
        List<SocietyListVo> societyList = societyBodyMapper.getSocietyList(societyName);
        List<SocietyListVo> resultObj = new ArrayList<>();
        for (SocietyListVo societyListVo : societyList){
            SocietyListVo newSocietyListVo = new SocietyListVo();
            newSocietyListVo.setSocietyId( societyListVo.getSocietyId());
            newSocietyListVo.setSocietyName( societyListVo.getSocietyName());
            newSocietyListVo.setSocietyIntroduce( societyListVo.getSocietyIntroduce());
            newSocietyListVo.setCreateBy( societyListVo.getCreateBy());
            newSocietyListVo.setCreateTime( societyListVo.getCreateTime());
            newSocietyListVo.setSocietyByName( societyListVo.getSocietyByName());
            // 查询主席和副主席
            String chairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "4");
            String viceChairman = sysRoleMapper.getSocietyRoleUser(societyListVo.getSocietyId(), "5");
            newSocietyListVo.setChairman(chairman);
            newSocietyListVo.setViceChairman(viceChairman);
            // 查询社团人数
            int societyUserCount = societyBodyMapper.getSocietyUserCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyUserCount(societyUserCount);
            // 查看社团部门数
            int societyDepartmentCount = societyBodyMapper.getSocietyDepartmentCount(societyListVo.getSocietyId());
            newSocietyListVo.setSocietyDepartmentCount(societyDepartmentCount);
            System.out.println("newSocietyListVo = " + newSocietyListVo);
            // 整合数据
            resultObj.add(newSocietyListVo);
            System.out.println("resultObj = " + resultObj);
        }
        System.out.println("----resultObj = " + resultObj);
        result = Result.ok(resultObj);
        return result;
    }
}




