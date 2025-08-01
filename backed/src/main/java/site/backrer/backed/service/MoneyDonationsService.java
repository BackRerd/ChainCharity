package site.backrer.backed.service;

import site.backrer.backed.entity.MoneyDonations;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author JoBackRer
* @description 针对表【money_donations】的数据库操作Service
* @createDate 2025-06-30 08:31:27
*/
public interface MoneyDonationsService extends IService<MoneyDonations> {
    List<MoneyDonations> getByUserID(Integer userID);

    BigDecimal amountSum();
    Long approvedCount();
    Long pendingCount();
    Long rejectedCount();
}
