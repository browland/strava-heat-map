package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "stream")
public class StreamEntity {
    @Id
    @Column(nullable = false, name = "id")
    private Long activityId;

    @Lob
    @Column(nullable = false, name = "latLngData", length = 10000)
    private String latLngStream;

    @Column(nullable = false, name = "strava_username")
    private String stravaUsername;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getLatLngStream() {
        return latLngStream;
    }

    public void setLatLngStream(String latLngStream) {
        this.latLngStream = latLngStream;
    }

    public String getStravaUsername() {
        return stravaUsername;
    }

    public void setStravaUsername(String stravaUsername) {
        this.stravaUsername = stravaUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamEntity that = (StreamEntity) o;

        if (!activityId.equals(that.activityId)) return false;
        if (!latLngStream.equals(that.latLngStream)) return false;
        return stravaUsername.equals(that.stravaUsername);
    }

    @Override
    public int hashCode() {
        int result = activityId.hashCode();
        result = 31 * result + latLngStream.hashCode();
        result = 31 * result + stravaUsername.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StreamEntity{" +
                "activityId=" + activityId +
                ", latLngStream='" + latLngStream + '\'' +
                ", stravaUsername='" + stravaUsername + '\'' +
                '}';
    }
}
