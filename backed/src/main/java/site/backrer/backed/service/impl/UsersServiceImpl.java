package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.Users;
import site.backrer.backed.service.UsersService;
import site.backrer.backed.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-07-05 15:08:30
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{
    @Override
    public Users getByUsernameOrEmail(String nameOrEmail) {
        return lambdaQuery()
                .eq(Users::getUsername,nameOrEmail)
                .or()
                .eq(Users::getPhone,nameOrEmail)
                .one();
    }

    @Override
    public Users getByUsername(String username) {
        return lambdaQuery().eq(Users::getUsername,username).one();
    }

    @Override
    public Users getByEmail(String email) {
        return lambdaQuery().eq(Users::getPhone,email).one();
    }
}




