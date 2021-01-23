package edu.depaul.ntessema.disney.media.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;
    private Binary image;
    public static Photo INVALID = new Photo("1", new Binary("photo".getBytes()));
}
