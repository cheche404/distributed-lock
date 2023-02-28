package com.cheche.factory;

/**
 * 锁工厂
 *
 * @author cheche
 * @date 2023/2/10
 */
public interface ZookeeperLockFactory {

  /**
   * 拿到对象
   * @param key 锁 key
   * @return 锁对象
   */
  ZookeeperLock getLock(String key);

}
