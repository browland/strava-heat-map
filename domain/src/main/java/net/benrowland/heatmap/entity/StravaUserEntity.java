package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "strava_user")
public class StravaUserEntity {
    @Id
    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "strava_username")
    private String stravaUsername;

    @Column(nullable = false, name = "access_token")
    private String accessToken;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StravaUserEntity that = (StravaUserEntity) o;

        if (!username.equals(that.username)) return false;
        if (!stravaUsername.equals(that.stravaUsername)) return false;
        return accessToken.equals(that.accessToken);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + stravaUsername.hashCode();
        result = 31 * result + accessToken.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StravaUserEntity{" +
                "username='" + username + '\'' +
                ", stravaUsername='" + stravaUsername + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
