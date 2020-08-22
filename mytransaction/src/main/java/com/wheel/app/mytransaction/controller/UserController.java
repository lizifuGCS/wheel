package com.wheel.app.mytransaction.controller;

import com.wheel.app.mytransaction.bean.User;
import com.wheel.app.mytransaction.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/22 11:00
 * @Version 1.0
 */
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/insert/user")
    public void addUser(@RequestBody User user) {
        userDao.addUser(user);

    }
}
