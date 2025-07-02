package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.Activities;
import site.backrer.backed.service.ActivitiesService;
import site.backrer.backed.mapper.ActivitiesMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【activities】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class ActivitiesServiceImpl extends ServiceImpl<ActivitiesMapper, Activities>
    implements ActivitiesService{

}




