package com.cheche.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.cheche.domain.Lock;

/**
 * @author cheche
 * @date 2023/2/7
 */
@Mapper
public interface LockMapper extends BaseMapper<Lock> {
}
