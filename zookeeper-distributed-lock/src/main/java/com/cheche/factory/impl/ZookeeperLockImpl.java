package com.cheche.factory.impl;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import com.cheche.factory.ZookeeperLock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZookeeperLockImpl
 *
 * @author cheche
 * @date 2023/2/10
 */
public class ZookeeperLockImpl implements ZookeeperLock {

  private String path;

  private String name;

  private String lockPath ;

  private ZooKeeper zk;

  private int state ;

  public ZookeeperLockImpl(String path, String name, ZooKeeper zk) {
    this.path = path;
    this.name = name;
    this.zk = zk;
    this.state=0;
  }

  @Override
  public boolean lock() {
    boolean flag= lockInternal();
    if(flag){
      state++;
    }
    return flag;
  }

  private boolean lockInternal(){
    try {
      String result = zk.create(getPath(), "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
      this.lockPath = result;
      List<String> waits = zk.getChildren(path, false);
      Collections.sort(waits);
      String[] paths=result.split("/");
      String curNodeName =  paths[paths.length-1];
      if (waits.get(0).equalsIgnoreCase(curNodeName)) {
        return true;
      }
      CountDownLatch latch = new CountDownLatch(1);
      for (int i = 0; i < waits.size(); i++) {
        String cur = waits.get(i);
        if (!cur.equalsIgnoreCase(curNodeName)) {
          continue;
        }
        String prePath = path+"/"+waits.get(i - 1);
        zk.exists(prePath, event -> latch.countDown());
        break;
      }
      latch.await();
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  private String getPath() {
    return path+"/"+name;
  }

  @Override
  public boolean unlock() {
    if(state>1){
      state--;
      return true;
    }
    try {
      Stat stat=zk.exists(lockPath,false);
      int version= stat.getVersion();
      zk.delete(lockPath,version);
      state--;
      return true;
    } catch (Exception e) {
      System.out.println("unlock:"+lockPath+" ,exception,");
    }
    return false;
  }

}

