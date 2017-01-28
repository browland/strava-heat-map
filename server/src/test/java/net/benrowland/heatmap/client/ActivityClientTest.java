package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.Activity;
import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityClientTest {
    private static final String ACTIVITIES_ENDPOINT = "url";
    private static final String ACCESS_TOKEN = "access_token";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ActivityClient activityClient;

    @Before
    public void setUp() {
        activityClient = new ActivityClient(restTemplate, ACTIVITIES_ENDPOINT);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiAndReturnsActivities() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(restTemplate.exchange(eq(ACTIVITIES_ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(Activity[].class)))
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
        when(restTemplate.exchange(eq(ACTIVITIES_ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(Activity[].class)))
                .thenReturn(responseEntity);

        Activity[] activitiesResponse = activityClient.getActivities(stravaUserEntity);

        assertThat(activitiesResponse).hasSize(0);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiWithAuthenticationHeaders() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(restTemplate.exchange(eq(ACTIVITIES_ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(Activity[].class)))
            .thenReturn(responseEntity);

        activityClient.getActivities(stravaUserEntity);

        verify(restTemplate).exchange(eq(ACTIVITIES_ENDPOINT),
                eq(HttpMethod.GET),
                httpEntityArgumentCaptor.capture(),
                eq(Activity[].class)
        );

        assertThat(httpEntityArgumentCaptor.getValue().getHeaders()).containsKey("Authorization");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaApiReturnsNonSuccessThrowsStravaApiException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(restTemplate.exchange(eq(ACTIVITIES_ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(Activity[].class)))
            .thenThrow(HttpClientErrorException.class);

        assertThatThrownBy(() -> activityClient.getActivities(stravaUserEntity))
            .isInstanceOf(StravaApiException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaApiCommunicationFailureThrowsStravaApiException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ResponseEntity<Activity[]> responseEntity = Mockito.mock(ResponseEntity.class);

        when(responseEntity.getBody()).thenReturn(getActivities());
        when(restTemplate.exchange(eq(ACTIVITIES_ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(Activity[].class)))
                .thenThrow(ResourceAccessException.class);

        assertThatThrownBy(() -> activityClient.getActivities(stravaUserEntity))
                .isInstanceOf(StravaApiException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaUserEntityNullThrowsIllegalArgumentException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = null;

        assertThatThrownBy(() -> activityClient.getActivities(stravaUserEntity))
                .isInstanceOf(IllegalArgumentException.class);

        verify(restTemplate, never()).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaUserEntityHasNoAccessTokenThrowsIllegalArgumentException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(null);

        assertThatThrownBy(() -> activityClient.getActivities(stravaUserEntity))
                .isInstanceOf(IllegalArgumentException.class);

        verify(restTemplate, never()).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
    }

    private Activity[] getActivities() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        Activity activity3 = new Activity();
        return new Activity[] {activity1, activity2, activity3};
    }
}
