package site.backrer.backed.controller.View;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.ViewActivitiesWithCategory;
import site.backrer.backed.service.ViewActivitiesWithCategoryService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/view-activities")
public class ViewActivitiesWithCategoryController {
    @Autowired
    private ViewActivitiesWithCategoryService viewActivitiesWithCategoryService;

    @PostMapping
    public Result addActivity(@RequestBody ViewActivitiesWithCategory activity) {
        viewActivitiesWithCategoryService.save(activity);
        return Result.success(activity);
    }

    @DeleteMapping("/{id}")
    public Result deleteActivity(@PathVariable Integer id) {
        viewActivitiesWithCategoryService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateActivity(@RequestBody ViewActivitiesWithCategory activity) {
        viewActivitiesWithCategoryService.updateById(activity);
        return Result.success(activity);
    }

    @GetMapping("/{id}")
    public Result getActivityById(@PathVariable Integer id) {
        ViewActivitiesWithCategory activity = viewActivitiesWithCategoryService.getById(id);
        if (activity != null) {
            return Result.success(activity);
        }
        return Result.error("404", "活动不存在");
    }

    @GetMapping("/page")
    public Result getActivityByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        QueryWrapper<ViewActivitiesWithCategory> queryWrapper = new QueryWrapper<>();

        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }

        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }

        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }

        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("start_date", startDate);
        }

        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("end_date", endDate);
        }

        Page<ViewActivitiesWithCategory> pageInfo = new Page<>(page, size);
        return Result.success(viewActivitiesWithCategoryService.page(pageInfo, queryWrapper));
    }
}
