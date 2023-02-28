package com.cheche.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author cheche
 * @date 2023/2/8
 */
@Slf4j
@Component
public class RedisLock {

  @Autowired
  StringRedisTemplate template;

  public boolean tryLock(String key, String value, int expireTime, TimeUnit timeUnit) {
    Boolean flag = template.opsForValue().setIfAbsent(key, value, expireTime, timeUnit);
    if (flag == null || !flag) {
      log.info("Request lock: " + key + "," + value + " failure");
      return false;
    }
    log.error("Request lock: " + key + "," + value + " success");
    return true;
  }

  public void unLock(String key, String value) {
    String script = "if redis.call('get', KEYS[1]) == KEYS[2] then return redis.call('del', KEYS[1]) else return 0 end";
    RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
    Long result = template.execute(redisScript, Arrays.asList(key, value));
    if (result == null || result == 0) {
      log.info("Release lock: " + key + "," + value + " failure, The lock does not exist or has expired");
    } else {
      log.info("Release lock: " + key + "," + value + " success");
    }
  }

}
