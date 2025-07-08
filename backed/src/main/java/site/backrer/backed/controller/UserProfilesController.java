package site.backrer.backed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.UserProfiles;
import site.backrer.backed.entity.Users;
import site.backrer.backed.service.UserProfilesService;
import site.backrer.backed.service.UsersService;
import site.backrer.backed.utils.Result;
@RestController
@RequestMapping("/user-profiles")
public class UserProfilesController {
    @Autowired
    private UserProfilesService userProfilesService;

    @PostMapping
    public Result addUser(@RequestBody UserProfiles user) {
        userProfilesService.save(user);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userProfilesService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateUser(@RequestBody UserProfiles user) {
        userProfilesService.updateById(user);
        return Result.success(user);
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        UserProfiles user = userProfilesService.getUserProfileByUserIs(id);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("404", "用户不存在");
    }

    @GetMapping("/page")
    public Result getUserByPage(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        Page<UserProfiles> pageInfo = new Page<>(page, size);
        return Result.success(userProfilesService.page(pageInfo));
    }
}
