package site.backrer.backed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.backrer.backed.entity.*;
import site.backrer.backed.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 大数据可视化服务实现类
 */
@Service
public class DataVisualizationServiceImpl implements DataVisualizationService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ActivitiesService activitiesService;

    @Autowired
    private MoneyDonationsService moneyDonationsService;

    @Autowired
    private ItemDonationsService itemDonationsService;

    @Autowired
    private ActivityParticipationService activityParticipationService;

    @Autowired
    private ForumPostsService forumPostsService;

    @Autowired
    private ForumRepliesService forumRepliesService;

    @Autowired
    private ForumLikesService forumLikesService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private DistributionsService distributionsService;

    @Override
    public Map<String, Object> getOverviewData() {
        Map<String, Object> overview = new HashMap<>();
        
        // 用户统计
        long totalUsers = usersService.count();
        long adminUsers = usersService.lambdaQuery().eq(Users::getIsAdmin, 1).count();
        long enterpriseUsers = usersService.lambdaQuery().eq(Users::getIsEnterprise, 1).count();
        long individualUsers = totalUsers - enterpriseUsers;

        // 活动统计
        long totalActivities = activitiesService.count();
        long planningActivities = activitiesService.lambdaQuery().eq(Activities::getStatus, "planning").count();
        long ongoingActivities = activitiesService.lambdaQuery().eq(Activities::getStatus, "ongoing").count();
        long completedActivities = activitiesService.lambdaQuery().eq(Activities::getStatus, "completed").count();

        // 捐赠统计
        BigDecimal totalMoneyDonations = moneyDonationsService.amountSum();
        long totalItemDonations = itemDonationsService.count();
        long approvedMoneyDonations = moneyDonationsService.approvedCount();
        long pendingMoneyDonations = moneyDonationsService.pendingCount();

        // 论坛统计
        long totalPosts = forumPostsService.count();
        long totalReplies = forumRepliesService.count();
        long totalLikes = forumLikesService.count();

        // 公告统计
        long totalAnnouncements = announcementService.count();

        overview.put("users", Map.of(
            "total", totalUsers,
            "admin", adminUsers,
            "enterprise", enterpriseUsers,
            "individual", individualUsers
        ));

        overview.put("activities", Map.of(
            "total", totalActivities,
            "planning", planningActivities,
            "ongoing", ongoingActivities,
            "completed", completedActivities
        ));

        overview.put("donations", Map.of(
            "totalMoney", totalMoneyDonations != null ? totalMoneyDonations : BigDecimal.ZERO,
            "totalItems", totalItemDonations,
            "approvedMoney", approvedMoneyDonations,
            "pendingMoney", pendingMoneyDonations
        ));

        overview.put("forum", Map.of(
            "posts", totalPosts,
            "replies", totalReplies,
            "likes", totalLikes
        ));

        overview.put("announcements", totalAnnouncements);

        return overview;
    }

    @Override
    public Map<String, Object> getUserAnalysisData() {
        Map<String, Object> analysis = new HashMap<>();
        
        // 用户类型分布
        long donorUsers = usersService.lambdaQuery().eq(Users::getUserType, "donor").count();
        long recipientUsers = usersService.lambdaQuery().eq(Users::getUserType, "recipient").count();
        long enterpriseUsers = usersService.lambdaQuery().eq(Users::getIsEnterprise, 1).count();

        // 用户状态分布
        long activeUsers = usersService.lambdaQuery().eq(Users::getUserStatus, "active").count();
        long suspendedUsers = usersService.lambdaQuery().eq(Users::getUserStatus, "suspended").count();
        long pendingUsers = usersService.lambdaQuery().eq(Users::getUserStatus, "pending").count();

        // 用户注册趋势
        List<Map<String, Object>> registrationTrend = getRegistrationTrend();

        analysis.put("userTypeDistribution", Map.of(
            "donor", donorUsers,
            "recipient", recipientUsers,
            "enterprise", enterpriseUsers
        ));

        analysis.put("userStatusDistribution", Map.of(
            "active", activeUsers,
            "suspended", suspendedUsers,
            "pending", pendingUsers
        ));

        analysis.put("registrationTrend", registrationTrend);

        return analysis;
    }

    @Override
    public Map<String, Object> getDonationAnalysisData() {
        Map<String, Object> analysis = new HashMap<>();
        
        // 金钱捐赠统计
        BigDecimal totalAmount = moneyDonationsService.amountSum();
        long approvedCount = moneyDonationsService.approvedCount();
        long pendingCount = moneyDonationsService.pendingCount();
        long rejectedCount = moneyDonationsService.rejectedCount();

        // 物品捐赠统计
        long totalItemDonations = itemDonationsService.count();
        long approvedItemDonations = itemDonationsService.lambdaQuery()
            .eq(ItemDonations::getApprovalStatus, "approved").count();
        long pendingItemDonations = itemDonationsService.lambdaQuery()
            .eq(ItemDonations::getApprovalStatus, "pending").count();

        // 捐赠趋势
        List<Map<String, Object>> donationTrend = getDonationTrend();

        // 物品类型分布
        List<Map<String, Object>> itemTypeDistribution = getItemTypeDistribution();

        analysis.put("moneyDonations", Map.of(
            "totalAmount", totalAmount != null ? totalAmount : BigDecimal.ZERO,
            "approvedCount", approvedCount,
            "pendingCount", pendingCount,
            "rejectedCount", rejectedCount
        ));

        analysis.put("itemDonations", Map.of(
            "totalCount", totalItemDonations,
            "approvedCount", approvedItemDonations,
            "pendingCount", pendingItemDonations
        ));

        analysis.put("donationTrend", donationTrend);
        analysis.put("itemTypeDistribution", itemTypeDistribution);

        return analysis;
    }

    @Override
    public Map<String, Object> getActivityAnalysisData() {
        Map<String, Object> analysis = new HashMap<>();
        
        // 活动状态分布
        long planningCount = activitiesService.lambdaQuery().eq(Activities::getStatus, "planning").count();
        long ongoingCount = activitiesService.lambdaQuery().eq(Activities::getStatus, "ongoing").count();
        long completedCount = activitiesService.lambdaQuery().eq(Activities::getStatus, "completed").count();

        // 活动参与统计
        long totalParticipation = activityParticipationService.count();
        long donorParticipation = activityParticipationService.lambdaQuery()
            .eq(ActivityParticipation::getRole, "donor").count();
        long recipientParticipation = activityParticipationService.lambdaQuery()
            .eq(ActivityParticipation::getRole, "recipient").count();

        // 活动创建趋势
        List<Map<String, Object>> activityTrend = getActivityTrend();

        analysis.put("statusDistribution", Map.of(
            "planning", planningCount,
            "ongoing", ongoingCount,
            "completed", completedCount
        ));

        analysis.put("participation", Map.of(
            "total", totalParticipation,
            "donors", donorParticipation,
            "recipients", recipientParticipation
        ));

        analysis.put("activityTrend", activityTrend);

        return analysis;
    }

    @Override
    public Map<String, Object> getForumAnalysisData() {
        Map<String, Object> analysis = new HashMap<>();
        
        // 帖子统计
        long totalPosts = forumPostsService.count();
        long stickyPosts = forumPostsService.lambdaQuery().eq(ForumPosts::getIsSticky, 1).count();
        long essencePosts = forumPostsService.lambdaQuery().eq(ForumPosts::getIsEssence, 1).count();
        long closedPosts = forumPostsService.lambdaQuery().eq(ForumPosts::getIsClosed, 1).count();

        // 回复和点赞统计
        long totalReplies = forumRepliesService.count();
        long totalLikes = forumLikesService.count();

        // 热门帖子
        List<Map<String, Object>> hotPosts = getHotPosts();

        // 论坛活跃度趋势
        List<Map<String, Object>> forumTrend = getForumTrend();

        analysis.put("posts", Map.of(
            "total", totalPosts,
            "sticky", stickyPosts,
            "essence", essencePosts,
            "closed", closedPosts
        ));

        analysis.put("interactions", Map.of(
            "replies", totalReplies,
            "likes", totalLikes
        ));

        analysis.put("hotPosts", hotPosts);
        analysis.put("forumTrend", forumTrend);

        return analysis;
    }

    @Override
    public Map<String, Object> getRealtimeData() {
        Map<String, Object> realtime = new HashMap<>();
        
        // 今日新增用户
        long todayNewUsers = getTodayNewUsers();
        
        // 今日新增捐赠
        BigDecimal todayMoneyDonations = getTodayMoneyDonations();
        long todayItemDonations = getTodayItemDonations();
        
        // 今日新增活动
        long todayNewActivities = getTodayNewActivities();
        
        // 今日论坛活跃度
        long todayNewPosts = getTodayNewPosts();
        long todayNewReplies = getTodayNewReplies();

        realtime.put("todayNewUsers", todayNewUsers);
        realtime.put("todayMoneyDonations", todayMoneyDonations);
        realtime.put("todayItemDonations", todayItemDonations);
        realtime.put("todayNewActivities", todayNewActivities);
        realtime.put("todayNewPosts", todayNewPosts);
        realtime.put("todayNewReplies", todayNewReplies);
        realtime.put("timestamp", new Date());

        return realtime;
    }

    @Override
    public Map<String, Object> getCustomRangeData(String startDate, String endDate) {
        Map<String, Object> customData = new HashMap<>();
        
        // 时间范围内的用户注册
        long newUsersInRange = getNewUsersInRange(startDate, endDate);
        
        // 时间范围内的捐赠
        BigDecimal moneyDonationsInRange = getMoneyDonationsInRange(startDate, endDate);
        long itemDonationsInRange = getItemDonationsInRange(startDate, endDate);
        
        // 时间范围内的活动
        long newActivitiesInRange = getNewActivitiesInRange(startDate, endDate);

        customData.put("newUsers", newUsersInRange);
        customData.put("moneyDonations", moneyDonationsInRange);
        customData.put("itemDonations", itemDonationsInRange);
        customData.put("newActivities", newActivitiesInRange);
        customData.put("startDate", startDate);
        customData.put("endDate", endDate);

        return customData;
    }

    @Override
    public List<Map<String, Object>> getRegistrationTrend() {
        // 获取最近6个月的用户注册趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            // 这里应该查询数据库获取该月的注册用户数
            // 简化实现，返回示例数据
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStr);
            monthData.put("count", (int)(Math.random() * 100)); // 示例数据
            trend.add(monthData);
        }
        
        return trend;
    }

    @Override
    public List<Map<String, Object>> getDonationTrend() {
        // 获取最近6个月的捐赠趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            // 这里应该查询数据库获取该月的捐赠数据
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStr);
            monthData.put("moneyAmount", new BigDecimal(Math.random() * 10000).setScale(2, BigDecimal.ROUND_HALF_UP));
            monthData.put("itemCount", (int)(Math.random() * 50));
            trend.add(monthData);
        }
        
        return trend;
    }

    @Override
    public List<Map<String, Object>> getItemTypeDistribution() {
        // 获取物品类型分布
        List<Map<String, Object>> distribution = new ArrayList<>();
        
        // 这里应该查询数据库获取真实的物品类型分布
        // 简化实现，返回示例数据
        String[] itemTypes = {"书籍", "衣物", "电子产品", "食品", "其他"};
        for (String type : itemTypes) {
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("type", type);
            typeData.put("count", (int)(Math.random() * 100));
            distribution.add(typeData);
        }
        
        return distribution;
    }

    @Override
    public List<Map<String, Object>> getActivityTrend() {
        // 获取最近6个月的活动创建趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStr);
            monthData.put("count", (int)(Math.random() * 20));
            trend.add(monthData);
        }
        
        return trend;
    }

    @Override
    public List<Map<String, Object>> getHotPosts() {
        // 获取热门帖子（按浏览量排序）
        List<Map<String, Object>> hotPosts = new ArrayList<>();
        
        // 这里应该查询数据库获取真实的热门帖子数据
        // 简化实现，返回示例数据
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("title", "热门帖子" + i);
            postData.put("viewCount", (int)(Math.random() * 1000));
            postData.put("replyCount", (int)(Math.random() * 100));
            postData.put("likeCount", (int)(Math.random() * 50));
            hotPosts.add(postData);
        }
        
        return hotPosts;
    }

    @Override
    public List<Map<String, Object>> getForumTrend() {
        // 获取最近6个月的论坛活跃度趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStr);
            monthData.put("posts", (int)(Math.random() * 100));
            monthData.put("replies", (int)(Math.random() * 300));
            monthData.put("likes", (int)(Math.random() * 200));
            trend.add(monthData);
        }
        
        return trend;
    }

    @Override
    public long getTodayNewUsers() {
        // 获取今日新增用户数
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日注册的用户数
        return (long)(Math.random() * 10); // 示例数据
    }

    @Override
    public BigDecimal getTodayMoneyDonations() {
        // 获取今日金钱捐赠总额
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日的金钱捐赠总额
        return new BigDecimal(Math.random() * 1000).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public long getTodayItemDonations() {
        // 获取今日物品捐赠数量
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日的物品捐赠数量
        return (long)(Math.random() * 20);
    }

    @Override
    public long getTodayNewActivities() {
        // 获取今日新增活动数
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日新增的活动数
        return (long)(Math.random() * 5);
    }

    @Override
    public long getTodayNewPosts() {
        // 获取今日新增帖子数
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日新增的帖子数
        return (long)(Math.random() * 15);
    }

    @Override
    public long getTodayNewReplies() {
        // 获取今日新增回复数
        LocalDate today = LocalDate.now();
        // 这里应该查询数据库获取今日新增的回复数
        return (long)(Math.random() * 50);
    }

    @Override
    public long getNewUsersInRange(String startDate, String endDate) {
        // 获取时间范围内新增用户数
        // 这里应该查询数据库获取指定时间范围内的新增用户数
        return (long)(Math.random() * 100);
    }

    @Override
    public BigDecimal getMoneyDonationsInRange(String startDate, String endDate) {
        // 获取时间范围内金钱捐赠总额
        // 这里应该查询数据库获取指定时间范围内的金钱捐赠总额
        return new BigDecimal(Math.random() * 10000).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public long getItemDonationsInRange(String startDate, String endDate) {
        // 获取时间范围内物品捐赠数量
        // 这里应该查询数据库获取指定时间范围内的物品捐赠数量
        return (long)(Math.random() * 200);
    }

    @Override
    public long getNewActivitiesInRange(String startDate, String endDate) {
        // 获取时间范围内新增活动数
        // 这里应该查询数据库获取指定时间范围内的新增活动数
        return (long)(Math.random() * 30);
    }
} 