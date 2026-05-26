package com.coldchain.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coldchain.server.entity.TemperatureRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemperatureRecordMapper extends BaseMapper<TemperatureRecord> {
}
