package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "strava_user")
public class StravaUserEntity {
    @Id
    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "strava_username")
    private String stravaUsername;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

    @Column(nullable = false, name = "sync_required")
    private boolean syncRequired;

    @Column(nullable = true, name = "last_activity_datetime")
    private LocalDateTime lastActivityDatetime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStravaUsername() {
        return stravaUsername;
    }

    public void setStravaUsername(String stravaUsername) {
        this.stravaUsername = stravaUsername;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isSyncRequired() {
        return syncRequired;
    }

    public void setSyncRequired(boolean syncRequired) {
        this.syncRequired = syncRequired;
    }

    public LocalDateTime getLastActivityDatetime() {
        return lastActivityDatetime;
    }

    public void setLastActivityDatetime(LocalDateTime lastActivityDatetime) {
        this.lastActivityDatetime = lastActivityDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StravaUserEntity that = (StravaUserEntity) o;

        if (syncRequired != that.syncRequired) return false;
        if (!username.equals(that.username)) return false;
        if (!stravaUsername.equals(that.stravaUsername)) return false;
        if (!accessToken.equals(that.accessToken)) return false;
        return lastActivityDatetime != null ? lastActivityDatetime.equals(that.lastActivityDatetime) : that.lastActivityDatetime == null;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + stravaUsername.hashCode();
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + (syncRequired ? 1 : 0);
        result = 31 * result + (lastActivityDatetime != null ? lastActivityDatetime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StravaUserEntity{" +
                "username='" + username + '\'' +
                ", stravaUsername='" + stravaUsername + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", syncRequired=" + syncRequired +
                ", lastActivityDatetime=" + lastActivityDatetime +
                '}';
    }
}
