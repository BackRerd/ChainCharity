package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.ActivityCategories;
import site.backrer.backed.service.ActivityCategoriesService;
import site.backrer.backed.mapper.ActivityCategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【activity_categories】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class ActivityCategoriesServiceImpl extends ServiceImpl<ActivityCategoriesMapper, ActivityCategories>
    implements ActivityCategoriesService{

}




