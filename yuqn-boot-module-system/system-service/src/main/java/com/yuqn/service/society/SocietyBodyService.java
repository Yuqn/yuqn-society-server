package com.yuqn.service.society;

import com.yuqn.entity.society.SocietyBody;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuqn.vo.Result;

/**
* @author yuqn
* @description 针对表【society_body】的数据库操作Service
* @createDate 2024-09-16 16:03:30
*/
public interface SocietyBodyService extends IService<SocietyBody> {
    public Result getSocietyList(String societyName);
}
