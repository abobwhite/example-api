package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.mapper.LatestSupplierMapper;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonDocumentUrl;
import com.daugherty.e2c.service.json.JsonLatestSupplier;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/latestSuppliers")
public class LatestSupplierService {
    
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    @Inject
    private Accessor<Supplier> latestSupplierAccessor;
    @Inject
    private Mutator<Supplier> supplierMutator;
    @Inject
    private LatestSupplierMapper latestSupplierMapper;
    @Inject
    private Uploader supplierLicenseUploader;
    @Inject
    private Uploader supplierLogoUploader;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonLatestSupplier retrieveSupplier(@PathVariable String id, Locale locale, Principal principal) {
        Supplier supplier = latestSupplierAccessor.load(id, locale);
        return latestSupplierMapper.fromDomainObject(supplier, locale, principal);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonLatestSupplier createSupplier(@RequestBody JsonLatestSupplier jsonLatestSupplier, HttpServletRequest request, Locale locale) {
        jsonLatestSupplier.setIpAddress(RequestUtils.remoteIp(request));
        Supplier supplier = latestSupplierMapper.toNewDomainObject(jsonLatestSupplier, locale);
        return latestSupplierMapper.fromDomainObject(supplierMutator.create(supplier), locale);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public JsonLatestSupplier updateSupplier(@PathVariable String id, @RequestBody JsonLatestSupplier jsonLatestSupplier, Locale locale) {
        Supplier supplier = latestSupplierMapper.toExistingDomainObject(id, jsonLatestSupplier, locale);
        return latestSupplierMapper.fromDomainObject(supplierMutator.update(supplier), locale);
    }

    @RequestMapping(value = "/{id}/licenses", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void addLicense(@PathVariable String id, MultipartHttpServletRequest request, HttpServletResponse response,
            Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, request, supplierLicenseUploader, locale));
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/{id}/logos", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public void addLogo(@PathVariable String id, MultipartHttpServletRequest request, HttpServletResponse response,
            Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, request, supplierLogoUploader, locale));
        out.flush();
        out.close();
    }
    
 // TODO: Re-factor this out at some point and remove documentUrlFactory dependency from service
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
