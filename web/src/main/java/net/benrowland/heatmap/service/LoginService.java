package net.benrowland.heatmap.service;

import net.benrowland.heatmap.entity.UserEntity;
import net.benrowland.heatmap.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserRepository userRepository;

    public boolean authorise(String username, String password) {
        UserEntity userEntity = userRepository.findByUsernameAndPassword(username, password);
        logger.info("User found: {}", userEntity);

        if(userEntity == null) {
            logger.info("User failed authentication");
            return false;
        }

        return true;
    }
}
