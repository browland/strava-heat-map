package net.benrowland.heatmap.service;

import net.benrowland.heatmap.client.OAuthClient;
import net.benrowland.heatmap.converter.OAuthCompletionResponseConverter;
import net.benrowland.heatmap.dto.OAuthCompletionResponse;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.repository.StravaUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class OAuthServiceTest {
    private static final String AUTHORISATION_CODE = "aaa111";
    private static final String ACCESS_TOKEN = "abc123";

    @Autowired
    private OAuthService oAuthService;

    @MockBean
    private OAuthClient oAuthClient;

    @MockBean
    private StravaUserRepository stravaUserRepository;

    @MockBean
    private OAuthCompletionResponseConverter OAuthCompletionResponseConverter;

    @Test
    public void test() throws Exception {
        final String username = "username";

        StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        OAuthCompletionResponse oAuthCompletionResponse = new OAuthCompletionResponse();
        oAuthCompletionResponse.setAccessToken(ACCESS_TOKEN);

        given(oAuthClient.authorise(eq(AUTHORISATION_CODE)))
                .willReturn(oAuthCompletionResponse);
        given(OAuthCompletionResponseConverter.convert(eq(username), eq(oAuthCompletionResponse)))
                .willReturn(stravaUserEntity);
        given(stravaUserRepository.save(eq(stravaUserEntity)))
                .willReturn(stravaUserEntity);

        oAuthService.authorise(username, AUTHORISATION_CODE);

        then(oAuthClient).should().authorise(AUTHORISATION_CODE);
        then(OAuthCompletionResponseConverter).should().convert(username, oAuthCompletionResponse);
        then(stravaUserRepository).should().save(stravaUserEntity);
    }
}
