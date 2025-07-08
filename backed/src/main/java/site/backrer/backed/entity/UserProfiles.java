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
 * @TableName user_profiles
 */
@TableName(value ="user_profiles")
@Data
public class UserProfiles {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer profileId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Object userType;

    /**
     * 个人实名/企业名称
     */
    private String realName;

    /**
     * 身份证号/统一社会信用代码
     */
    private String idCardNumber;

    /**
     * 头像/企业LOGO
     */
    private String avatar;

    /**
     * 
     */
    private Object gender;

    /**
     * 
     */
    private Date birthDate;

    /**
     * 
     */
    private String address;

    /**
     * 受助者银行卡号
     */
    private String bankAccount;

    /**
     * 
     */
    private String bankName;

    /**
     * 需求说明文件URL
     */
    private String needsDescriptionFile;

    /**
     * 家庭成员数
     */
    private Integer familyMembers;

    /**
     * 月收入
     */
    private BigDecimal monthlyIncome;

    /**
     * 
     */
    private Object povertyLevel;

    /**
     * 企业法人
     */
    private String legalRepresentative;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 
     */
    private Object enterpriseType;

    /**
     * 
     */
    private String industry;

    /**
     * 
     */
    private String contactPerson;

    /**
     * 
     */
    private String contactPhone;

    /**
     * 企业可扮演角色
     */
    private Object enterpriseRole;

    /**
     * 实名认证状态
     */
    private Integer idVerified;

    /**
     * 银行卡认证状态(受助者)
     */
    private Integer bankVerified;

    /**
     * 企业认证状态
     */
    private Integer enterpriseVerified;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

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
        UserProfiles other = (UserProfiles) that;
        return (this.getProfileId() == null ? other.getProfileId() == null : this.getProfileId().equals(other.getProfileId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
            && (this.getIdCardNumber() == null ? other.getIdCardNumber() == null : this.getIdCardNumber().equals(other.getIdCardNumber()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getBirthDate() == null ? other.getBirthDate() == null : this.getBirthDate().equals(other.getBirthDate()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getBankAccount() == null ? other.getBankAccount() == null : this.getBankAccount().equals(other.getBankAccount()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getNeedsDescriptionFile() == null ? other.getNeedsDescriptionFile() == null : this.getNeedsDescriptionFile().equals(other.getNeedsDescriptionFile()))
            && (this.getFamilyMembers() == null ? other.getFamilyMembers() == null : this.getFamilyMembers().equals(other.getFamilyMembers()))
            && (this.getMonthlyIncome() == null ? other.getMonthlyIncome() == null : this.getMonthlyIncome().equals(other.getMonthlyIncome()))
            && (this.getPovertyLevel() == null ? other.getPovertyLevel() == null : this.getPovertyLevel().equals(other.getPovertyLevel()))
            && (this.getLegalRepresentative() == null ? other.getLegalRepresentative() == null : this.getLegalRepresentative().equals(other.getLegalRepresentative()))
            && (this.getBusinessLicense() == null ? other.getBusinessLicense() == null : this.getBusinessLicense().equals(other.getBusinessLicense()))
            && (this.getEnterpriseType() == null ? other.getEnterpriseType() == null : this.getEnterpriseType().equals(other.getEnterpriseType()))
            && (this.getIndustry() == null ? other.getIndustry() == null : this.getIndustry().equals(other.getIndustry()))
            && (this.getContactPerson() == null ? other.getContactPerson() == null : this.getContactPerson().equals(other.getContactPerson()))
            && (this.getContactPhone() == null ? other.getContactPhone() == null : this.getContactPhone().equals(other.getContactPhone()))
            && (this.getEnterpriseRole() == null ? other.getEnterpriseRole() == null : this.getEnterpriseRole().equals(other.getEnterpriseRole()))
            && (this.getIdVerified() == null ? other.getIdVerified() == null : this.getIdVerified().equals(other.getIdVerified()))
            && (this.getBankVerified() == null ? other.getBankVerified() == null : this.getBankVerified().equals(other.getBankVerified()))
            && (this.getEnterpriseVerified() == null ? other.getEnterpriseVerified() == null : this.getEnterpriseVerified().equals(other.getEnterpriseVerified()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProfileId() == null) ? 0 : getProfileId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getIdCardNumber() == null) ? 0 : getIdCardNumber().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getBirthDate() == null) ? 0 : getBirthDate().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getBankAccount() == null) ? 0 : getBankAccount().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getNeedsDescriptionFile() == null) ? 0 : getNeedsDescriptionFile().hashCode());
        result = prime * result + ((getFamilyMembers() == null) ? 0 : getFamilyMembers().hashCode());
        result = prime * result + ((getMonthlyIncome() == null) ? 0 : getMonthlyIncome().hashCode());
        result = prime * result + ((getPovertyLevel() == null) ? 0 : getPovertyLevel().hashCode());
        result = prime * result + ((getLegalRepresentative() == null) ? 0 : getLegalRepresentative().hashCode());
        result = prime * result + ((getBusinessLicense() == null) ? 0 : getBusinessLicense().hashCode());
        result = prime * result + ((getEnterpriseType() == null) ? 0 : getEnterpriseType().hashCode());
        result = prime * result + ((getIndustry() == null) ? 0 : getIndustry().hashCode());
        result = prime * result + ((getContactPerson() == null) ? 0 : getContactPerson().hashCode());
        result = prime * result + ((getContactPhone() == null) ? 0 : getContactPhone().hashCode());
        result = prime * result + ((getEnterpriseRole() == null) ? 0 : getEnterpriseRole().hashCode());
        result = prime * result + ((getIdVerified() == null) ? 0 : getIdVerified().hashCode());
        result = prime * result + ((getBankVerified() == null) ? 0 : getBankVerified().hashCode());
        result = prime * result + ((getEnterpriseVerified() == null) ? 0 : getEnterpriseVerified().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", profileId=").append(profileId);
        sb.append(", userId=").append(userId);
        sb.append(", userType=").append(userType);
        sb.append(", realName=").append(realName);
        sb.append(", idCardNumber=").append(idCardNumber);
        sb.append(", avatar=").append(avatar);
        sb.append(", gender=").append(gender);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", address=").append(address);
        sb.append(", bankAccount=").append(bankAccount);
        sb.append(", bankName=").append(bankName);
        sb.append(", needsDescriptionFile=").append(needsDescriptionFile);
        sb.append(", familyMembers=").append(familyMembers);
        sb.append(", monthlyIncome=").append(monthlyIncome);
        sb.append(", povertyLevel=").append(povertyLevel);
        sb.append(", legalRepresentative=").append(legalRepresentative);
        sb.append(", businessLicense=").append(businessLicense);
        sb.append(", enterpriseType=").append(enterpriseType);
        sb.append(", industry=").append(industry);
        sb.append(", contactPerson=").append(contactPerson);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", enterpriseRole=").append(enterpriseRole);
        sb.append(", idVerified=").append(idVerified);
        sb.append(", bankVerified=").append(bankVerified);
        sb.append(", enterpriseVerified=").append(enterpriseVerified);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}