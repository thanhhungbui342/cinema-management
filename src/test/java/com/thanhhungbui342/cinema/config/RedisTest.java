package com.thanhhungbui342.cinema.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest 
@ActiveProfiles("local")
public class RedisTest {
    
    @Autowired 
    private StringRedisTemplate redisTemplate;

    @Test 
    void testRedisCloudConnection() {
        String key = "test:connection";
        String value = "Connect completed";

        redisTemplate.opsForValue().set(key, value);

        String retrievedValue = redisTemplate.opsForValue().get(key);

        assertThat(retrievedValue).isEqualTo(value);

        redisTemplate.delete(key);
    }
}
