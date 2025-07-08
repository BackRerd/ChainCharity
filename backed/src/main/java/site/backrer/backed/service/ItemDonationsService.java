package site.backrer.backed.service;

import site.backrer.backed.entity.ItemDonations;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author JoBackRer
* @description 针对表【item_donations】的数据库操作Service
* @createDate 2025-06-30 08:31:27
*/
public interface ItemDonationsService extends IService<ItemDonations> {
    Map<String, Object> getStats();
}
