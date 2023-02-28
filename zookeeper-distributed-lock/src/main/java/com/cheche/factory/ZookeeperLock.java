package com.cheche.factory;

/**
 * @author cheche
 * @date 2023/2/10
 */
public interface ZookeeperLock {

  /**
   * 阻塞获取锁
   *
   * @return true/ false
   */
  boolean lock();


  /**
   * 释放锁
   *
   * @return rue/ false
   */
  boolean unlock();

}
