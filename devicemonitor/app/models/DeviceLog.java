package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_log")
public class DeviceLog {

    @Id
    public Long id;

    @Column(name = "device_id")
    public Long deviceId;

    public String status;

    @Column(name = "execution_hours")
    public int executionHours;

    public String error;

    @Column(name = "update_at")
    public Long updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getExecutionHours() {
        return executionHours;
    }

    public void setExecutionHours(int executionHours) {
        this.executionHours = executionHours;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }
}
