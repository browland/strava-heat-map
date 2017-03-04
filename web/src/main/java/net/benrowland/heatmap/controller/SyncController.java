package net.benrowland.heatmap.controller;

import net.benrowland.heatmap.security.ForbiddenException;
import net.benrowland.heatmap.service.SyncService;
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
    private SyncService syncService;

    @RequestMapping(path = "/sync", method = RequestMethod.POST)
    public void sync() throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            log.error("No user in principal!");
            throw new ForbiddenException();
        }

        syncService.syncUser(user.getUsername());

        boolean syncState;
        do {
            syncState = syncService.syncState(user.getUsername());
            try {
                log.info("Waiting for sync to complete on user {} ...", user.getUsername());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("While waiting for sync to complete on user " + user.getUsername(), e);
                break;
            }
        }
        while(syncState);

        log.info("Sync complete for user {} ...", user.getUsername());
    }
}
