package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.ForbiddenException;
import net.benrowland.heatmap.entity.KmlEntity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.KmlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class KmlController {
    private static final Logger logger = LoggerFactory.getLogger(KmlController.class);

    @Autowired
    private KmlRepository kmlRepository;

    @RequestMapping(path = "/kml", produces = "application/xml")
    public String getKml(HttpSession httpSession) {
        StravaUserEntity stravaUserEntity = (StravaUserEntity) httpSession.getAttribute("StravaUser");
        if(stravaUserEntity == null) {
            throw new ForbiddenException();
        }

        KmlEntity kmlEntity = kmlRepository.findByStravaUsername(stravaUserEntity.getStravaUsername());
        return kmlEntity.getKmlDocument();
    }
}
