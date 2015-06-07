package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.mapper.BuyerMapper;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.persistence.document.DocumentException;
import com.daugherty.e2c.persistence.document.TutorialDao;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyer;
import com.daugherty.e2c.service.json.JsonDocumentUrl;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST resource for Buyers.
 */
@Controller
@RequestMapping("/buyers")
public class BuyerService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Accessor<Buyer> approvedBuyerAccessor;
    @Inject
    private Accessor<Buyer> latestBuyerAccessor;
    @Inject
    private Mutator<Buyer> buyerMutator;
    @Inject
    private Uploader buyerLicenseUploader;
    @Inject
    private Uploader buyerLogoUploader;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private TutorialDao tutorialDao;
    @Inject
    private BuyerMapper buyerMapper;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.BUYER, Role.BUYER_MODERATOR })
    @ResponseBody
    public JsonBuyer retrieveBuyer(@PathVariable String id,
            @RequestParam(required = false, defaultValue = "false") Boolean latest, Principal principal, Locale locale) {
        Accessor<Buyer> accessor = Boolean.TRUE.equals(latest) ? latestBuyerAccessor : approvedBuyerAccessor;
        Buyer buyer = accessor.load(id, Locale.CHINESE);
        return new JsonBuyer(buyer, documentUrlFactory, locale, principal);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonBuyer createBuyer(@RequestBody JsonBuyer jsonBuyer, HttpServletRequest request, Locale locale) {
        jsonBuyer.setIpAddress(RequestUtils.remoteIp(request));
        Buyer buyer = buyerMutator.create(buyerMapper.toNewDomainObject(jsonBuyer, locale));
        return new JsonBuyer(buyer, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.BUYER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public JsonBuyer updateBuyer(@PathVariable String id, @RequestBody JsonBuyer jsonBuyer, Locale locale) {
        Buyer buyer = buyerMapper.toExistingDomainObject(id, jsonBuyer, locale);
        return new JsonBuyer(buyerMutator.update(buyer), documentUrlFactory, locale);
    }

    @RequestMapping(value = "/{id}/licenses", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @Secured({ Role.BUYER, Role.BUYER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void addLicense(@PathVariable String id, MultipartHttpServletRequest request, HttpServletResponse response,
            Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, request, buyerLicenseUploader, locale));
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/{id}/logos", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @Secured({ Role.BUYER, Role.BUYER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void addLogo(@PathVariable String id, MultipartHttpServletRequest request, HttpServletResponse response,
            Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, request, buyerLogoUploader, locale));
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/{id}/tutorial", method = RequestMethod.GET, headers = "Accept=application/pdf",
            produces = { "application/pdf" })
    @Secured({ Role.BUYER, Role.ADMIN, Role.SUPPLIER_MODERATOR, Role.TRANSLATOR })
    public void retrieveBuyerTutorial(@PathVariable String id, HttpServletResponse response, Locale locale) {
        Buyer buyer = latestBuyerAccessor.load(id, locale);

        if (buyer != null) {
            Resource resource = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 1);

                long expires = cal.getTimeInMillis();

                resource = tutorialDao.getBuyerTutorial(locale);

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

    private String addDocument(String publicId, MultipartHttpServletRequest request, Uploader uploader, Locale locale) {
        try {
            Iterator<String> itr = request.getFileNames();

            MultipartFile mpf = request.getFile(itr.next());
            LOG.debug(mpf.getOriginalFilename() + " uploaded for party id " + publicId + " with content type "
                    + mpf.getContentType());

            ObjectMapper objectMapper = new RootWrappingObjectMapper();
            String uploadedDocumentKey = uploader.uploadDocument(publicId, mpf.getContentType(), mpf.getInputStream());
            return objectMapper.writeValueAsString(new JsonDocumentUrl(documentUrlFactory.createDocumentUrl(
                    uploadedDocumentKey, locale)));

        } catch (IOException e) {
            throw new UploaderException(e);
        }
    }
}
