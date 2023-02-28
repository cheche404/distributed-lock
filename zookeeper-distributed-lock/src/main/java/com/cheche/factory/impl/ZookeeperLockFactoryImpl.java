package com.cheche.factory.impl;

import lombok.Getter;
import lombok.Setter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import com.cheche.factory.ZookeeperLock;
import com.cheche.factory.ZookeeperLockFactory;

/**
 * @author cheche
 * @date 2023/2/10
 */
@Getter
@Setter
public class ZookeeperLockFactoryImpl implements ZookeeperLockFactory {

  private static final String ZOOKEEPER_LOCK_ROOT_PATH="/zklock/locks";
  private ZooKeeper zooKeeper;

  @Override
  public ZookeeperLock getLock(String key) {
    String path = getPath(key);
    String result = null;
    try {
      result = zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    } catch (Exception e) {

    } finally {
      System.out.println(Thread.currentThread().getName() + result);
      try {
        Stat stat= zooKeeper.exists(path, false);
        if(stat == null){
          return null;
        }
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
    return new ZookeeperLockImpl(path, key, zooKeeper);
  }

  private String getPath(String key) {
    return ZOOKEEPER_LOCK_ROOT_PATH + "/" + key;
  }
}
