package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.ActivityCategories;
import site.backrer.backed.service.ActivityCategoriesService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/categories")
public class ActivityCategoryController {
    @Autowired
    private ActivityCategoriesService categoryService;

    @PostMapping
    public Result addCategory(@RequestBody ActivityCategories category) {
        categoryService.save(category);
        return Result.success(category);
    }

    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Integer id) {
        categoryService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateCategory(@RequestBody ActivityCategories category) {
        categoryService.updateById(category);
        return Result.success(category);
    }

    @GetMapping("/{id}")
    public Result getCategoryById(@PathVariable Integer id) {
        ActivityCategories category = categoryService.getById(id);
        if (category != null) {
            return Result.success(category);
        }
        return Result.error("404", "分类不存在");
    }

    @GetMapping("/page")
    public Result getCategoryByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String description) {

        QueryWrapper<ActivityCategories> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(categoryName)) {
            queryWrapper.like("category_name", categoryName);
        }

        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like("description", description);
        }

        Page<ActivityCategories> pageInfo = new Page<>(page, size);
        return Result.success(categoryService.page(pageInfo, queryWrapper));
    }
}

