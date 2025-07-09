package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.Announcement;
import site.backrer.backed.service.AnnouncementService;
import site.backrer.backed.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【announcement(公告表)】的数据库操作Service实现
* @createDate 2025-07-09 07:36:35
*/
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService{

}




