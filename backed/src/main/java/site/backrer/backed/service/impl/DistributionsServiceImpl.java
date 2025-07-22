package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import site.backrer.backed.entity.Distributions;
import site.backrer.backed.service.DistributionsService;
import site.backrer.backed.mapper.DistributionsMapper;
import org.springframework.stereotype.Service;
import site.backrer.backed.service.ItemDonationsService;
import site.backrer.backed.service.MoneyDonationsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
* @author JoBackRer
* @description 针对表【distributions】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class DistributionsServiceImpl extends ServiceImpl<DistributionsMapper, Distributions>
    implements DistributionsService{

    @Resource
    private ItemDonationsService itemDonationsService;
    @Resource
    private MoneyDonationsService moneyDonationsService;

    @Override
    public Map<String, Object> getDistributionStatistics() {
        // 先做总统计
        QueryWrapper<Distributions> approvedWrapper = new QueryWrapper<>();
        approvedWrapper.eq("approval_status", "approved");
        List<Map<String, Object>> totalResult = this.baseMapper.selectMaps(approvedWrapper.select(
                "COUNT(*) AS total_records",
                "IFNULL(SUM(CASE WHEN donation_type = 'item' THEN distributed_quantity ELSE 0 END), 0) AS total_items",
                "IFNULL(SUM(CASE WHEN donation_type = 'money' THEN distributed_amount ELSE 0 END), 0) AS total_amount",
                "COUNT(DISTINCT recipient_id) AS total_recipients"
        ));
        Map<String, Object> stats = totalResult.isEmpty() ? new HashMap<>() : totalResult.get(0);

        // ========== 统计本月记录 ==========
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfThisMonth = now.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = firstDayOfThisMonth.plusMonths(1);
        QueryWrapper<Distributions> thisMonthWrapper = new QueryWrapper<>();
        thisMonthWrapper.eq("approval_status", "approved")
                .ge("distribution_time", firstDayOfThisMonth)
                .lt("distribution_time", firstDayOfNextMonth);
        Long thisMonthCount = this.baseMapper.selectCount(thisMonthWrapper);

        // ========== 统计上月记录 ==========
        LocalDate firstDayOfLastMonth = firstDayOfThisMonth.minusMonths(1);
        LocalDate firstDayOfThisMonthAgain = firstDayOfThisMonth;
        QueryWrapper<Distributions> lastMonthWrapper = new QueryWrapper<>();
        lastMonthWrapper.eq("approval_status", "approved")
                .ge("distribution_time", firstDayOfLastMonth)
                .lt("distribution_time", firstDayOfThisMonthAgain);
        Long lastMonthCount = this.baseMapper.selectCount(lastMonthWrapper);

        // ========== 计算同比增长率 ==========
        double growthRate;
        if (lastMonthCount != null && lastMonthCount > 0) {
            growthRate = (thisMonthCount - lastMonthCount) * 1.0 / lastMonthCount;
        } else {
            growthRate = 1.0; // 如果上月无数据，定义为100%增长
        }

        stats.put("this_month_records", thisMonthCount);
        stats.put("last_month_records", lastMonthCount);
        stats.put("growth_rate", growthRate);

        // 生成覆盖地区数量
        Number totalRecipientsNum = (Number) stats.getOrDefault("total_recipients", 0L);
        int totalRecipients = totalRecipientsNum.intValue();
        int minRegions = Math.max(1, totalRecipients / 10);
        int maxRegions = Math.max(1, totalRecipients / 5);
        int randomRegions = new Random().nextInt(maxRegions - minRegions + 1) + minRegions;
        stats.put("covered_regions", randomRegions);


        return stats;
    }

    @Override
    public List<Distributions> getByMoneyId(int moneyId) {
        return lambdaQuery().eq(Distributions::getMoneyDonationId, moneyId).list();
    }

    @Override
    public List<Distributions> getByItemId(int itemId) {
        return lambdaQuery().eq(Distributions::getItemDonationId, itemId).list();
    }
    @Override
    public List<BigDecimal> getUserStatus(int userId){
        LambdaQueryChainWrapper<Distributions> approved = lambdaQuery().eq(Distributions::getRecipientId, userId).eq(Distributions::getApprovalStatus, "approved");
        List<BigDecimal> list = new ArrayList<>();
        List<Distributions> item = approved.eq(Distributions::getDonationType, "item").list();
        List<Distributions> money = approved.eq(Distributions::getDonationType, "money").list();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Distributions d : money) {
            totalAmount.add(d.getDistributedAmount());
        }
        list.add(totalAmount);
        totalAmount = BigDecimal.ZERO;
        for (Distributions d : item) {
            totalAmount.add(BigDecimal.valueOf(d.getDistributedQuantity()));
        }
        list.add(totalAmount);
        return list;
    }

}




