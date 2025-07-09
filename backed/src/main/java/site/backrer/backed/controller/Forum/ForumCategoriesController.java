package site.backrer.backed.controller.Forum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import site.backrer.backed.entity.ForumCategories;
import site.backrer.backed.service.ForumCategoriesService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/forum/categories")
public class ForumCategoriesController {

    @Autowired
    private ForumCategoriesService forumCategoriesService;

    @GetMapping("/page")
    public Result list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer parentId,
            @RequestParam(required = false) Boolean isActive) {

        Page<ForumCategories> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumCategories> queryWrapper = new QueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        }
        if (isActive != null) {
            queryWrapper.eq("is_active", isActive);
        }
        queryWrapper.orderByAsc("sort_order");

        return Result.success(forumCategoriesService.page(page, queryWrapper));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(forumCategoriesService.getById(id));
    }

    @PostMapping
    public Result create(@RequestBody ForumCategories category) {
        return Result.success(forumCategoriesService.save(category));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody ForumCategories category) {
        category.setCategoryId(id);
        return Result.success(forumCategoriesService.updateById(category));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(forumCategoriesService.removeById(id));
    }
}