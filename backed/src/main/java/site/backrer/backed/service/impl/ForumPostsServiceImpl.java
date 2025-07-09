package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.ForumPosts;
import site.backrer.backed.service.ForumPostsService;
import site.backrer.backed.mapper.ForumPostsMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【forum_posts】的数据库操作Service实现
* @createDate 2025-07-09 08:42:44
*/
@Service
public class ForumPostsServiceImpl extends ServiceImpl<ForumPostsMapper, ForumPosts>
    implements ForumPostsService{

}




