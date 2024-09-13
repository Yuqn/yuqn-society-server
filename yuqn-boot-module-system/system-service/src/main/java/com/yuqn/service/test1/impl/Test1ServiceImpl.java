package com.yuqn.service.test1.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuqn.dao.test1.Test1Mapper;
import com.yuqn.entity.test.YuqnTest;
import com.yuqn.service.test1.Test1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Test1ServiceImpl implements Test1Service {

    private Test1Mapper test1Mapper;

    @Autowired
    public void setTest1Mapper(Test1Mapper test1Mapper) {
        this.test1Mapper = test1Mapper;
    }

    @Override
    public String message() {
        QueryWrapper<YuqnTest> objectQueryWrapper = new QueryWrapper<>();
        return test1Mapper.selectCount(objectQueryWrapper).toString();
    }
}
