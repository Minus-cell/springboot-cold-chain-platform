package com.coldchain.server.controller.portal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Order;
import com.coldchain.server.entity.TemperatureRecord;
import com.coldchain.server.entity.Vehicle;
import com.coldchain.server.mapper.OrderMapper;
import com.coldchain.server.mapper.TemperatureRecordMapper;
import com.coldchain.server.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/portal/track")
public class TrackController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TemperatureRecordMapper temperatureRecordMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @GetMapping("/{orderNo}")
    public Result<Map<String, Object>> track(@PathVariable String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order == null) {
            return Result.error("订单不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);

        List<Map<String, Object>> timeline = generateTimeline(order);
        result.put("timeline", timeline);

        if (order.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.selectById(order.getVehicleId());
            result.put("vehicle", vehicle);
        }

        return Result.success(result);
    }

    @GetMapping("/{orderNo}/temperature")
    public Result<List<TemperatureRecord>> getTemperature(@PathVariable String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order == null) {
            return Result.error("订单不存在");
        }

        LambdaQueryWrapper<TemperatureRecord> tempWrapper = new LambdaQueryWrapper<>();
        tempWrapper.eq(TemperatureRecord::getOrderId, order.getId());
        tempWrapper.orderByAsc(TemperatureRecord::getRecordTime);
        List<TemperatureRecord> records = temperatureRecordMapper.selectList(tempWrapper);

        return Result.success(records);
    }

    @GetMapping("/{orderNo}/location")
    public Result<Map<String, Object>> getLocation(@PathVariable String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order == null) {
            return Result.error("订单不存在");
        }

        Map<String, Object> location = new HashMap<>();

        if (order.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.selectById(order.getVehicleId());
            if (vehicle != null) {
                location.put("lat", vehicle.getCurrentLat());
                location.put("lng", vehicle.getCurrentLng());
                location.put("vehicleNo", vehicle.getVehicleNo());
            }
        }

        return Result.success(location);
    }

    private List<Map<String, Object>> generateTimeline(Order order) {
        List<Map<String, Object>> timeline = new ArrayList<>();

        Map<String, Object> created = new HashMap<>();
        created.put("status", "已下单");
        created.put("time", order.getCreateTime());
        created.put("description", "订单已创建，等待取件");
        timeline.add(created);

        if (order.getOrderStatus() >= 1) {
            Map<String, Object> inTransit = new HashMap<>();
            inTransit.put("status", "运输中");
            inTransit.put("time", order.getUpdateTime());
            inTransit.put("description", "货物正在运输中");
            timeline.add(inTransit);
        }

        if (order.getOrderStatus() >= 2) {
            Map<String, Object> delivering = new HashMap<>();
            delivering.put("status", "派送中");
            delivering.put("time", order.getUpdateTime());
            delivering.put("description", "货物正在派送中");
            timeline.add(delivering);
        }

        if (order.getOrderStatus() == 3) {
            Map<String, Object> completed = new HashMap<>();
            completed.put("status", "已签收");
            completed.put("time", order.getUpdateTime());
            completed.put("description", "货物已签收");
            timeline.add(completed);
        }

        if (order.getOrderStatus() == 4) {
            Map<String, Object> cancelled = new HashMap<>();
            cancelled.put("status", "已取消");
            cancelled.put("time", order.getUpdateTime());
            cancelled.put("description", "订单已取消");
            timeline.add(cancelled);
        }

        return timeline;
    }
}
