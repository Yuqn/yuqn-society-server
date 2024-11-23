package com.yuqn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuqn.entity.LoginUser;
import com.yuqn.entity.User;
import com.yuqn.mapper.MenuMapper;
import com.yuqn.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author: yuqn
 * @Date: 2024/5/21 23:34
 * @description:
 * secutiry类
 * 重写登录验证方法，常规方法是 loadUserByUsername 接收传递的参数，进行security自定义的校验
 * 这里重写 loadUserByUsername 方法，自定义校验方法（如查询数据库是否存在此人）
 * @version: 1.0
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @author: yuqn
     * @Date: 2024/11/24 0:30
     * @description:
     * 根据用户名查询到用户信息，并且映射到UserDetails
     * @param: null
     * @return: null
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        System.out.println("username==" + username);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println("user = " + user);
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
