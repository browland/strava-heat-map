package net.benrowland.heatmap.dto;

public class OAuthCallbackRequest {
    private String state;
    private String code;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
