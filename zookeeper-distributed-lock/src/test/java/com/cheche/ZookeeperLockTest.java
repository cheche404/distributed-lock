package com.cheche;

import org.apache.zookeeper.*;
import com.cheche.factory.ZookeeperLock;
import com.cheche.factory.impl.ZookeeperLockFactoryImpl;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author cheche
 * @date 2023/2/10
 */
public class ZookeeperLockTest {

  public static final Random random  = new Random();

  public static void main(String[] args) throws InterruptedException, KeeperException {
    ZooKeeper zk  = getZkClient();
//    zk.create("/zklock/locks/lock", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

    CountDownLatch latch = new CountDownLatch(1);
    ZookeeperLockFactoryImpl factory = new ZookeeperLockFactoryImpl();
    factory.setZooKeeper(zk);
    for(int i = 0; i < 1; i++){
      int finalI = i;
      Thread t = new Thread(()->{
        exec(factory);
        System.out.println("Thread_"+ finalI +"释放锁完成");
        latch.countDown();
      },"Thread_" + i);
      t.start();
    }
    try {
      latch.await();
      TimeUnit.SECONDS.sleep(5);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("测试完成");
  }


  public static void exec(ZookeeperLockFactoryImpl factory){
    ZookeeperLock lock = factory.getLock("lock");
    System.out.println("Thread:" + Thread.currentThread().getName() + ",尝试获取锁");
    boolean flag = lock.lock();
    System.out.println("Thread:"+Thread.currentThread().getName()+",尝试获取锁,结果："+flag);

    try {
      TimeUnit.MILLISECONDS.sleep(random.nextInt(30));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    lock.unlock();
    System.out.println("Thread:"+Thread.currentThread().getName()+",释放锁锁");


  }




  public static ZooKeeper getZkClient(){
    try {
      return new ZooKeeper("10.81.86.129:2181,10.81.86.129:2182,10.81.86.129:2183", 200000, new Watcher() {
        @Override
        public void process(WatchedEvent event) {
          if(event.getState() == Event.KeeperState.SyncConnected){
            System.out.println("连接成功");
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
