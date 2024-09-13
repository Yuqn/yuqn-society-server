package com.yuqn.service.test.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuqn.dao.test.TestMapper;
import com.yuqn.entity.test.YuqnTest;
import com.yuqn.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private TestMapper testMapper;

    @Autowired
    public void setTestMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    public String getMessage() {
        QueryWrapper<YuqnTest> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(testMapper.selectList(objectQueryWrapper));
        return "获取信息成功";
    }

    @Override
    public List<YuqnTest> getAllMessage() {
        System.out.println("使用xml配置文件读取数据成功");
        return testMapper.getAllTset();
    }
}
