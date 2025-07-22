package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.Distributions;
import site.backrer.backed.entity.ItemDonations;
import site.backrer.backed.entity.MoneyDonations;
import site.backrer.backed.service.DistributionsService;
import site.backrer.backed.service.ItemDonationsService;
import site.backrer.backed.utils.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item-donations")
public class ItemDonationController {
    @Autowired
    private ItemDonationsService donationService;
    @Resource
    private DistributionsService distributionsService;
    /**
     * 提交物品捐赠
     * 请求示例：
     * POST /item-donations
     * {
     *   "activityId": 1,           // 活动ID
     *   "donorId": 2,               // 捐赠者ID
     *   "itemType": "书本",          // 物品类型
     *   "quantity": 100,            // 数量（整数）
     *   "imagePath": "/uploads/books.jpg"  // 图片路径（可选）
     * }
     */
    @PostMapping
    public Result addItemDonation(@RequestBody ItemDonations donation) {
        donationService.save(donation);
        return Result.success(donation);
    }

    @DeleteMapping("/{id}")
    public Result deleteItemDonation(@PathVariable Integer id) {
        donationService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateItemDonation(@RequestBody ItemDonations donation) {
        donationService.updateById(donation);
        return Result.success(donation);
    }

    @GetMapping("/{id}")
    public Result getItemDonationById(@PathVariable Integer id) {
        ItemDonations donation = donationService.getById(id);
        if (donation != null) {
            return Result.success(donation);
        }
        return Result.error("404", "捐赠记录不存在");
    }

    @GetMapping("/page")
    public Result getItemDonationByPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String userId) {
        QueryWrapper<ItemDonations> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)){
            queryWrapper.eq("donor_id", userId);
        }

        Page<ItemDonations> pageInfo = new Page<>(page, size);
        return Result.success(donationService.page(pageInfo, queryWrapper));
    }
    @GetMapping("/page-i")
    public Result getItemDonationByPageI(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String userId) {
        QueryWrapper<ItemDonations> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("donor_id", userId);
        }

        Page<ItemDonations> pageInfo = donationService.page(new Page<>(page, size), queryWrapper);

        // 预分配distributions列表大小
        List<HashMap<String, List<Distributions>>> distributions = new ArrayList<>(pageInfo.getRecords().size());

        // 使用普通for循环替代forEach，减少lambda开销
        List<ItemDonations> records = pageInfo.getRecords();
        for (ItemDonations itemDonations : records) {
            HashMap<String, List<Distributions>> itt = new HashMap<>(1); // 初始容量设为1
            itt.put(String.valueOf(itemDonations.getDonationId()),
                    distributionsService.getByItemId(itemDonations.getDonationId()));
            distributions.add(itt);
        }

        // 直接构造结果map，避免中间变量
        return Result.success(Map.of(
                "page", pageInfo,
                "distributions", distributions
        ));
    }
    @GetMapping("/stats")
    public Result getDonationStats() {
        Map<String, Object> stats = donationService.getStats();
        return Result.success(stats);
    }
}
