package site.backrer.backed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName item_donations
 */
@TableName(value ="item_donations")
@Data
public class ItemDonations {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer donationId;

    /**
     * 
     */
    private Integer activityId;

    /**
     * 
     */
    private Integer donorId;

    /**
     * 
     */
    private String itemType;

    /**
     * 
     */
    private Integer quantity;

    /**
     * 
     */
    private String imagePath;

    /**
     * 
     */
    private Date donationTime;

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

    /**
     * 
     */
    private Object status;

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
        ItemDonations other = (ItemDonations) that;
        return (this.getDonationId() == null ? other.getDonationId() == null : this.getDonationId().equals(other.getDonationId()))
            && (this.getActivityId() == null ? other.getActivityId() == null : this.getActivityId().equals(other.getActivityId()))
            && (this.getDonorId() == null ? other.getDonorId() == null : this.getDonorId().equals(other.getDonorId()))
            && (this.getItemType() == null ? other.getItemType() == null : this.getItemType().equals(other.getItemType()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getImagePath() == null ? other.getImagePath() == null : this.getImagePath().equals(other.getImagePath()))
            && (this.getDonationTime() == null ? other.getDonationTime() == null : this.getDonationTime().equals(other.getDonationTime()))
            && (this.getApprovalStatus() == null ? other.getApprovalStatus() == null : this.getApprovalStatus().equals(other.getApprovalStatus()))
            && (this.getReviewedBy() == null ? other.getReviewedBy() == null : this.getReviewedBy().equals(other.getReviewedBy()))
            && (this.getReviewedAt() == null ? other.getReviewedAt() == null : this.getReviewedAt().equals(other.getReviewedAt()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDonationId() == null) ? 0 : getDonationId().hashCode());
        result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
        result = prime * result + ((getDonorId() == null) ? 0 : getDonorId().hashCode());
        result = prime * result + ((getItemType() == null) ? 0 : getItemType().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getImagePath() == null) ? 0 : getImagePath().hashCode());
        result = prime * result + ((getDonationTime() == null) ? 0 : getDonationTime().hashCode());
        result = prime * result + ((getApprovalStatus() == null) ? 0 : getApprovalStatus().hashCode());
        result = prime * result + ((getReviewedBy() == null) ? 0 : getReviewedBy().hashCode());
        result = prime * result + ((getReviewedAt() == null) ? 0 : getReviewedAt().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", donationId=").append(donationId);
        sb.append(", activityId=").append(activityId);
        sb.append(", donorId=").append(donorId);
        sb.append(", itemType=").append(itemType);
        sb.append(", quantity=").append(quantity);
        sb.append(", imagePath=").append(imagePath);
        sb.append(", donationTime=").append(donationTime);
        sb.append(", approvalStatus=").append(approvalStatus);
        sb.append(", reviewedBy=").append(reviewedBy);
        sb.append(", reviewedAt=").append(reviewedAt);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}