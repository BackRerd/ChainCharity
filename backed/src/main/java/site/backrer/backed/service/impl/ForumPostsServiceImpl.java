package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @Override
    public Page<ForumPosts> searchPosts(Page<ForumPosts> page, String keyword) {
        QueryWrapper<ForumPosts> queryWrapper = new QueryWrapper<>();

        // 使用MyBatis-Plus的条件构造器进行全文搜索
        queryWrapper.and(wrapper -> wrapper
                .like("title", keyword)
                .or()
                .like("content", keyword)
        );

        // 只返回已发布的帖子
        queryWrapper.eq("status", "published");

        // 按创建时间降序排列
        queryWrapper.orderByDesc("created_at");

        return baseMapper.selectPage(page, queryWrapper);
    }
}




