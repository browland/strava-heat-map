package net.benrowland.heatmap.client;

import net.benrowland.heatmap.dto.OAuthCompletionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OAuthClientTest {
    private static final String completionUri = "completionUri";
    private static final String clientSecret = "clientSecret";
    private static final String clientId = "clientId";

    @Mock
    private RestTemplate restTemplate;

    private OAuthClient oAuthClient;

    @Before
    public void setUp() {
        oAuthClient = new OAuthClient(restTemplate, completionUri, clientSecret, clientId);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void successfulAuthorisationCallReturnsResponse() throws StravaApiException {
        final String authorisationCode = "authCode";
        final OAuthCompletionResponse completionResponse = new OAuthCompletionResponse();

        ResponseEntity<OAuthCompletionResponse> responseEntity = mock(ResponseEntity.class);

        given(restTemplate.postForEntity(eq(completionUri), any(HttpEntity.class), eq(OAuthCompletionResponse.class)))
            .willReturn(responseEntity);
        given(responseEntity.getStatusCode())
            .willReturn(HttpStatus.OK);
        given(responseEntity.getBody())
            .willReturn(completionResponse);

        assertThat(oAuthClient.authorise(authorisationCode)).isEqualTo(completionResponse);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void authorisationCallPopulatesContentTypeHeader() throws StravaApiException {
        final String authorisationCode = "authCode";
        final OAuthCompletionResponse completionResponse = new OAuthCompletionResponse();

        ResponseEntity<OAuthCompletionResponse> responseEntity = mock(ResponseEntity.class);

        given(restTemplate.postForEntity(eq(completionUri), any(HttpEntity.class), eq(OAuthCompletionResponse.class)))
                .willReturn(responseEntity);
        given(responseEntity.getStatusCode())
                .willReturn(HttpStatus.OK);
        given(responseEntity.getBody())
                .willReturn(completionResponse);

        oAuthClient.authorise(authorisationCode);

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate)
            .postForEntity(eq(completionUri), httpEntityArgumentCaptor.capture(), eq(OAuthCompletionResponse.class));

        assertThat(httpEntityArgumentCaptor.getValue().getHeaders()).containsKey("Content-Type");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void authorisationCallPopulatesRequestParameters() throws StravaApiException {
        final String authorisationCode = "authCode";
        final OAuthCompletionResponse completionResponse = new OAuthCompletionResponse();

        ResponseEntity<OAuthCompletionResponse> responseEntity = mock(ResponseEntity.class);

        given(restTemplate.postForEntity(eq(completionUri), any(HttpEntity.class), eq(OAuthCompletionResponse.class)))
                .willReturn(responseEntity);
        given(responseEntity.getStatusCode())
                .willReturn(HttpStatus.OK);
        given(responseEntity.getBody())
                .willReturn(completionResponse);

        oAuthClient.authorise(authorisationCode);

        final ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        verify(restTemplate)
            .postForEntity(eq(completionUri), httpEntityArgumentCaptor.capture(), eq(OAuthCompletionResponse.class));

        Map<String,String> requestBodyMap = (Map<String, String>) httpEntityArgumentCaptor.getValue().getBody();
        assertThat(requestBodyMap).containsKeys("client_id", "client_secret", "code");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void authorisationCallReturnsHttpErrorCodeThrowsStravaApiException() throws StravaApiException {
        final String authorisationCode = "authCode";

        ResponseEntity<OAuthCompletionResponse> responseEntity = mock(ResponseEntity.class);

        given(restTemplate.postForEntity(eq(completionUri), any(HttpEntity.class), eq(OAuthCompletionResponse.class)))
            .willThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> oAuthClient.authorise(authorisationCode)).isInstanceOf(StravaApiException.class);

        verify(responseEntity, never()).getBody();
    }
}
