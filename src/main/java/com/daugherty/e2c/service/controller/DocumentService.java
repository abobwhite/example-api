package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentException;
import com.daugherty.e2c.security.Role;

/**
 * Rest Resource for Documents
 */
@Controller
@RequestMapping("/documents")
public class DocumentService {
    @Inject
    private DocumentDao documentDao;

    @RequestMapping(value = "/{documentKey}", method = RequestMethod.GET,
            headers = "Accept=image/jpeg, image/jpg, image/png, image/gif, application/pdf", produces = { "image/jpeg",
                    "image/jpg", "image/png", "image/gif", "application/pdf" })
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    public void loadDocument(@PathVariable String documentKey, HttpServletResponse response) {
        InputStream input = null;
        OutputStream output = null;

        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);

            long expires = cal.getTimeInMillis();

            GridFsResource file = documentDao.load(documentKey);

            response.addHeader("Content-type", file.getContentType());
            response.addHeader("Content-Length", String.valueOf(file.contentLength()));
            response.addHeader("Content-Disposition", "inline; filename=\"" + file.getFilename() + "\"");
            response.addDateHeader("Expires", expires);
            response.addDateHeader("Last-Modified", new Date().getTime());

            input = file.getInputStream();
            output = response.getOutputStream();
            int bytes;
            while ((bytes = input.read()) != -1) {
                output.write(bytes);
            }
        } catch (IOException e) {
            throw new DocumentException(e);
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                    throw new DocumentException(e);
                }
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    throw new DocumentException(e);
                }
        }

    }
}
