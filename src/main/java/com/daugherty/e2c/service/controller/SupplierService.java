package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.mapper.SupplierMapper;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.document.DocumentException;
import com.daugherty.e2c.persistence.document.TutorialDao;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonSupplier;

/**
 * REST resource for Suppliers.
 */
@Controller
@RequestMapping("/suppliers")
public class SupplierService {
    @Inject
    private Accessor<Supplier> approvedSupplierAccessor;
    @Inject
    private TutorialDao tutorialDao;
    @Inject
    private SupplierMapper supplierMapper;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonSupplier retrieveSupplier(@PathVariable String id, Locale locale, Principal principal) {
        Supplier supplier = approvedSupplierAccessor.load(id, locale);
        return supplierMapper.fromDomainObject(supplier, locale, principal);
    }

    @RequestMapping(value = "/{id}/tutorial", method = RequestMethod.GET, headers = "Accept=application/pdf",
            produces = { "application/pdf" })
    @Secured({ Role.SUPPLIER, Role.ADMIN, Role.SUPPLIER_MODERATOR, Role.TRANSLATOR })
    public void retrieveSupplierTutorial(@PathVariable String id, HttpServletResponse response, @RequestParam(
            required = false, defaultValue = "en") String tutorialLanguage) {

        Locale locale = new Locale(tutorialLanguage);

        Supplier supplier = approvedSupplierAccessor.load(id, locale);

        if (supplier.getMembership().getLevel().isExportTutorialAccessible()) {
            Resource resource = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 1);

                long expires = cal.getTimeInMillis();

                resource = tutorialDao.getSupplierTutorial(locale);

                response.addHeader("Content-type", "application/pdf");
                response.addHeader("Content-Length", String.valueOf(resource.contentLength()));
                response.addHeader("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"");
                response.addDateHeader("Expires", expires);
                response.addDateHeader("Last-Modified", new Date().getTime());

                input = resource.getInputStream();
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
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
