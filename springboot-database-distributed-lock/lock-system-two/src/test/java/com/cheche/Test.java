package com.cheche;

import lombok.extern.slf4j.Slf4j;
import com.cheche.service.LockService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author cheche
 * @date 2023/2/7
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LockSystemTwoApplication.class)
public class Test {

  @Resource
  LockService lockService;

  @org.junit.Test
  public void testLock() {
    log.info("system2准备获取锁");
    lockService.lock("key", 6000 * 1000);
    try {
      //模拟业务耗时
      Thread.sleep(4 * 1000);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      lockService.unLock("key");
    }
  }

}
