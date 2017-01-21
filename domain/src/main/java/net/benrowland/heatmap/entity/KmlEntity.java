package net.benrowland.heatmap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "kml")
public class KmlEntity {
    @Id
    @Column(nullable = false, name = "strava_username")
    private String stravaUsername;

    @Lob
    @Column(nullable = false, name = "kml_document", length = 100000000)
    private String kmlDocument;

    public String getStravaUsername() {
        return stravaUsername;
    }

    public void setStravaUsername(String stravaUsername) {
        this.stravaUsername = stravaUsername;
    }

    public String getKmlDocument() {
        return kmlDocument;
    }

    public void setKmlDocument(String kmlDocument) {
        this.kmlDocument = kmlDocument;
    }
}
