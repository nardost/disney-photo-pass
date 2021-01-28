# Media Service (Disney Photo-Pass System)

### Endpoints

The endpoints are built on the reactive web stack.

METHOD | ENDPOINT | DESCRIPTION
------ | -------- | -----------
```GET``` | ```/v1/photo/list``` | streams MD5 hashes of all photos
```GET``` | ```/v1/photo/{id}``` | retrieves a single photo
```POST``` | ```/v1/photo/save``` | saves a picture file.


### Environment Variables

VARIABLE | PURPOSE
-------- | -------
```MONGODB_AUTHENTICATION_DB``` | MongoDB authentication database
```MONGODB_LOCAL_HOST``` | Development MongoDB URL
```MONGODB_LOCAL_PORT``` | Development MongoDB port
```MONGODB_DISNEY_MEDIA_DB``` | Development MongoDB database
```MONGODB_DISNEY_USERNAME``` | Development MongoDB user name
```MONGODB_DISNEY_PASSWORD``` | Development MongoDB password
