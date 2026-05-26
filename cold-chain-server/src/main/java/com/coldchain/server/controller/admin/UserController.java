package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.Role;
import com.coldchain.server.entity.User;
import com.coldchain.server.entity.UserRole;
import com.coldchain.server.mapper.RoleMapper;
import com.coldchain.server.mapper.UserMapper;
import com.coldchain.server.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController("adminUserController")
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<User> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> result = userMapper.selectPage(pageInfo, wrapper);
        
        for (User user : result.getRecords()) {
            user.setPassword(null);
            List<String> roleNames = getUserRoleNames(user.getId());
            user.setRealName(roleNames.isEmpty() ? "" : String.join(",", roleNames));
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("size", result.getSize());
        
        return Result.success(data);
    }

    private List<String> getUserRoleNames(Long userId) {
        LambdaQueryWrapper<UserRole> urWrapper = new LambdaQueryWrapper<>();
        urWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(urWrapper);
        
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(Role::getId, roleIds);
        List<Role> roles = roleMapper.selectList(roleWrapper);
        
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null);
            List<String> roleNames = getUserRoleNames(id);
            user.setRealName(String.join(",", roleNames));
        }
        return Result.success(user);
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            return Result.error("用户名已存在");
        }
        
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            password = "admin123";
        }
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        
        userMapper.insert(user);
        
        if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
            for (Long roleId : user.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        
        return Result.success("新增成功");
    }

    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        
        existingUser.setPhone(user.getPhone());
        existingUser.setEmail(user.getEmail());
        existingUser.setStatus(user.getStatus());
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        
        userMapper.updateById(existingUser);
        
        LambdaQueryWrapper<UserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(deleteWrapper);
        
        if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
            for (Long roleId : user.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        LambdaQueryWrapper<UserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(deleteWrapper);
        
        userMapper.deleteById(id);
        return Result.success("删除成功");
    }
}