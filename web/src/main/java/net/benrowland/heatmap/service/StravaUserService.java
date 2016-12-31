package net.benrowland.heatmap.service;

import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StravaUserService {

    @Autowired
    private StravaUserRepository stravaUserRepository;

    public StravaUserEntity findStravaUser(String username) {
        return stravaUserRepository.findByUsername(username);
    }
}
