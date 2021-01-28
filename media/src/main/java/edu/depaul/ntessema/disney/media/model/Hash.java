package edu.depaul.ntessema.disney.media.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@AllArgsConstructor
public class Hash {

    private final String imageId;
    private final String md5Hash;

    public Hash(Photo photo) {
        this.imageId = photo.getId();
        this.md5Hash = photo.getMd5Hash();
    }
}
