package com.wheel.app.mytransaction.dao;

import com.wheel.app.mytransaction.annotation.MyTransaction;
import com.wheel.app.mytransaction.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/22 10:57
 * @Version 1.0
 */
@Component
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MyTransaction
    public void addUser(User user) {
        user.setAge(2222);
        doAddUser(user);
    }

    public void doAddUser(User user) {
        String sql = "insert into aop_user (name,age) values (?,?);";
        jdbcTemplate.update(sql, user.getName(), user.getAge());
        int a = 1 / 0;
    }
}
