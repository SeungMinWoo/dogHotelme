package com.green.hoteldog.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    //레디스 설정
    @Value("${spring.data.redis.host}")
    private String host;//레디스 서버 주소
    @Value("${spring.data.redis.port}")
    private int port;// 레디스 포트번호
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        //RedisConnectionFactory 빈 등록
        return new LettuceConnectionFactory(host,port);
    }

    @Bean
    public StringRedisTemplate redisTemplate(){
        //StringRedisTemplate은 key 값과 value 값은 둘다 String으로 받을 때 사용한다.
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }
}
