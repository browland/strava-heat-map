package net.benrowland.heatmap.service;

import net.benrowland.heatmap.client.OAuthClient;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.converter.OAuthCompletionResponseConverter;
import net.benrowland.heatmap.dto.OAuthCompletionResponse;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {
    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    @Autowired
    private OAuthClient oAuthClient;

    @Autowired
    private OAuthCompletionResponseConverter OAuthCompletionResponseConverter;

    @Autowired
    private StravaUserRepository stravaUserRepository;

    public void authorise(String username, String authorisationCode) throws StravaApiException {
        OAuthCompletionResponse oAuthCompletionResponse = oAuthClient.authorise(authorisationCode);

        StravaUserEntity stravaUserEntity = OAuthCompletionResponseConverter.convert(username, oAuthCompletionResponse);
        stravaUserRepository.save(stravaUserEntity);

        logger.info("Saved session token for OAuth callback {}", stravaUserEntity);
    }
}
