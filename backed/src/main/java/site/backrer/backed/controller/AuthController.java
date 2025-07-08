package site.backrer.backed.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.LoginRequest;
import site.backrer.backed.entity.RegisterRequest;
import site.backrer.backed.entity.UserInfoResponse;
import site.backrer.backed.entity.Users;
import site.backrer.backed.service.UsersService;
import site.backrer.backed.utils.JwtUtils;
import site.backrer.backed.utils.PasswordUtils;
import site.backrer.backed.utils.Result;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private UsersService usersService; // 你自己实现，用于查用户

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private PasswordUtils passwordUtils;
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        Users user = usersService.getByUsernameOrEmail(loginRequest.getUsername());
        if (user == null || !passwordUtils.matches(loginRequest.getPassword(), user.getPassword())) {
            return Result.error("账户或密码错误！");
        }

        String token = jwtUtils.generateToken(user);
        return Result.success(token);
    }
    @GetMapping("/token")
    public Result getUserName(String token){
        if (token == null){
            return Result.error("token为空");
        }
        UserInfoResponse userInfoFromJwt = jwtUtils.getUserInfoFromJwt(token);
        if (userInfoFromJwt == null) {
            return Result.error("jwt验证失效!");
        }
        if (userInfoFromJwt.getRole() == 1){
            //校验ip
            userInfoFromJwt.setRole(1);
            return Result.success(userInfoFromJwt);
        }else {
            return Result.success(userInfoFromJwt);
        }
    }
    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        if (usersService.getByUsernameOrEmail(registerRequest.getUsername()) != null) {
            return Result.error("用户名或手机号已存在");
        }
        System.out.println(registerRequest.toString());
        Users user = new Users();
        user.setUsername(registerRequest.getUsername());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordUtils.hashPassword(registerRequest.getPassword()));
        user.setIsAdmin(0);
        user.setCreatedAt(new Date());
        user.setUserStatus(null);
        usersService.save(user);
        return Result.success("注册成功!");
    }
}
