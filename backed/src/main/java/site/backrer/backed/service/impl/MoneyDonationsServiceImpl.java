package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import site.backrer.backed.entity.MoneyDonateStats;
import site.backrer.backed.entity.MoneyDonations;
import site.backrer.backed.service.MoneyDonationsService;
import site.backrer.backed.mapper.MoneyDonationsMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author JoBackRer
* @description 针对表【money_donations】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class MoneyDonationsServiceImpl extends ServiceImpl<MoneyDonationsMapper, MoneyDonations>
    implements MoneyDonationsService{
    @Resource
    private MoneyDonationsMapper moneyDonationsMapper;

    @Override
    public BigDecimal amountSum() {
        QueryWrapper<MoneyDonations> wrapper = new QueryWrapper<>();
        wrapper.select("SUM(amount) AS total_amount");
        List<Map<String, Object>> result = moneyDonationsMapper.selectMaps(wrapper);
        BigDecimal amountSum = BigDecimal.ZERO;
        if (!result.isEmpty() && result.get(0).get("total_amount") != null) {
            amountSum = new BigDecimal(result.get(0).get("total_amount").toString());
        }
        return amountSum;
    }

    @Override
    public Long approvedCount() {
        QueryWrapper<MoneyDonations> wrapper = new QueryWrapper<>();
        wrapper.eq("approval_status", "approved");
        return moneyDonationsMapper.selectCount(wrapper);
    }

    @Override
    public Long pendingCount() {
        QueryWrapper<MoneyDonations> wrapper = new QueryWrapper<>();
        wrapper.eq("approval_status", "pending");
        return moneyDonationsMapper.selectCount(wrapper);
    }

    @Override
    public Long rejectedCount() {
        QueryWrapper<MoneyDonations> wrapper = new QueryWrapper<>();
        wrapper.eq("approval_status", "rejected");
        return moneyDonationsMapper.selectCount(wrapper);
    }
}




