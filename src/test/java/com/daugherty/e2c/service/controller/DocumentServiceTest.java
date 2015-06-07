package com.daugherty.e2c.service.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.daugherty.e2c.persistence.document.DocumentDao;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {
    @Mock
    private DocumentDao documentDao;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private GridFsResource file;
    @Mock
    private InputStream inputStream;
    @Mock
    private ServletOutputStream outputStream;

    @InjectMocks
    private DocumentService documentService = new DocumentService();

    @Test
    public void loadDocumentWritesToOutputStream() throws IOException {
        String documentKey = "documentKey";
        when(documentDao.load(documentKey)).thenReturn(file);
        when(file.getContentType()).thenReturn("contentType");
        when(file.contentLength()).thenReturn(100L);
        when(file.getFilename()).thenReturn("fileName");
        when(file.getInputStream()).thenReturn(inputStream);
        when(inputStream.read()).thenReturn(-1);
        when(httpServletResponse.getOutputStream()).thenReturn(outputStream);

        documentService.loadDocument(documentKey, httpServletResponse);

        verify(httpServletResponse).addHeader("Content-type", "contentType");
        verify(httpServletResponse).addHeader("Content-Length", "100");
        verify(httpServletResponse).addHeader("Content-Disposition", "inline; filename=\"" + "fileName" + "\"");
    }
}
