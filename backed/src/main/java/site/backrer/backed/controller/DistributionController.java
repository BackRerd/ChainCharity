package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.Distributions;
import site.backrer.backed.service.DistributionsService;
import site.backrer.backed.utils.Result;

import java.util.Map;

@RestController
@RequestMapping("/distributions")
public class DistributionController {
    @Autowired
    private DistributionsService distributionService;
    /**
     * 创建分发记录
     * 请求示例：
     * POST /distributions
     * {
     *   "activityId": 1,               // 活动ID
     *   "recipientId": 4,               // 接收者ID
     *   "donationType": "item",         // 捐赠类型: item/money
     *   "itemDonationId": 1,            // 物品捐赠ID（当donationType=item时必填）
     *   "moneyDonationId": null,        // 资金捐赠ID（当donationType=money时必填）
     *   "distributedQuantity": 50,      // 分发数量（物品分发时必填）
     *   "distributedAmount": null       // 分发金额（资金分发时必填）
     * }
     */
    @PostMapping
    public Result addDistribution(@RequestBody Distributions distribution) {
        distributionService.save(distribution);
        return Result.success(distribution);
    }

    @DeleteMapping("/{id}")
    public Result deleteDistribution(@PathVariable Integer id) {
        distributionService.removeById(id);
        return Result.success();
    }
    @GetMapping("/status/{id}")
    public Result getUserStatus(@PathVariable Integer id) {
        return Result.success(distributionService.getUserStatus(id));
    }

    @PutMapping
    public Result updateDistribution(@RequestBody Distributions distribution) {
        distributionService.updateById(distribution);
        return Result.success(distribution);
    }

    @GetMapping("/{id}")
    public Result getDistributionById(@PathVariable Integer id) {
        Distributions distribution = distributionService.getById(id);
        if (distribution != null) {
            return Result.success(distribution);
        }
        return Result.error("404", "分发记录不存在");
    }

    @GetMapping("/page")
    public Result getDistributionByPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String recipient_id) {
        QueryWrapper<Distributions> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(recipient_id)){
            queryWrapper.eq("recipient_id", recipient_id);
        }
        Page<Distributions> pageInfo = new Page<>(page, size);
        return Result.success(distributionService.page(pageInfo,queryWrapper));
    }

    @GetMapping("/statistics")
    public Result getDistributionStatistics() {
        Map<String, Object> stats = distributionService.getDistributionStatistics();
        return Result.success(stats);
    }

}
