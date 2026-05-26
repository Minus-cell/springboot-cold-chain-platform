package com.coldchain.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coldchain.server.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
