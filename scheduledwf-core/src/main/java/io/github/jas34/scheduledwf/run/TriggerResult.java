package io.github.jas34.scheduledwf.run;

import java.util.Objects;

/**
 * @author Jasbir Singh
 */
public class TriggerResult extends Result {

    private Long createTime;
    private Long updateTime;
    private String createdBy;
    private String updatedBy;

    public TriggerResult() {
        this.createTime = System.currentTimeMillis();
    }

    public TriggerResult(String id) {
        this(id, null, null, null);
    }

    public TriggerResult(String id, Status status, Throwable exception, String createdBy) {
        super(id, status, exception);
        this.createTime = System.currentTimeMillis();
        this.createdBy = createdBy;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFailureReason() {
        if(Objects.isNull(getException())) {
            return null;
        }
        return getException().getMessage() + "; cause: " + getException().getCause();
    }

    @Override
    public String toString() {
        return "TriggerResult{" + "createTime=" + createTime + ", updateTime=" + updateTime + ", createdBy='"
                + createdBy + '\'' + ", updatedBy='" + updatedBy + '\'' + "} " + super.toString();
    }
}
