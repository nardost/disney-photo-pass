package edu.depaul.ntessema.disney.media.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;
    private String mimeType;
    private Binary image;
    private String md5Hash;
    private Date timestamp;

    /**
     * A static factory method that creates a Photo instance.
     * @param bytes the byte array of the image file.
     * @param mimeType the mime type of the file being uploaded.
     * @return a Photo object.
     */
    public static Photo create(final byte[] bytes, final String mimeType) {
        return  new Photo(
                Photo.getUUID(),
                mimeType,
                new Binary(BsonBinarySubType.BINARY, bytes),
                Photo.getMessageDigest(bytes),
                new Date()); // TODO: explore why Timestamp causes a runtime exception
    }

    /**
     * Utility method that generates a universally unique ID for photo objects.
     * @return a unique id.
     */
    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /*
     * Final members are not persisted. Use @Transient
     * to make an instance variable non-persistent.
     * https://docs.oracle.com/javaee/6/tutorial/doc/bnbqa.html
     */
    private static final String HASHING_ALGORITHM = "MD5";

    /**
     * Utility method that computes the md5 hash of image bytes.
     * @param bytes the array of bytes of the image
     * @return Base64 encoded md5 hash
     */
    private static String getMessageDigest(byte[] bytes) {
        try {
            final byte[] digest = MessageDigest.getInstance(HASHING_ALGORITHM).digest(bytes);
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ignored) {
            log.error("Unknown hashing algorithm: " + HASHING_ALGORITHM);
            return "";
        }
    }

}
