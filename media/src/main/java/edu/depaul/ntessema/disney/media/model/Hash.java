package edu.depaul.ntessema.disney.media.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Slf4j
@AllArgsConstructor
@JsonPropertyOrder({
        "imageId",
        "timestamp",
        "md5Hash"
})
public class Hash {

    @JsonProperty("image_id") private final String imageId;
    @JsonProperty("md5_hash") private final String md5Hash;
    @JsonProperty("created_on") private final Date timestamp;

    public Hash(Photo photo) {
        this.imageId = photo.getId();
        this.md5Hash = photo.getMd5Hash();
        this.timestamp = photo.getTimestamp();
    }
}
