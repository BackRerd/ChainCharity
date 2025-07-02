package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.Users;
import site.backrer.backed.service.UsersService;
import site.backrer.backed.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




