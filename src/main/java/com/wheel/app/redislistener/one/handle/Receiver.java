package com.wheel.app.redislistener.one.handle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheel.app.redislistener.one.pojo.User;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/23 10:49
 * @Version 1.0
 */
@Service
public class Receiver {

    public void receiveMessage(String message) {
        System.out.println("topic1接收到消息");
    }

    public void handleMessage(String message) {
        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(User.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        User user = (User)seria.deserialize(message.getBytes());
        System.out.println("接收到消息信息"+user.getUserName()+"信息"+user);
    }

}
