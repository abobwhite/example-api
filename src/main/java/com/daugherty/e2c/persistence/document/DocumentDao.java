package com.daugherty.e2c.persistence.document;

import java.io.InputStream;

import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface DocumentDao {
    public String create(String documentKey, String contentType, InputStream inputStream);

    public GridFsResource load(String documentKey);

    public void delete(String documentKey);
}
