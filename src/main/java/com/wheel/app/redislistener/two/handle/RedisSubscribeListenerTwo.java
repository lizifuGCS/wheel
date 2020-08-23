package com.wheel.app.redislistener.two.handle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheel.app.redislistener.two.pojo.UserTwo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/23 10:49
 * @Version 1.0
 */
@Service
public class RedisSubscribeListenerTwo implements MessageListener {

    private final static Logger logger = LoggerFactory.getLogger(RedisSubscribeListenerTwo.class);


    @Override
    public void onMessage(Message message, byte[] pattern) {
        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(UserTwo.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        UserTwo user = (UserTwo) seria.deserialize(message.getBody());
        logger.info("监听导消息==》[{}]", user);
    }
}
