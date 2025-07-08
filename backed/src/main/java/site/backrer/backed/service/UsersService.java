package site.backrer.backed.service;

import site.backrer.backed.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author JoBackRer
* @description 针对表【users】的数据库操作Service
* @createDate 2025-07-05 15:08:30
*/
public interface UsersService extends IService<Users> {
    Users getByUsernameOrEmail(String nameOrEmail);
    Users getByUsername(String username);
    Users getByEmail(String email);
}
