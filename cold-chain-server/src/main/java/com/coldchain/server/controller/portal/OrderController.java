package com.coldchain.server.controller.portal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coldchain.server.common.PageResult;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Order;
import com.coldchain.server.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController("portalOrderController")
@RequestMapping("/api/portal/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    private static final DateTimeFormatter ORDER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @PostMapping("/create")
    public Result<Order> create(@RequestBody Order order) {
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        if (order.getGoodsWeight() != null && order.getGoodsVolume() != null) {
            BigDecimal price = calculatePrice(order.getGoodsWeight(), order.getGoodsVolume());
            order.setTotalPrice(price);
        }

        orderMapper.insert(order);
        return Result.success("下单成功", order);
    }

    @GetMapping("/list")
    public Result<PageResult<Order>> list(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer orderStatus) {

        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);

        if (orderStatus != null) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }

        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(page, wrapper);

        return Result.success(PageResult.ok(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/{orderNo}")
    public Result<Order> getByOrderNo(@PathVariable String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order);
    }

    @PutMapping("/cancel/{orderNo}")
    public Result<Void> cancel(@PathVariable String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order == null) {
            return Result.error("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            return Result.error("只有待取件的订单可以取消");
        }

        order.setOrderStatus(4);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        return Result.success("取消成功");
    }

    private String generateOrderNo() {
        String prefix = "DD" + LocalDateTime.now().format(ORDER_FORMATTER);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Order::getOrderNo, prefix);
        long count = orderMapper.selectCount(wrapper) + 1;
        return prefix + String.format("%04d", count);
    }

    private BigDecimal calculatePrice(BigDecimal weight, BigDecimal volume) {
        BigDecimal weightPrice = weight.multiply(new BigDecimal("2.00"));
        BigDecimal volumePrice = volume.multiply(new BigDecimal("500.00"));
        return weightPrice.max(volumePrice).add(new BigDecimal("100.00"));
    }
}
