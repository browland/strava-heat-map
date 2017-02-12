package net.benrowland.heatmap.client;

public class StravaApiException extends Exception {

    public StravaApiException(String message) {
        super(message);
    }

    public StravaApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
