package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.MoneyDonations;
import site.backrer.backed.service.MoneyDonationsService;
import site.backrer.backed.mapper.MoneyDonationsMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【money_donations】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class MoneyDonationsServiceImpl extends ServiceImpl<MoneyDonationsMapper, MoneyDonations>
    implements MoneyDonationsService{

}




