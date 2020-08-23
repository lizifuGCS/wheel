package com.wheel.app.redislistener.one.handle;

import com.wheel.app.redislistener.one.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/23 11:36
 * @Version 1.0
 */
@Component
//@EnableScheduling // 此注解必加,必须要加，重中之重
public class PushMessage {

    private final static Logger logger =  LoggerFactory.getLogger(PushMessage.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0/3 * * * * ?") //每5秒执行一次
    public void pushInfo() {
        logger.info("进入定时任务======");
        User lizifu = new User("lizifu", "123456789");
        logger.info("进入定时任务======，推送消息[{}]",lizifu);
        redisTemplate.convertAndSend("myChannel", lizifu);

    }
}
