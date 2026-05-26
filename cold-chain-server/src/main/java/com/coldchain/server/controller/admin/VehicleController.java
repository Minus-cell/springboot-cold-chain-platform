package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coldchain.server.common.PageResult;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Vehicle;
import com.coldchain.server.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/vehicle")
public class VehicleController {

    @Autowired
    private VehicleMapper vehicleMapper;

    @GetMapping("/list")
    public Result<PageResult<Vehicle>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String vehicleNo,
            @RequestParam(required = false) Integer status) {

        Page<Vehicle> page = new Page<>(current, size);
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();

        if (vehicleNo != null && !vehicleNo.isEmpty()) {
            wrapper.like(Vehicle::getVehicleNo, vehicleNo);
        }
        if (status != null) {
            wrapper.eq(Vehicle::getStatus, status);
        }

        wrapper.orderByDesc(Vehicle::getCreateTime);
        Page<Vehicle> result = vehicleMapper.selectPage(page, wrapper);

        return Result.success(PageResult.ok(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<Vehicle> getById(@PathVariable Long id) {
        Vehicle vehicle = vehicleMapper.selectById(id);
        return Result.success(vehicle);
    }

    @PostMapping
    public Result<Vehicle> create(@RequestBody Vehicle vehicle) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vehicle::getVehicleNo, vehicle.getVehicleNo());
        if (vehicleMapper.selectCount(wrapper) > 0) {
            return Result.error("车牌号已存在");
        }
        vehicle.setCreateTime(LocalDateTime.now());
        vehicle.setUpdateTime(LocalDateTime.now());
        vehicleMapper.insert(vehicle);
        return Result.success("添加成功", vehicle);
    }

    @PutMapping
    public Result<Void> update(@RequestBody Vehicle vehicle) {
        vehicle.setUpdateTime(LocalDateTime.now());
        vehicleMapper.updateById(vehicle);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        vehicleMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/location")
    public Result<List<Vehicle>> getLocations() {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Vehicle::getCurrentLat);
        wrapper.isNotNull(Vehicle::getCurrentLng);
        List<Vehicle> vehicles = vehicleMapper.selectList(wrapper);
        return Result.success(vehicles);
    }
}
