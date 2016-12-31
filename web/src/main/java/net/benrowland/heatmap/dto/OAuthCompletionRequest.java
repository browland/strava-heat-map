package net.benrowland.heatmap.dto;

public class OAuthCompletionRequest {
    private String clientId;
    private String clientSecret;
    private String authorisationCode;

    public OAuthCompletionRequest(String clientId, String clientSecret, String authorisationCode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorisationCode = authorisationCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorisationCode() {
        return authorisationCode;
    }

    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }
}
