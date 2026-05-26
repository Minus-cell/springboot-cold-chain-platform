package com.coldchain.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("vehicle_info")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String vehicleNo;

    private String vehicleType;

    private String driverName;

    private String driverPhone;

    private String temperatureRange;

    private BigDecimal maxLoad;

    private Integer status;

    private BigDecimal currentLat;

    private BigDecimal currentLng;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
