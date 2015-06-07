package com.daugherty.e2c.business;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines operations for Uploading Documents
 */
public interface Uploader {
    public String uploadDocument(Long id, String contentType, InputStream inputStream) throws IOException;

    public String uploadDocument(Long id, String documentName, String contentType, InputStream inputStream)
            throws IOException;

    public String uploadDocument(String publicId, String contentType, InputStream inputStream) throws IOException;

    public String uploadDocument(String publicId, String documentName, String contentType, InputStream inputStream)
            throws IOException;
}
