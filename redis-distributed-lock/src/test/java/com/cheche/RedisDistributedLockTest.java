package com.cheche;

import lombok.extern.slf4j.Slf4j;
import com.cheche.common.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author cheche
 * @date 2023/2/8
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisDistributedLockApplication.class)
public class RedisDistributedLockTest {

  @Resource
  RedisLock redisLock;
  public static final String LOCK_NAME = "name";
  public static final String CLIENT_A = "a";
  public static final String CLIENT_B = "b";
  public static final String CLIENT_C = "c";
  public static final int EXPIRE_TIME = 5;


  @Test
  public void test1() {

    //客户端a
    boolean lockResultA = redisLock.tryLock(LOCK_NAME, CLIENT_A, EXPIRE_TIME, TimeUnit.SECONDS);
    if (lockResultA) {
      try {
        System.out.println("clinet A");
        //模拟客户端a的操作耗时
        Thread.sleep(2 * 1000);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        redisLock.unLock(LOCK_NAME, CLIENT_A);
      }
    }

    //客户端b
    boolean lockResultB = redisLock.tryLock(LOCK_NAME, CLIENT_B, EXPIRE_TIME, TimeUnit.SECONDS);
    if (lockResultB) {
      try {
        System.out.println("client B");
        //模拟客户端b的操作耗时
        Thread.sleep(2 * 1000);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        redisLock.unLock(LOCK_NAME, CLIENT_B);
      }
    }

    //客户端c
    boolean lockResultC = redisLock.tryLock(LOCK_NAME, CLIENT_C, EXPIRE_TIME, TimeUnit.SECONDS);
    if (lockResultC) {
      try {
        System.out.println("client B");
        //模拟客户端b的操作耗时
        Thread.sleep(2 * 1000);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        redisLock.unLock(LOCK_NAME, CLIENT_C);
      }
    }
  }

}
