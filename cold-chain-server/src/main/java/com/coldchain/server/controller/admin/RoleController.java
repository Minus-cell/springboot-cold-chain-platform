package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Role;
import com.coldchain.server.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminRoleController")
@RequestMapping("/api/admin/role")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping("/list")
    public Result<List<Role>> list() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Role::getCreateTime);
        List<Role> list = roleMapper.selectList(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleMapper.selectById(id);
        return Result.success(role);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Role role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, role.getRoleCode());
        if (roleMapper.selectCount(wrapper) > 0) {
            return Result.error("角色编码已存在");
        }
        roleMapper.insert(role);
        return Result.success("新增成功");
    }

    @PutMapping
    public Result<Void> update(@RequestBody Role role) {
        roleMapper.updateById(role);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleMapper.deleteById(id);
        return Result.success("删除成功");
    }
}