package com.yuqn.dao.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuqn.entity.test.YuqnTest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<YuqnTest> {
    List<YuqnTest> getAllTset();
}
