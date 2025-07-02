package site.backrer.backed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.Activities;
import site.backrer.backed.service.ActivitiesService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    @Autowired
    private ActivitiesService activityService;
    /**
     * 创建新活动
     * 请求示例：
     * POST /activities
     * {
     *   "title": "2023秋季助学活动",
     *   "description": "为山区儿童提供学习用品",
     *   "categoryId": 1,               // 分类ID
     *   "startDate": "2023-09-01",      // 格式: yyyy-MM-dd
     *   "endDate": "2023-09-30",        // 格式: yyyy-MM-dd
     *   "status": "planning"            // 可选: planning/ongoing/completed
     * }
     */
    @PostMapping
    public Result addActivity(@RequestBody Activities activity) {
        activityService.save(activity);
        return Result.success(activity);
    }

    @DeleteMapping("/{id}")
    public Result deleteActivity(@PathVariable Integer id) {
        activityService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateActivity(@RequestBody Activities activity) {
        activityService.updateById(activity);
        return Result.success(activity);
    }

    @GetMapping("/{id}")
    public Result getActivityById(@PathVariable Integer id) {
        Activities activity = activityService.getById(id);
        if (activity != null) {
            return Result.success(activity);
        }
        return Result.error("404", "活动不存在");
    }

    @GetMapping("/page")
    public Result getActivityByPage(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Page<Activities> pageInfo = new Page<>(page, size);
        return Result.success(activityService.page(pageInfo));
    }
}