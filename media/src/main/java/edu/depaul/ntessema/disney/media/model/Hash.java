package edu.depaul.ntessema.disney.media.model;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
public class Hash {

    private String id;
    private String hash;

    public Hash(Photo photo) {
        try {
            this.id = photo.getId();
            final byte[] hashedBytes = MessageDigest.getInstance("MD5").digest(photo.getImage().getData());
            this.hash = Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException ignored) {
        }
    }
}
