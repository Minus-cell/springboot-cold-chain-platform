package com.coldchain.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coldchain.server.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
}
