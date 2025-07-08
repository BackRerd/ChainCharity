package site.backrer.backed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName view_activity_participation_details
 */
@TableName(value ="view_activity_participation_details")
@Data
public class ViewActivityParticipationDetails {
    /**
     * 
     */
    private Integer participationId;

    /**
     * 
     */
    private Integer activityId;

    /**
     * 
     */
    private String activityTitle;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private String participantUsername;

    /**
     * 
     */
    private Object role;

    /**
     * 
     */
    private Date participationTime;

    /**
     * 
     */
    private Object approvalStatus;

    /**
     * 
     */
    private Integer reviewedBy;

    /**
     * 
     */
    private Date reviewedAt;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ViewActivityParticipationDetails other = (ViewActivityParticipationDetails) that;
        return (this.getParticipationId() == null ? other.getParticipationId() == null : this.getParticipationId().equals(other.getParticipationId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getActivityTitle() == null ? other.getActivityTitle() == null : this.getActivityTitle().equals(other.getActivityTitle()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getParticipantUsername() == null ? other.getParticipantUsername() == null : this.getParticipantUsername().equals(other.getParticipantUsername()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getParticipationTime() == null ? other.getParticipationTime() == null : this.getParticipationTime().equals(other.getParticipationTime()))
            && (this.getApprovalStatus() == null ? other.getApprovalStatus() == null : this.getApprovalStatus().equals(other.getApprovalStatus()))
            && (this.getReviewedBy() == null ? other.getReviewedBy() == null : this.getReviewedBy().equals(other.getReviewedBy()))
            && (this.getReviewedAt() == null ? other.getReviewedAt() == null : this.getReviewedAt().equals(other.getReviewedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getParticipationId() == null) ? 0 : getParticipationId().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getActivityTitle() == null) ? 0 : getActivityTitle().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getParticipantUsername() == null) ? 0 : getParticipantUsername().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getParticipationTime() == null) ? 0 : getParticipationTime().hashCode());
        result = prime * result + ((getApprovalStatus() == null) ? 0 : getApprovalStatus().hashCode());
        result = prime * result + ((getReviewedBy() == null) ? 0 : getReviewedBy().hashCode());
        result = prime * result + ((getReviewedAt() == null) ? 0 : getReviewedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", participationId=").append(participationId);
        sb.append(", activityId=").append(activityId);
        sb.append(", activityTitle=").append(activityTitle);
        sb.append(", userId=").append(userId);
        sb.append(", participantUsername=").append(participantUsername);
        sb.append(", role=").append(role);
        sb.append(", participationTime=").append(participationTime);
        sb.append(", approvalStatus=").append(approvalStatus);
        sb.append(", reviewedBy=").append(reviewedBy);
        sb.append(", reviewedAt=").append(reviewedAt);
        sb.append("]");
        return sb.toString();
    }
}