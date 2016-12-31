package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.ForbiddenException;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.service.RecentActivitiesStreamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LatestActivityController {
    private static final Logger logger = LoggerFactory.getLogger(LatestActivityController.class);

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;

    @RequestMapping(path = "/latestActivity", produces = "application/json")
    public Stream[] getLatestActivity(HttpSession httpSession) throws StravaApiException {
        StravaUserEntity stravaUserEntity = (StravaUserEntity) httpSession.getAttribute("StravaUser");
        if(stravaUserEntity == null) {
            throw new ForbiddenException();
        }

        return recentActivitiesStreamsService.streamsForRecentActivities(stravaUserEntity);
    }
}
