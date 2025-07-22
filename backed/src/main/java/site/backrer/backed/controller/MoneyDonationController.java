package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.*;
import site.backrer.backed.mapper.MoneyDonationsMapper;
import site.backrer.backed.service.DistributionsService;
import site.backrer.backed.service.MoneyDonationsService;
import site.backrer.backed.utils.Result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/money-donations")
public class MoneyDonationController {
    @Autowired
    private MoneyDonationsService donationService;
    @Autowired
    private DistributionsService distributionsService;

    /**
     * 提交资金捐赠
     * 请求示例：
     * POST /money-donations
     * {
     *   "activityId": 1,           // 活动ID
     *   "donorId": 3,               // 捐赠者ID
     *   "amount": 5000.00           // 金额（小数）
     * }
     */
    @PostMapping
    public Result addMoneyDonation(@RequestBody MoneyDonations donation) {
        donationService.save(donation);
        return Result.success(donation);
    }

    @DeleteMapping("/{id}")
    public Result deleteMoneyDonation(@PathVariable Integer id) {
        donationService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateMoneyDonation(@RequestBody MoneyDonations donation) {
        donationService.updateById(donation);
        return Result.success(donation);
    }

    @GetMapping("/{id}")
    public Result getMoneyDonationById(@PathVariable Integer id) {
        MoneyDonations donation = donationService.getById(id);
        if (donation != null) {
            return Result.success(donation);
        }
        return Result.error("404", "捐赠记录不存在");
    }
    @GetMapping("/stats")
    public Result getMoneyDonationStats() {
        MoneyDonateStats moneyDonateStats = new MoneyDonateStats();
        moneyDonateStats.setTotalAmount(donationService.amountSum());
        moneyDonateStats.setApprovedCount(donationService.approvedCount());
        moneyDonateStats.setRejectedCount(donationService.rejectedCount());
        moneyDonateStats.setPendingCount(donationService.pendingCount());
        return Result.success(moneyDonateStats);
    }

    @GetMapping("/page")
    public Result getMoneyDonationByPage(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String userId) {
        QueryWrapper<MoneyDonations> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)){
            queryWrapper.eq("donor_id", userId);
        }
        Page<MoneyDonations> pageInfo = new Page<>(page, size);
        return Result.success(donationService.page(pageInfo,queryWrapper));
    }
    @GetMapping("/page-i")
    public Result getItemDonationByPageI(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String userId) {
        QueryWrapper<MoneyDonations> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("donor_id", userId);
        }

        Page<MoneyDonations> pageInfo = donationService.page(new Page<>(page, size), queryWrapper);
        
        // 预分配distributions列表大小
        List<HashMap<String, List<Distributions>>> distributions = new ArrayList<>(pageInfo.getRecords().size());

        // 使用普通for循环替代forEach，减少lambda开销
        List<MoneyDonations> records = pageInfo.getRecords();
        for (MoneyDonations MoneyDonations : records) {
            HashMap<String, List<Distributions>> itt = new HashMap<>(1); // 初始容量设为1
            itt.put(String.valueOf(MoneyDonations.getDonationId()),
                    distributionsService.getByMoneyId(MoneyDonations.getDonationId()));
            distributions.add(itt);
        }

        // 直接构造结果map，避免中间变量
        return Result.success(Map.of(
                "page", pageInfo,
                "distributions", distributions
        ));
    }
}

