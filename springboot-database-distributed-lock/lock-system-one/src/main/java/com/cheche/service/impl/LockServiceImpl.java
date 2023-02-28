package com.cheche.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.cheche.mapper.LockMapper;
import com.cheche.domain.Lock;
import com.cheche.service.LockService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cheche
 * @date 2023/2/7
 */
@Slf4j
@Service
public class LockServiceImpl extends ServiceImpl<LockMapper, Lock> implements LockService {

  @Override
  public void lock(String name, int survivalTime) {
    String threadName = "system1-" + Thread.currentThread().getName();
    while (true) {
      Lock lock = this.lambdaQuery().eq(Lock::getName, name).one();
      if (lock == null) {
        //说明无锁
        Lock lk = new Lock();
        lk.setName(name);
        lk.setSurvivalTime(survivalTime);
        lk.setThreadName(threadName);
        try {
          save(lk);
          log.info(threadName + "获取锁成功");
          return;
        } catch (DuplicateKeyException e) {
          //继续重试
          log.info(threadName + "获取锁失败");
          continue;
        }
      }

      //此时有锁,判断锁是否过期
      Date now = new Date();
      Date expireDate = new Date(lock.getCreateTime().getTime() + lock.getSurvivalTime());
      if (expireDate.before(now)) {
        //锁已经过期
        boolean result = removeById(lock.getId());
        if (result) {
          log.info(threadName + "删除了过期锁");
        }

        //尝试获取锁
        Lock lk = new Lock();
        lk.setName(name);
        lk.setSurvivalTime(survivalTime);
        lk.setThreadName(threadName);
        try {
          save(lk);
          log.info(threadName + "获取锁成功");
          return;
        } catch (DuplicateKeyException e) {
          log.info(threadName + "获取锁失败");
        }
      }
    }

  }

  @Override
  public void unLock(String name) {
    //释放锁的时候，需要注意只能释放自己创建的锁
    String threadName = "system1-" + Thread.currentThread().getName();
    Lock lock = lambdaQuery().eq(Lock::getName, name).eq(Lock::getThreadName, threadName).one();
    if (lock != null) {
      boolean b = removeById(lock.getId());
      if (b) {
        log.info(threadName + "释放了锁");
      } else {
        log.info(threadName + "准备释放锁,但锁过期了,被其他客户端强制释放掉了");
      }
    } else {
      log.info(threadName + "准备释放锁,但锁过期了,被其他客户端强制释放掉了");
    }

  }
}
