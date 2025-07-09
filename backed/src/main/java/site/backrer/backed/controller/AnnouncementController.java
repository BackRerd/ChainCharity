package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.Announcement;
import site.backrer.backed.entity.Distributions;
import site.backrer.backed.service.AnnouncementService;
import site.backrer.backed.utils.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    // 获取所有公告
    @GetMapping
    public Result getAllAnnouncements() {
        return Result.success(announcementService.list());
    }

    // 新增公告
    @PostMapping
    public Result addAnnouncement(@RequestBody Announcement announcement) {
        announcement.setId(UUID.randomUUID().toString());
        announcement.setCreateTime(new Date());
        announcement.setUpdateTime(new Date());
        announcementService.save(announcement);
        return Result.success(announcement);
    }
    @GetMapping("/page")
    public Result getDistributionByPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String title) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(title)){
            queryWrapper.eq("title", title);
        }
        Page<Announcement> pageInfo = new Page<>(page, size);
        return Result.success(announcementService.page(pageInfo,queryWrapper));
    }
    // 更新公告
    @PutMapping("/{id}")
    public Result updateAnnouncement(@PathVariable String id, @RequestBody Announcement announcement) {
        if (announcementService.getById(announcement.getId()) != null) {
            announcementService.updateById(announcement);
            return Result.success(announcementService.getById(announcement.getId()));
        }
        return Result.error("404", "公告不存在");
    }

    // 删除公告
    @DeleteMapping("/{id}")
    public Result deleteAnnouncement(@PathVariable String id) {
        boolean removed = announcementService.removeById(id);
        return removed ? Result.success() : Result.error("404", "公告不存在");
    }
}
