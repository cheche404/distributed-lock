package com.cheche.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cheche
 * @date 2023/2/7
 */
@Data
@NoArgsConstructor
@TableName(value = "t_lock")
public class Lock {

  /**
   * 自增序号
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 锁名称
   */
  private String name;

  /**
   * 存活时间，单位ms
   */
  private int survivalTime;

  /**
   * 锁创建的时间
   */
  private Date createTime;

  /**
   * 线程名称
   */
  private String ThreadName;

}
