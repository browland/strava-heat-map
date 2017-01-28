package net.benrowland.heatmap.client;

public class StravaApiException extends Exception {

    StravaApiException(String message) {
        super(message);
    }

    StravaApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
