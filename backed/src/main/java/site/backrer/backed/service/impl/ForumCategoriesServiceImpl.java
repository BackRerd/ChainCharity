package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.ForumCategories;
import site.backrer.backed.service.ForumCategoriesService;
import site.backrer.backed.mapper.ForumCategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【forum_categories】的数据库操作Service实现
* @createDate 2025-07-09 08:42:44
*/
@Service
public class ForumCategoriesServiceImpl extends ServiceImpl<ForumCategoriesMapper, ForumCategories>
    implements ForumCategoriesService{

}




