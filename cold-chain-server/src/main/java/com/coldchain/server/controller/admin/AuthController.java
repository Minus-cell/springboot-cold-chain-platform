package com.coldchain.server.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.User;
import com.coldchain.server.mapper.UserMapper;
import com.coldchain.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("adminAuthController")
@RequestMapping("/api/admin/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (username == null || password == null) {
            return Result.error("用户名或密码不能为空");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return Result.error("用户不存在");
        }

        if (user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }

        String md5Password = DigestUtils.md5DigestAsHex((password).getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());

        return Result.success("登录成功", result);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("退出成功");
    }

    @GetMapping("/getInfo")
    public Result<User> getInfo(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
            User user = userMapper.selectById(userId);
            if (user != null) {
                user.setPassword(null);
            }
            return Result.success(user);
        } catch (Exception e) {
            return Result.unauthorized("token无效");
        }
    }
}