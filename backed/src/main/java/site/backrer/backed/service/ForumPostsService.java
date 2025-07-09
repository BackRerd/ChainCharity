package site.backrer.backed.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.backrer.backed.entity.ForumPosts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author JoBackRer
* @description 针对表【forum_posts】的数据库操作Service
* @createDate 2025-07-09 08:42:44
*/
public interface ForumPostsService extends IService<ForumPosts> {
    Page<ForumPosts> searchPosts(Page<ForumPosts> page, String keyword);
}
