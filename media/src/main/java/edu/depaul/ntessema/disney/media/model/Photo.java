package edu.depaul.ntessema.disney.media.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;
    private String mimeType;
    private Binary image;
    private String md5Hash;
    private Date timestamp;
}
