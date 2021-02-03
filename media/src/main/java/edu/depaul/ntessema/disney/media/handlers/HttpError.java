package edu.depaul.ntessema.disney.media.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpError {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String requestId;
    private final String timestamp;
}
