package site.backrer.backed.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.backrer.backed.entity.ActivityParticipation;
import site.backrer.backed.entity.ViewActivityParticipationDetails;
import site.backrer.backed.service.ActivityParticipationService;
import site.backrer.backed.service.ViewActivityParticipationDetailsService;
import site.backrer.backed.utils.Result;

@RestController
@RequestMapping("/participations")
public class ActivityParticipationController {
    @Autowired
    private ActivityParticipationService participationService;
    @Autowired
    private ViewActivityParticipationDetailsService viewActivityParticipationDetailsService;
    /**
     * 参与活动报名
     * 请求示例：
     * POST /participations
     * {
     *   "activityId": 1,           // 活动ID
     *   "userId": 2,                // 用户ID
     *   "role": "donor"             // 角色: donor/recipient
     * }
     */
    @PostMapping
    public Result addParticipation(@RequestBody ActivityParticipation participation) {
        participationService.save(participation);
        return Result.success(participation);
    }

    @DeleteMapping("/{id}")
    public Result deleteParticipation(@PathVariable Integer id) {
        participationService.removeById(id);
        return Result.success();
    }

    @PutMapping
    public Result updateParticipation(@RequestBody ActivityParticipation participation) {
        participationService.updateById(participation);
        return Result.success(participation);
    }

    @GetMapping("/{id}")
    public Result getParticipationById(@PathVariable Integer id) {
        ActivityParticipation participation = participationService.getById(id);
        if (participation != null) {
            return Result.success(participation);
        }
        return Result.error("404", "参与记录不存在");
    }

    @GetMapping("/page")
    public Result getParticipationByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer activityId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        Page<ActivityParticipation> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<ActivityParticipation> queryWrapper = new LambdaQueryWrapper<>();

        if (activityId != null) {
            queryWrapper.eq(ActivityParticipation::getActivityId, activityId);
        }
        if (userId != null) {
            queryWrapper.eq(ActivityParticipation::getUserId, userId);
        }
        if (role != null) {
            queryWrapper.eq(ActivityParticipation::getRole, role);
        }
        if (approvalStatus != null) {
            queryWrapper.eq(ActivityParticipation::getApprovalStatus, approvalStatus);
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between(ActivityParticipation::getParticipationTime, startTime, endTime);
        }

        return Result.success(participationService.page(pageInfo, queryWrapper));
    }

    @GetMapping("/page-view")
    public Result getParticipationByPageView(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer activityId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String approvalStatus,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        Page<ViewActivityParticipationDetails> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<ViewActivityParticipationDetails> queryWrapper = new LambdaQueryWrapper<>();

        if (activityId != null) {
            queryWrapper.eq(ViewActivityParticipationDetails::getActivityId, activityId);
        }
        if (userId != null) {
            queryWrapper.eq(ViewActivityParticipationDetails::getUserId, userId);
        }
        if (role != null) {
            queryWrapper.eq(ViewActivityParticipationDetails::getRole, role);
        }
        if (approvalStatus != null) {
            queryWrapper.eq(ViewActivityParticipationDetails::getApprovalStatus, approvalStatus);
        }
        if (startTime != null && endTime != null) {
            queryWrapper.between(ViewActivityParticipationDetails::getParticipationTime, startTime, endTime);
        }

        return Result.success(viewActivityParticipationDetailsService.page(pageInfo, queryWrapper));
    }
}