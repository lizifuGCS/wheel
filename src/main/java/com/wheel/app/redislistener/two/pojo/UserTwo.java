package com.wheel.app.redislistener.two.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/23 10:59
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTwo implements  Serializable{
    private static final long serialVersionUID = 2L;
    private String userName;
    private String password;
}
