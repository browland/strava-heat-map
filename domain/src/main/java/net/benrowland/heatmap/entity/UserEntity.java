package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user")
public class UserEntity {
    @Id
    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
