package com.coldchain.server.controller.portal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coldchain.server.common.Result;
import com.coldchain.server.entity.User;
import com.coldchain.server.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portal/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/info")
    public Result<User> getInfo(@RequestParam Long userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<Void> updateInfo(@RequestBody User user) {
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }

        existUser.setRealName(user.getRealName());
        existUser.setPhone(user.getPhone());
        existUser.setEmail(user.getEmail());

        userMapper.updateById(existUser);
        return Result.success("更新成功");
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestParam Long userId,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        String oldMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldMd5.equals(user.getPassword())) {
            return Result.error("原密码错误");
        }

        String newMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        user.setPassword(newMd5);
        userMapper.updateById(user);

        return Result.success("密码修改成功");
    }
}
