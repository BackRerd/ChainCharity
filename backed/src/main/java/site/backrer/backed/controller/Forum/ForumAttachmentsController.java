package site.backrer.backed.controller.Forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import site.backrer.backed.entity.ForumAttachments;
import site.backrer.backed.service.ForumAttachmentsService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/forum/attachments")
public class ForumAttachmentsController {

    @Autowired
    private ForumAttachmentsService forumAttachmentsService;

    @GetMapping("/list")
    public Result list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer postId,
            @RequestParam(required = false) Integer replyId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String fileType) {

        Page<ForumAttachments> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumAttachments> queryWrapper = new QueryWrapper<>();

        if (postId != null) {
            queryWrapper.eq("post_id", postId);
        }
        if (replyId != null) {
            queryWrapper.eq("reply_id", replyId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (fileType != null && !fileType.isEmpty()) {
            queryWrapper.eq("file_type", fileType);
        }
        queryWrapper.orderByDesc("created_at");

        return Result.success(forumAttachmentsService.page(page, queryWrapper));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(forumAttachmentsService.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody ForumAttachments attachment) {
        return Result.success(forumAttachmentsService.save(attachment));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody ForumAttachments attachment) {
        attachment.setAttachmentId(id);
        return Result.success(forumAttachmentsService.updateById(attachment));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(forumAttachmentsService.removeById(id));
    }
}
