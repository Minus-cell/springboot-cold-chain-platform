package com.coldchain.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coldchain.server.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}