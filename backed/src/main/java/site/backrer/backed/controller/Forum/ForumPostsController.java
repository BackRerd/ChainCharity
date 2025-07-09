package site.backrer.backed.controller.Forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import site.backrer.backed.entity.ForumPosts;
import site.backrer.backed.service.ForumPostsService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/forum/posts")
public class ForumPostsController {

    @Autowired
    private ForumPostsService forumPostsService;

    @GetMapping("/page")
    public Result list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean isSticky,
            @RequestParam(required = false) Boolean isEssence,
            @RequestParam(required = false) Boolean isClosed,
            @RequestParam(required = false) String status) {

        Page<ForumPosts> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumPosts> queryWrapper = new QueryWrapper<>();

        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (isSticky != null) {
            queryWrapper.eq("is_sticky", isSticky);
        }
        if (isEssence != null) {
            queryWrapper.eq("is_essence", isEssence);
        }
        if (isClosed != null) {
            queryWrapper.eq("is_closed", isClosed);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("is_sticky").orderByDesc("created_at");

        return Result.success(forumPostsService.page(page, queryWrapper));
    }

    @GetMapping("/search")
    public Result search(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam String keyword) {

        Page<ForumPosts> page = new Page<>(pageNum, pageSize);
        return Result.success(forumPostsService.searchPosts(page, keyword));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(forumPostsService.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody ForumPosts post) {
        return Result.success(forumPostsService.save(post));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody ForumPosts post) {
        post.setPostId(id);
        return Result.success(forumPostsService.updateById(post));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(forumPostsService.removeById(id));
    }
}
