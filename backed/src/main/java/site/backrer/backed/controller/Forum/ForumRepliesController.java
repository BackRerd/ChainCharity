package site.backrer.backed.controller.Forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import site.backrer.backed.entity.ForumReplies;
import site.backrer.backed.service.ForumRepliesService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/forum/replies")
public class ForumRepliesController {

    @Autowired
    private ForumRepliesService forumRepliesService;

    @GetMapping("/page")
    public Result list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer postId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Boolean isFirst,
            @RequestParam(required = false) String status) {

        Page<ForumReplies> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumReplies> queryWrapper = new QueryWrapper<>();

        if (postId != null) {
            queryWrapper.eq("post_id", postId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (isFirst != null) {
            queryWrapper.eq("is_first", isFirst);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByAsc("created_at");

        return Result.success(forumRepliesService.page(page, queryWrapper));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(forumRepliesService.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody ForumReplies reply) {
        return Result.success(forumRepliesService.save(reply));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody ForumReplies reply) {
        reply.setReplyId(id);
        return Result.success(forumRepliesService.updateById(reply));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(forumRepliesService.removeById(id));
    }
}
