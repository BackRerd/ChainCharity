package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.ItemDonations;
import site.backrer.backed.service.ItemDonationsService;
import site.backrer.backed.mapper.ItemDonationsMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author JoBackRer
* @description 针对表【item_donations】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class ItemDonationsServiceImpl extends ServiceImpl<ItemDonationsMapper, ItemDonations>
    implements ItemDonationsService{
    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDonations", this.count());
        stats.put("approvedDonations", this.lambdaQuery().eq(ItemDonations::getApprovalStatus, "approved").count());
        stats.put("pendingDonations", this.lambdaQuery().eq(ItemDonations::getApprovalStatus, "pending").count());
        return stats;
    }
    @Override
    public List<ItemDonations> getByUserID(Integer userID){
        return lambdaQuery().eq(ItemDonations::getDonationId, userID).list();
    }
}




