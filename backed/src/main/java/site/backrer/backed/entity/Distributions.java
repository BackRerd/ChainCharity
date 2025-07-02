package site.backrer.backed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName distributions
 */
@TableName(value ="distributions")
@Data
public class Distributions {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer distributionId;

    /**
     * 
     */
    private Integer activityId;

    /**
     * 
     */
    private Integer recipientId;

    /**
     * 
     */
    private Object donationType;

    /**
     * 
     */
    private Integer itemDonationId;

    /**
     * 
     */
    private Integer moneyDonationId;

    /**
     * 
     */
    private Integer distributedQuantity;

    /**
     * 
     */
    private BigDecimal distributedAmount;

    /**
     * 
     */
    private Date distributionTime;

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
        Distributions other = (Distributions) that;
        return (this.getDistributionId() == null ? other.getDistributionId() == null : this.getDistributionId().equals(other.getDistributionId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getRecipientId() == null ? other.getRecipientId() == null : this.getRecipientId().equals(other.getRecipientId()))
            && (this.getDonationType() == null ? other.getDonationType() == null : this.getDonationType().equals(other.getDonationType()))
            && (this.getItemDonationId() == null ? other.getItemDonationId() == null : this.getItemDonationId().equals(other.getItemDonationId()))
            && (this.getMoneyDonationId() == null ? other.getMoneyDonationId() == null : this.getMoneyDonationId().equals(other.getMoneyDonationId()))
            && (this.getDistributedQuantity() == null ? other.getDistributedQuantity() == null : this.getDistributedQuantity().equals(other.getDistributedQuantity()))
            && (this.getDistributedAmount() == null ? other.getDistributedAmount() == null : this.getDistributedAmount().equals(other.getDistributedAmount()))
            && (this.getDistributionTime() == null ? other.getDistributionTime() == null : this.getDistributionTime().equals(other.getDistributionTime()))
            && (this.getApprovalStatus() == null ? other.getApprovalStatus() == null : this.getApprovalStatus().equals(other.getApprovalStatus()))
            && (this.getReviewedBy() == null ? other.getReviewedBy() == null : this.getReviewedBy().equals(other.getReviewedBy()))
            && (this.getReviewedAt() == null ? other.getReviewedAt() == null : this.getReviewedAt().equals(other.getReviewedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDistributionId() == null) ? 0 : getDistributionId().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getRecipientId() == null) ? 0 : getRecipientId().hashCode());
        result = prime * result + ((getDonationType() == null) ? 0 : getDonationType().hashCode());
        result = prime * result + ((getItemDonationId() == null) ? 0 : getItemDonationId().hashCode());
        result = prime * result + ((getMoneyDonationId() == null) ? 0 : getMoneyDonationId().hashCode());
        result = prime * result + ((getDistributedQuantity() == null) ? 0 : getDistributedQuantity().hashCode());
        result = prime * result + ((getDistributedAmount() == null) ? 0 : getDistributedAmount().hashCode());
        result = prime * result + ((getDistributionTime() == null) ? 0 : getDistributionTime().hashCode());
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
        sb.append(", distributionId=").append(distributionId);
        sb.append(", activityId=").append(activityId);
        sb.append(", recipientId=").append(recipientId);
        sb.append(", donationType=").append(donationType);
        sb.append(", itemDonationId=").append(itemDonationId);
        sb.append(", moneyDonationId=").append(moneyDonationId);
        sb.append(", distributedQuantity=").append(distributedQuantity);
        sb.append(", distributedAmount=").append(distributedAmount);
        sb.append(", distributionTime=").append(distributionTime);
        sb.append(", approvalStatus=").append(approvalStatus);
        sb.append(", reviewedBy=").append(reviewedBy);
        sb.append(", reviewedAt=").append(reviewedAt);
        sb.append("]");
        return sb.toString();
    }
}