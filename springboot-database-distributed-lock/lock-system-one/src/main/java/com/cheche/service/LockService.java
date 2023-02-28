package com.cheche.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheche.domain.Lock;

/**
 * @author cheche
 * @date 2023/2/7
 */
public interface LockService extends IService<Lock> {

  /**
   * 阻塞获取分布式锁
   *
   * @param name         锁名称
   * @param survivalTime 存活时间
   */
  void lock(String name, int survivalTime);

  /**
   * 释放锁
   *
   * @param name 锁名称
   */
  public void unLock(String name);

}
