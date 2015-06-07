package com.daugherty.e2c.persistence.document.mongo;

import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentNotFoundException;

@Repository
public class MongoDocumentDao implements DocumentDao {
    @Inject
    private GridFsOperations operations;

    @Override
    public String create(String documentType, String contentType, InputStream inputStream) {
        return operations.store(inputStream, documentType, contentType).getFilename();
    }

    @Override
    public GridFsResource load(String documentKey) {
        try {
            return operations.getResource(documentKey);
        } catch (NullPointerException e) {
            throw new DocumentNotFoundException();
        }
    }

    @Override
    public void delete(String documentKey) {
        try {
            operations.delete(Query.query(Criteria.where("filename").is(documentKey)));
        } catch (NullPointerException e) {
            throw new DocumentNotFoundException();
        }
    }
}
