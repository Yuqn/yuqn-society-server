package com.yuqn.dao.society;

import com.yuqn.entity.society.SocietyBody;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuqn.vo.SocietyListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author yuqn
* @description 针对表【society_body】的数据库操作Mapper
* @createDate 2024-09-16 16:03:30
* @Entity com.yuqn.entity.society.SocietyBody
*/
@Mapper
public interface SocietyBodyMapper extends BaseMapper<SocietyBody> {
    // 查看社团基本数据
    List<SocietyListVo> getSocietyList(String societyName);
    // 查看社团人数
    int getSocietyUserCount(String societyId);
    // 查看社团部门数
    int getSocietyDepartmentCount(String societyId);
}




