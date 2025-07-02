package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.backrer.backed.entity.Distributions;
import site.backrer.backed.service.DistributionsService;
import site.backrer.backed.mapper.DistributionsMapper;
import org.springframework.stereotype.Service;

/**
* @author JoBackRer
* @description 针对表【distributions】的数据库操作Service实现
* @createDate 2025-06-30 08:31:27
*/
@Service
public class DistributionsServiceImpl extends ServiceImpl<DistributionsMapper, Distributions>
    implements DistributionsService{

}




