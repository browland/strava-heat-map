package net.benrowland.heatmap.client;

import net.benrowland.heatmap.entity.StravaUserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
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
public class StravaApiTest {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ENDPOINT = "endpoint";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StravaApi stravaApi;

    @Test
    @SuppressWarnings("unchecked")
    public void callsStravaApiWithAuthenticationHeaders() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        stravaApi.call(stravaUserEntity, ENDPOINT, String.class);

        verify(restTemplate).exchange(eq(ENDPOINT),
                eq(HttpMethod.GET),
                httpEntityArgumentCaptor.capture(),
                eq(String.class)
        );

        assertThat(httpEntityArgumentCaptor.getValue().getHeaders()).containsKey("Authorization");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaUserEntityHasNoAccessTokenThrowsIllegalArgumentException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(null);

        assertThatThrownBy(() -> stravaApi.call(stravaUserEntity, ENDPOINT, String.class))
                .isInstanceOf(IllegalArgumentException.class);

        verify(restTemplate, never()).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stravaUserEntityNullThrowsIllegalArgumentException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = null;

        assertThatThrownBy(() -> stravaApi.call(stravaUserEntity, ENDPOINT, String.class))
                .isInstanceOf(IllegalArgumentException.class);

        verify(restTemplate, never()).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void forbiddenStatusCodeRethrownAsStravaApiException() throws StravaApiException {
        final StravaUserEntity stravaUserEntity = new StravaUserEntity();
        stravaUserEntity.setAccessToken(ACCESS_TOKEN);

        when(restTemplate.exchange(eq(ENDPOINT), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        assertThatThrownBy(() -> stravaApi.call(stravaUserEntity, ENDPOINT, String.class))
                .isInstanceOf(StravaApiException.class);
    }
}
