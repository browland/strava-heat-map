package net.benrowland.heatmap.service;

import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import net.benrowland.heatmap.repository.UserRepository;
import net.benrowland.heatmap.security.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Service
public class SyncService {
    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StravaUserRepository stravaUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void syncUser(final String username) {
        StravaUserEntity stravaUserEntity = stravaUserRepository.findByUsername(username);
        if (stravaUserEntity == null) {
            log.error("No strava user for user {}!", username);
            throw new ForbiddenException();
        }

        stravaUserEntity.setSyncRequired(true);
        stravaUserRepository.save(stravaUserEntity);
    }

    public boolean syncState(final String username) {
        StravaUserEntity stravaUserEntity = stravaUserRepository.findByUsername(username);

        if (stravaUserEntity == null) {
            log.error("No strava user for user {}!", username);
            throw new ForbiddenException();
        }

        entityManager.refresh(stravaUserEntity);
        return stravaUserEntity.isSyncRequired();
    }
}
