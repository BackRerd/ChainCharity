package site.backrer.backed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.Users;
import site.backrer.backed.service.UsersService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersService userService;
    /**
     * 创建新用户
     * 请求示例：
     * POST /users
     * {
     *   "username": "john_doe",
     *   "password": "password123",
     *   "email": "john@example.com",
     *   "isAdmin": 1,          // 必须是布尔值
     *   "userStatus": "active"     // 可选: active/suspended/pending
     * }
     */
    @PostMapping
    public Result addUser(@RequestBody Users user) {
        userService.save(user);
        return Result.success(user);
    }
    /**
     * 删除用户
     * 请求示例：
     * DELETE /users/123
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }
    /**
     * 更新用户信息
     * 请求示例：
     * PUT /users
     * {
     *   "userId": 123,
     *   "username": "updated_name",
     *   "email": "new@example.com"
     * }
     */
    @PutMapping
    public Result updateUser(@RequestBody Users user) {
        userService.updateById(user);
        return Result.success(user);
    }
    /**
     * 根据ID获取用户详情
     * 请求示例：
     * GET /users/123
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        Users user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("404", "用户不存在");
    }
    /**
     * 分页查询用户列表
     * 请求示例：
     * GET /users/page?page=2&size=5
     */
    @GetMapping("/page")
    public Result getUserByPage(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        Page<Users> pageInfo = new Page<>(page, size);
        return Result.success(userService.page(pageInfo));
    }
}

