package com.yuqn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuqn.entity.LoginUser;
import com.yuqn.entity.User;
import com.yuqn.mapper.MenuMapper;
import com.yuqn.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: yuqn
 * @Date: 2024/11/26 11:09
 * @description:
 * 电话号码查询用户，封装到UserDetails，用于CustomAuthenticationProvider验证
 * @version: 1.0
 */
@Service
public class PhoneNumberUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @author: yuqn
     * @Date: 2024/11/26 11:04
     * @description:
     * 自定义手机号码验证
     * @param: null
     * @return: null
     */
    public UserDetails loadUserByPhoneNumber(String phoneNumber){
        // 根据手机号码查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phoneNumber);
        User user = userMapper.selectOne(queryWrapper);
        // 如果没有用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }
        // 查询对应权限
        // List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        list.add(user.getRoles());
        System.out.println("list = " + list);

        // 将user封装到 LoginUser 返回，security 会根据 LoginUser 获取账号密码进行校验，数据库中的密码需要使用{noop}表示明文保存的，不然会报错，因为security使用的加密校验
        return new LoginUser(user,list);
    }
}
