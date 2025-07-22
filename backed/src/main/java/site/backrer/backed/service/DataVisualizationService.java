package site.backrer.backed.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 大数据可视化服务接口
 * 提供各种统计数据和可视化相关的业务方法
 */
public interface DataVisualizationService {

    /**
     * 获取总体概览数据
     */
    Map<String, Object> getOverviewData();

    /**
     * 获取用户分析数据
     */
    Map<String, Object> getUserAnalysisData();

    /**
     * 获取捐赠分析数据
     */
    Map<String, Object> getDonationAnalysisData();

    /**
     * 获取活动分析数据
     */
    Map<String, Object> getActivityAnalysisData();

    /**
     * 获取论坛分析数据
     */
    Map<String, Object> getForumAnalysisData();

    /**
     * 获取实时数据
     */
    Map<String, Object> getRealtimeData();

    /**
     * 获取自定义时间范围的数据
     */
    Map<String, Object> getCustomRangeData(String startDate, String endDate);

    /**
     * 获取用户注册趋势
     */
    List<Map<String, Object>> getRegistrationTrend();

    /**
     * 获取捐赠趋势
     */
    List<Map<String, Object>> getDonationTrend();

    /**
     * 获取物品类型分布
     */
    List<Map<String, Object>> getItemTypeDistribution();

    /**
     * 获取活动创建趋势
     */
    List<Map<String, Object>> getActivityTrend();

    /**
     * 获取热门帖子
     */
    List<Map<String, Object>> getHotPosts();

    /**
     * 获取论坛活跃度趋势
     */
    List<Map<String, Object>> getForumTrend();

    /**
     * 获取今日新增用户数
     */
    long getTodayNewUsers();

    /**
     * 获取今日金钱捐赠总额
     */
    BigDecimal getTodayMoneyDonations();

    /**
     * 获取今日物品捐赠数量
     */
    long getTodayItemDonations();

    /**
     * 获取今日新增活动数
     */
    long getTodayNewActivities();

    /**
     * 获取今日新增帖子数
     */
    long getTodayNewPosts();

    /**
     * 获取今日新增回复数
     */
    long getTodayNewReplies();

    /**
     * 获取时间范围内新增用户数
     */
    long getNewUsersInRange(String startDate, String endDate);

    /**
     * 获取时间范围内金钱捐赠总额
     */
    BigDecimal getMoneyDonationsInRange(String startDate, String endDate);

    /**
     * 获取时间范围内物品捐赠数量
     */
    long getItemDonationsInRange(String startDate, String endDate);

    /**
     * 获取时间范围内新增活动数
     */
    long getNewActivitiesInRange(String startDate, String endDate);
} 