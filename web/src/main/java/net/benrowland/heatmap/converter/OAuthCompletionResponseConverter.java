package net.benrowland.heatmap.converter;

import net.benrowland.heatmap.dto.OAuthCompletionResponse;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.springframework.stereotype.Component;

@Component
public class OAuthCompletionResponseConverter {

    public StravaUserEntity convert(String username, OAuthCompletionResponse oAuthCompletionResponse) {
        StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setUsername(username);
        stravaUserEntity.setStravaUsername(oAuthCompletionResponse.getAthlete().getUsername());
        stravaUserEntity.setAccessToken(oAuthCompletionResponse.getAccessToken());

        return stravaUserEntity;
    }
}
