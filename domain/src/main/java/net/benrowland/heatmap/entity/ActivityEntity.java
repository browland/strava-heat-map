package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "activity")
public class ActivityEntity {
    @Id
    @Column(nullable = false, name = "id")
    private Long id;
}
