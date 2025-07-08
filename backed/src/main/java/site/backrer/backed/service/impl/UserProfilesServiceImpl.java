package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.UserProfiles;
import site.backrer.backed.service.UserProfilesService;
import site.backrer.backed.mapper.UserProfilesMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【user_profiles】的数据库操作Service实现
* @createDate 2025-07-05 15:09:57
*/
@Service
public class UserProfilesServiceImpl extends ServiceImpl<UserProfilesMapper, UserProfiles>
    implements UserProfilesService{

    @Override
    public UserProfiles getUserProfileByUserIs(Integer userId) {
        return lambdaQuery().eq(UserProfiles::getUserId, userId).one();
    }
}




