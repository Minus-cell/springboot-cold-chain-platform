package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coldchain.server.common.PageResult;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Order;
import com.coldchain.server.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    private static final DateTimeFormatter ORDER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @GetMapping("/list")
    public Result<PageResult<Order>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        if (orderStatus != null) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }
        if (startTime != null && !startTime.isEmpty()) {
            wrapper.ge(Order::getCreateTime, startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            wrapper.le(Order::getCreateTime, endTime);
        }

        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(page, wrapper);

        return Result.success(PageResult.ok(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        return Result.success(order);
    }

    @PostMapping
    public Result<Order> create(@RequestBody Order order) {
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);
        return Result.success("创建成功", order);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Order order) {
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        order.setOrderStatus(request.get("status"));
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("状态更新成功");
    }

    private String generateOrderNo() {
        String prefix = "DD" + LocalDateTime.now().format(ORDER_FORMATTER);
        long count = orderMapper.selectCount(null) + 1;
        return prefix + String.format("%04d", count);
    }
}
