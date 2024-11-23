package com.yuqn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: yuqn
 * @Date: 2024/11/20 9:01
 * @description:
 * 院系、专业、班级vo
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeMajorClass implements Serializable {
    /**
     * 学院名字信息
     */
    private String text;
    /**
     * 学院value值
     */
    private String value;
    /**
     * 子记录
     */
    private List<CollegeMajorClass> children;
}
