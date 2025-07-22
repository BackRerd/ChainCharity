package site.backrer.backed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.*;
import site.backrer.backed.service.*;
import site.backrer.backed.utils.Result;

import java.math.BigDecimal;
import java.util.*;

/**
 * 大数据可视化接口控制器
 * 提供各种统计数据和可视化接口
 * 
 * 主要功能：
 * 1. 总体概览数据 - 系统整体运营数据概览
 * 2. 用户分析数据 - 用户注册、类型分布、状态分析
 * 3. 捐赠分析数据 - 金钱和物品捐赠统计、趋势分析
 * 4. 活动分析数据 - 活动状态、参与度分析
 * 5. 论坛分析数据 - 帖子、回复、点赞统计
 * 6. 实时数据 - 今日新增数据统计
 * 7. 自定义时间范围数据 - 指定时间段的统计分析
 * 
 * API接口列表：
 * - GET /api/visualization/overview - 获取总体概览数据
 * - GET /api/visualization/users/analysis - 获取用户分析数据
 * - GET /api/visualization/donations/analysis - 获取捐赠分析数据
 * - GET /api/visualization/activities/analysis - 获取活动分析数据
 * - GET /api/visualization/forum/analysis - 获取论坛分析数据
 * - GET /api/visualization/realtime - 获取实时数据
 * - GET /api/visualization/custom-range?startDate=xxx&endDate=xxx - 获取自定义时间范围数据
 * 
 * 返回数据格式：
 * {
 *   "code": "200",
 *   "msg": "请求成功",
 *   "data": {
 *     // 具体数据内容
 *   }
 * }
 */
@RestController
@RequestMapping("/api/visualization")
public class DataVisualizationController {

    @Autowired
    private DataVisualizationService dataVisualizationService;

    /**
     * 获取总体概览数据
     */
    @GetMapping("/overview")
    public Result getOverview() {
        try {
            Map<String, Object> overview = dataVisualizationService.getOverviewData();
            return Result.success(overview);
        } catch (Exception e) {
            return Result.error("获取概览数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户分析数据
     */
    @GetMapping("/users/analysis")
    public Result getUserAnalysis() {
        try {
            Map<String, Object> analysis = dataVisualizationService.getUserAnalysisData();
            return Result.success(analysis);
        } catch (Exception e) {
            return Result.error("获取用户分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取捐赠分析数据
     */
    @GetMapping("/donations/analysis")
    public Result getDonationAnalysis() {
        try {
            Map<String, Object> analysis = dataVisualizationService.getDonationAnalysisData();
            return Result.success(analysis);
        } catch (Exception e) {
            return Result.error("获取捐赠分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取活动分析数据
     */
    @GetMapping("/activities/analysis")
    public Result getActivityAnalysis() {
        try {
            Map<String, Object> analysis = dataVisualizationService.getActivityAnalysisData();
            return Result.success(analysis);
        } catch (Exception e) {
            return Result.error("获取活动分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取论坛分析数据
     */
    @GetMapping("/forum/analysis")
    public Result getForumAnalysis() {
        try {
            Map<String, Object> analysis = dataVisualizationService.getForumAnalysisData();
            return Result.success(analysis);
        } catch (Exception e) {
            return Result.error("获取论坛分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取实时数据
     */
    @GetMapping("/realtime")
    public Result getRealtimeData() {
        try {
            Map<String, Object> realtime = dataVisualizationService.getRealtimeData();
            return Result.success(realtime);
        } catch (Exception e) {
            return Result.error("获取实时数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取自定义时间范围的数据
     */
    @GetMapping("/custom-range")
    public Result getCustomRangeData(@RequestParam String startDate, 
                                   @RequestParam String endDate) {
        try {
            Map<String, Object> customData = dataVisualizationService.getCustomRangeData(startDate, endDate);
            return Result.success(customData);
        } catch (Exception e) {
            return Result.error("获取自定义范围数据失败: " + e.getMessage());
        }
    }


} 