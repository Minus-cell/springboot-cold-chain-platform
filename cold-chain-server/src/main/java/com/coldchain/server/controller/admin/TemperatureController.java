package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coldchain.server.common.PageResult;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.TemperatureRecord;
import com.coldchain.server.mapper.TemperatureRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/temperature")
public class TemperatureController {

    @Autowired
    private TemperatureRecordMapper temperatureRecordMapper;

    @GetMapping("/list")
    public Result<PageResult<TemperatureRecord>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long vehicleId) {

        Page<TemperatureRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<TemperatureRecord> wrapper = new LambdaQueryWrapper<>();

        if (orderId != null) {
            wrapper.eq(TemperatureRecord::getOrderId, orderId);
        }
        if (vehicleId != null) {
            wrapper.eq(TemperatureRecord::getVehicleId, vehicleId);
        }

        wrapper.orderByDesc(TemperatureRecord::getRecordTime);
        Page<TemperatureRecord> result = temperatureRecordMapper.selectPage(page, wrapper);

        return Result.success(PageResult.ok(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/{orderId}/chart")
    public Result<Map<String, Object>> getChart(@PathVariable Long orderId) {
        LambdaQueryWrapper<TemperatureRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemperatureRecord::getOrderId, orderId);
        wrapper.orderByAsc(TemperatureRecord::getRecordTime);
        List<TemperatureRecord> records = temperatureRecordMapper.selectList(wrapper);

        List<String> dates = new ArrayList<>();
        List<Double> temperatures = new ArrayList<>();
        List<Double> humidities = new ArrayList<>();

        for (TemperatureRecord record : records) {
            dates.add(record.getRecordTime().toString());
            temperatures.add(record.getTemperature().doubleValue());
            humidities.add(record.getHumidity() != null ? record.getHumidity().doubleValue() : 0);
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("dates", dates);
        chartData.put("temperatures", temperatures);
        chartData.put("humidities", humidities);

        return Result.success(chartData);
    }

    @GetMapping("/alarm")
    public Result<List<TemperatureRecord>> getAlarms() {
        LambdaQueryWrapper<TemperatureRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(TemperatureRecord::getTemperature, -25)
                .or()
                .gt(TemperatureRecord::getTemperature, -15);
        wrapper.orderByDesc(TemperatureRecord::getRecordTime);
        wrapper.last("LIMIT 50");
        List<TemperatureRecord> alarms = temperatureRecordMapper.selectList(wrapper);
        return Result.success(alarms);
    }

    @GetMapping("/latest")
    public Result<List<TemperatureRecord>> getLatest() {
        List<TemperatureRecord> records = temperatureRecordMapper.selectList(
                new LambdaQueryWrapper<TemperatureRecord>()
                        .orderByDesc(TemperatureRecord::getRecordTime)
                        .last("LIMIT 20")
        );
        return Result.success(records);
    }
}
