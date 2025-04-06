package com.hbox.ecom_cart.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisonConfig {

    @Bean
    public RedissonClient redissonClient()
    {

        return Redisson.create();
    }
}
