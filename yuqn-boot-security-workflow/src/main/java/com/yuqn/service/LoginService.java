package com.yuqn.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuqn.entity.User;
import com.yuqn.vo.Result;
import org.apache.ibatis.annotations.Mapper;

public interface LoginService{
    Result login(User user);
    Result logout();
}
