package net.benrowland.heatmap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.Lists;
import net.benrowland.heatmap.client.StravaApiException;
import net.benrowland.heatmap.entity.StravaUserEntity;
import net.benrowland.heatmap.entity.StreamEntity;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
    private static final String STRAVA_USERNAME = "bob";

    @Autowired
    private RecentActivitiesStreamsService recentActivitiesStreamsService;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9000));

    @Test
    public void stravaApiReturnsNoActivitiesReturnsNoStreams() throws StravaApiException, JsonProcessingException {
        stubStravaActivitiesApiReturnsEmptyActivities();

        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        assertThat(recentActivitiesStreamsService.streamsForRecentActivitiesAndUpdateLastActivityDate(stravaUserEntity)).isEmpty();
    }

    @Test
    public void stravaApiReturnsManualActivityDoesNotRequestStream() throws StravaApiException, IOException {
        stubStravaActivitiesApiReturnsManualActivity();

        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        assertThat(recentActivitiesStreamsService.streamsForRecentActivitiesAndUpdateLastActivityDate(stravaUserEntity)).isEmpty();
    }

    @Test
    public void stravaApiReturnsActivityRequestsStreamReturnsStream() throws StravaApiException, IOException {
        stubStravaActivitiesApiReturnsActivityAndStream();

        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setStravaUsername(STRAVA_USERNAME);
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        StreamEntity expectedStreamEntity = new StreamEntity();
        expectedStreamEntity.setStravaUsername(STRAVA_USERNAME);
        expectedStreamEntity.setActivityId(8529483L);
        expectedStreamEntity.setLatLngStream("[{\"lat\":0.0,\"lng\":1.0},{\"lat\":-2.0,\"lng\":3.0}]");

        List<StreamEntity> expectedStreams = Lists.newArrayList(expectedStreamEntity);
        LocalDateTime expectedLastActivityDateTime = LocalDateTime.of(2013, 8, 23, 17, 4, 12);

        assertThat(recentActivitiesStreamsService.streamsForRecentActivitiesAndUpdateLastActivityDate(stravaUserEntity)).isEqualTo(expectedStreams);
        assertThat(stravaUserEntity.getLastActivityDatetime()).isEqualTo(expectedLastActivityDateTime);
    }

    private void stubStravaActivitiesApiReturnsEmptyActivities() {
        stubFor(get(urlEqualTo("/activities?per_page=100"))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody("[]")));
    }

    private void stubStravaActivitiesApiReturnsManualActivity() throws IOException {
        final ClassPathResource cpr = new ClassPathResource("net/benrowland/heatmap/service/activity-manual.json");
        final String activityJson = IOUtils.toString(cpr.getInputStream());

        stubFor(get(urlEqualTo("/activities?per_page=100"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(activityJson)));
    }

    private void stubStravaActivitiesApiReturnsActivityAndStream() throws IOException {
        final ClassPathResource activityCpr = new ClassPathResource("net/benrowland/heatmap/service/activity-non-manual.json");
        final String activityJson = IOUtils.toString(activityCpr.getInputStream());

        stubFor(get(urlEqualTo("/activities?per_page=100"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(activityJson)));

        final ClassPathResource streamCpr = new ClassPathResource("net/benrowland/heatmap/service/stream.json");
        final String streamJson = IOUtils.toString(streamCpr.getInputStream());

        stubFor(get(urlEqualTo("/streams"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                .withBody(streamJson)));
    }
}
