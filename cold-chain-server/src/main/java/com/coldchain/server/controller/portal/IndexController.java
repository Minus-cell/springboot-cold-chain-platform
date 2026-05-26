package com.coldchain.server.controller.portal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Order;
import com.coldchain.server.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portal/index")
public class IndexController {

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@RequestParam Long userId) {
        Map<String, Object> stats = new HashMap<>();

        Long totalCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)).longValue();

        Long inTransitCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .in(Order::getOrderStatus, 1, 2)).longValue();

        Long completedCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(Order::getOrderStatus, 3)).longValue();

        stats.put("totalCount", totalCount);
        stats.put("inTransitCount", inTransitCount);
        stats.put("completedCount", completedCount);

        return Result.success(stats);
    }

    @GetMapping("/news")
    public Result<List<Map<String, String>>> getNews() {
        return Result.success(Collections.emptyList());
    }
}
