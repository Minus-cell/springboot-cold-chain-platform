package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Order;
import com.coldchain.server.entity.TemperatureRecord;
import com.coldchain.server.entity.Vehicle;
import com.coldchain.server.mapper.OrderMapper;
import com.coldchain.server.mapper.TemperatureRecordMapper;
import com.coldchain.server.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TemperatureRecordMapper temperatureRecordMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        Long todayCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime, LocalDate.now().atStartOfDay())).longValue();

        Long inTransitCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, 1)).longValue();

        Long alarmCount = temperatureRecordMapper.selectCount(new LambdaQueryWrapper<TemperatureRecord>()
                .lt(TemperatureRecord::getTemperature, -25)
                .or()
                .gt(TemperatureRecord::getTemperature, -15)).longValue();

        Long onlineVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<Vehicle>()
                .eq(Vehicle::getStatus, 1)).longValue();

        stats.put("todayOrderCount", todayCount);
        stats.put("inTransitCount", inTransitCount);
        stats.put("alarmCount", alarmCount);
        stats.put("onlineVehicleCount", onlineVehicleCount);

        return Result.success(stats);
    }

    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> getTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            Long count = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                    .between(Order::getCreateTime, date.atStartOfDay(), date.plusDays(1).atStartOfDay())).longValue();

            Map<String, Object> day = new HashMap<>();
            day.put("date", date.toString());
            day.put("count", count);
            trend.add(day);
        }

        return Result.success(trend);
    }

    @GetMapping("/realtime")
    public Result<List<Map<String, Object>>> getRealtime() {
        List<TemperatureRecord> records = temperatureRecordMapper.selectList(
                new LambdaQueryWrapper<TemperatureRecord>()
                        .orderByDesc(TemperatureRecord::getRecordTime)
                        .last("LIMIT 10")
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (TemperatureRecord record : records) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", record.getId());
            map.put("temperature", record.getTemperature());
            map.put("humidity", record.getHumidity());
            map.put("location", record.getLocation());
            map.put("recordTime", record.getRecordTime());
            result.add(map);
        }

        return Result.success(result);
    }
}
