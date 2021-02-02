package edu.depaul.ntessema.disney.media.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpError {
    private final String error;
    private final int status;
    private final String message;
    private final String path;
    private final String requestId;
    private final String timestamp;
}
