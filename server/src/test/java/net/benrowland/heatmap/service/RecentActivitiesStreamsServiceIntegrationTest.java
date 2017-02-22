package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"strava.api.getActivities.endpoint=http://localhost:9000/activities",
                                  "strava.api.getStreams.endpoint=http://localhost:9000/streams"})
public class RecentActivitiesStreamsServiceIntegrationTest {
    private static final String ACCESS_TOKEN = "abc";

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9000));

    @Test
    public void stravaApiReturnsNoActivitiesReturnsNoStreams() throws StravaApiException, JsonProcessingException {
        stubStravaActivitiesApiReturnsEmptyActivities();

        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        assertThat(recentActivitiesStreamsService.streamsForRecentActivities(stravaUserEntity)).isEmpty();
    }

    private void stubStravaActivitiesApiReturnsEmptyActivities() {
        stubFor(get(urlEqualTo("/activities"))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("[]")));
    }
}
