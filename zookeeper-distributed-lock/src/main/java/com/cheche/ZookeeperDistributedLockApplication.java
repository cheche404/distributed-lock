package com.cheche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author cheche
 * @date 2023/2/10
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ZookeeperDistributedLockApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZookeeperDistributedLockApplication.class);
  }
}