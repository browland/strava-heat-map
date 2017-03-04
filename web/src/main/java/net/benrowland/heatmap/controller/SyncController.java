package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import net.benrowland.heatmap.security.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SyncController {
    private static final Logger log = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private StravaUserRepository stravaUserRepository;

    @RequestMapping(path = "/sync", method = RequestMethod.POST)
    public void sync() throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            log.error("No user in principal!");
            throw new ForbiddenException();
        }

        StravaUserEntity stravaUserEntity = stravaUserRepository.findByUsername(user.getUsername());
        if (stravaUserEntity == null) {
            log.error("No strava user for user {}!", user.getUsername());
            throw new ForbiddenException();
        }

        stravaUserEntity.setSyncRequired(true);
        stravaUserRepository.save(stravaUserEntity);
    }
}
