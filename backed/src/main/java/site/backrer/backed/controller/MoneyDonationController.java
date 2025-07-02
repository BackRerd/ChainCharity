package site.backrer.backed.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.MoneyDonations;
import site.backrer.backed.service.MoneyDonationsService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/money-donations")
public class MoneyDonationController {
    @Autowired
    private MoneyDonationsService donationService;
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

    @GetMapping("/page")
    public Result getMoneyDonationByPage(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Page<MoneyDonations> pageInfo = new Page<>(page, size);
        return Result.success(donationService.page(pageInfo));
    }
}

