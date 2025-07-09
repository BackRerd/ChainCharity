package site.backrer.backed.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName forum_posts
 */
@TableName(value ="forum_posts")
@Data
public class ForumPosts {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer postId;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer categoryId;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer viewCount;

    /**
     * 
     */
    private Integer replyCount;

    /**
     * 
     */
    private Date lastReplyAt;

    /**
     * 
     */
    private Integer isSticky;

    /**
     * 精华帖
     */
    private Integer isEssence;

    /**
     * 是否关闭回复
     */
    private Integer isClosed;

    /**
     * 
     */
    private Object status;

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
        ForumPosts other = (ForumPosts) that;
        return (this.getPostId() == null ? other.getPostId() == null : this.getPostId().equals(other.getPostId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getReplyCount() == null ? other.getReplyCount() == null : this.getReplyCount().equals(other.getReplyCount()))
            && (this.getLastReplyAt() == null ? other.getLastReplyAt() == null : this.getLastReplyAt().equals(other.getLastReplyAt()))
            && (this.getIsSticky() == null ? other.getIsSticky() == null : this.getIsSticky().equals(other.getIsSticky()))
            && (this.getIsEssence() == null ? other.getIsEssence() == null : this.getIsEssence().equals(other.getIsEssence()))
            && (this.getIsClosed() == null ? other.getIsClosed() == null : this.getIsClosed().equals(other.getIsClosed()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPostId() == null) ? 0 : getPostId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
        result = prime * result + ((getReplyCount() == null) ? 0 : getReplyCount().hashCode());
        result = prime * result + ((getLastReplyAt() == null) ? 0 : getLastReplyAt().hashCode());
        result = prime * result + ((getIsSticky() == null) ? 0 : getIsSticky().hashCode());
        result = prime * result + ((getIsEssence() == null) ? 0 : getIsEssence().hashCode());
        result = prime * result + ((getIsClosed() == null) ? 0 : getIsClosed().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", postId=").append(postId);
        sb.append(", userId=").append(userId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", replyCount=").append(replyCount);
        sb.append(", lastReplyAt=").append(lastReplyAt);
        sb.append(", isSticky=").append(isSticky);
        sb.append(", isEssence=").append(isEssence);
        sb.append(", isClosed=").append(isClosed);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}