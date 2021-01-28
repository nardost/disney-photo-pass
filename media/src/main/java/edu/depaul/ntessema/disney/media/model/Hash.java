package edu.depaul.ntessema.disney.media.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
@Slf4j
public class Hash {

    private final String imageId;
    private String md5Hash = "Hash value of image not computed.";

    public Hash(Photo photo) {
        final String HASHING_ALGORITHM = "MD5";
        this.imageId = photo.getId();
        try {
            final byte[] digest = MessageDigest.getInstance(HASHING_ALGORITHM).digest(photo.getImage().getData());
            this.md5Hash = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ignored) {
            log.error("Unknown hashing algorithm: " + HASHING_ALGORITHM);
        }
    }
}
