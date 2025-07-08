package site.backrer.backed.service;

import site.backrer.backed.entity.UserProfiles;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author JoBackRer
* @description 针对表【user_profiles】的数据库操作Service
* @createDate 2025-07-05 15:09:57
*/
public interface UserProfilesService extends IService<UserProfiles> {
        UserProfiles getUserProfileByUserIs(Integer userId);
}
