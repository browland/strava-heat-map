package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityClientTest {
    private static final String ACTIVITIES_ENDPOINT = "http://strava/activities";
    private static final int PAGE_SIZE = 1;
    private static final String ACTIVITIES_URL = ACTIVITIES_ENDPOINT + "?per_page=" + PAGE_SIZE;
    private static final String ACCESS_TOKEN = "access_token";

    @Mock
    private StravaApi stravaApi;

    private ActivityClient activityClient;

    @Before
    public void setUp() {
        activityClient = new ActivityClient(stravaApi, ACTIVITIES_ENDPOINT, PAGE_SIZE);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiAndReturnsActivities() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(stravaApi.call(eq(stravaUserEntity), eq(ACTIVITIES_URL), eq(Activity[].class)))
            .thenReturn(responseEntity);

        Activity[] activitiesResponse = activityClient.getActivities(stravaUserEntity);

        assertThat(activitiesResponse).hasSize(3);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiWithAfterDateAndReturnsActivities() throws StravaApiException {
        final LocalDateTime afterDate = LocalDateTime.of(2017, 3, 3, 18, 11, 0);
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);
        stravaUserEntity.setLastActivityDatetime(afterDate);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        long afterDateSecsSinceEpoch = 1488564660L;
        final String urlWithAfterDate = ACTIVITIES_URL + "&after=" + afterDateSecsSinceEpoch;

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(stravaApi.call(eq(stravaUserEntity), eq(urlWithAfterDate), eq(Activity[].class)))
                .thenReturn(responseEntity);

        Activity[] activitiesResponse = activityClient.getActivities(stravaUserEntity);

        assertThat(activitiesResponse).hasSize(3);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiAndReturnsEmptyActivities() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(new Activity[] {});
        when(stravaApi.call(eq(stravaUserEntity), eq(ACTIVITIES_URL), eq(Activity[].class)))
                .thenReturn(responseEntity);

        Activity[] activitiesResponse = activityClient.getActivities(stravaUserEntity);

        assertThat(activitiesResponse).hasSize(0);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaApiReturnsNonSuccessThrowsStravaApiException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(stravaApi.call(eq(stravaUserEntity), eq(ACTIVITIES_URL), eq(Activity[].class)))
            .thenThrow(HttpClientErrorException.class);

        assertThatThrownBy(() -> activityClient.getActivities(stravaUserEntity))
            .isInstanceOf(StravaApiException.class);
    }

    private Activity[] getActivities() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        Activity activity3 = new Activity();
        return new Activity[] {activity1, activity2, activity3};
    }
}
