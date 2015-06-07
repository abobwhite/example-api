package com.daugherty.e2c.persistence.document.mongo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.daugherty.e2c.persistence.document.DocumentNotFoundException;
import com.daugherty.e2c.persistence.document.DocumentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/spring/documentDao-test-context.xml" })
public class MongoDocumentDaoTest {
    @Inject
    private ResourceLoader resourceLoader;
    @Inject
    private GridFsOperations operations;
    @Inject
    private MongoDocumentDao dao;

    @Test
    public void storeDocument() throws IOException {
        String key = dao.create(DocumentType.BUYER_LICENSE.toString(), "application/text",
                resourceLoader.getResource("classpath:supplier.txt").getInputStream());

        GridFsResource file = operations.getResource(key);

        assertThat(new BufferedReader(new InputStreamReader(file.getInputStream())).readLine(), is("Hi I'm a Supplier"));

        // This is a cleanup step
        operations.delete(Query.query(Criteria.where("filename").is(key)));
    }

    @Test(expected = DocumentNotFoundException.class)
    public void loadDocumentThrowDocumentNotFoundException() {
        dao.load("DocumentDoesNotexist");

        fail();
    }

    @Test
    public void loadDoument() throws IOException {
        operations.store(resourceLoader.getResource("classpath:supplier.txt").getInputStream(), "key");

        GridFsResource file = dao.load("key");

        assertDocument("key", file.getInputStream());
    }

    private void assertDocument(String key, InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            assertThat(bufferedReader.readLine(), is("Hi I'm a Supplier"));
        } finally {
            bufferedReader.close();
        }

        // This is a cleanup step
        operations.delete(Query.query(Criteria.where("filename").is(key)));
    }
}
