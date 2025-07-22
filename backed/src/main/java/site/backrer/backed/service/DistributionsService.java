package site.backrer.backed.service;

import site.backrer.backed.entity.Distributions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author JoBackRer
* @description 针对表【distributions】的数据库操作Service
* @createDate 2025-06-30 08:31:27
*/
public interface DistributionsService extends IService<Distributions> {
    Map<String, Object> getDistributionStatistics();
    List<Distributions> getByMoneyId(int moneyId);
    List<Distributions> getByItemId(int itemId);

    List<BigDecimal> getUserStatus(int userId);
}
