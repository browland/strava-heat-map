package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.security.ForbiddenException;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.dto.Stream;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import net.benrowland.heatmap.service.RecentActivitiesStreamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LatestActivityController {
    private static final Logger log = LoggerFactory.getLogger(LatestActivityController.class);

    @Autowired
    private StravaUserRepository stravaUserRepository;

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;

    @RequestMapping(path = "/latestActivity", produces = "application/json")
    public Stream[] getLatestActivity() throws StravaApiException, IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user == null) {
            log.error("No user in principal!");
            throw new ForbiddenException();
        }

        StravaUserEntity stravaUserEntity = stravaUserRepository.findByUsername(user.getUsername());
        if(stravaUserEntity == null) {
            log.error("No strava_user found for user {}!", user.getUsername());
            throw new ForbiddenException();
        }

        return recentActivitiesStreamsService.streamsForRecentActivities(stravaUserEntity);
    }
}
