package site.backrer.backed.controller.Forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import site.backrer.backed.entity.ForumLikes;
import site.backrer.backed.service.ForumLikesService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/forum/likes")
public class ForumLikesController {

    @Autowired
    private ForumLikesService forumLikesService;

    @GetMapping("/page")
    public Result list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer postId,
            @RequestParam(required = false) Integer replyId) {

        Page<ForumLikes> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumLikes> queryWrapper = new QueryWrapper<>();

        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (postId != null) {
            queryWrapper.eq("post_id", postId);
        }
        if (replyId != null) {
            queryWrapper.eq("reply_id", replyId);
        }
        queryWrapper.orderByDesc("created_at");

        return Result.success(forumLikesService.page(page, queryWrapper));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(forumLikesService.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody ForumLikes like) {
        return Result.success(forumLikesService.save(like));
    }

    @DeleteMapping("")
    public Result delete(@RequestParam Integer id,
                         @RequestParam(required = false) Integer postId,
                         @RequestParam(required = false) Integer userId) {
        boolean b;
        if (postId != null && userId != null) {
            QueryWrapper<ForumLikes> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                    .eq("post_id", postId);
            b = forumLikesService.remove(wrapper);
        }else {
            b = forumLikesService.removeById(id);;
        }

        return Result.success(b);
    }
}