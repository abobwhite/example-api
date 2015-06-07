package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;

import javax.inject.Inject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.mapper.CertificationMapper;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonCertification;
import com.daugherty.e2c.service.json.JsonDocumentUrl;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CertificationService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Accessor<Certification> latestCertificationAccessor;
    @Inject
    private Accessor<Certification> approvedCertificationAccessor;
    @Inject
    private Mutator<Certification> supplierCertificationMutator;
    @Inject
    private Mutator<Certification> productCertificationMutator;
    @Inject
    private Mutator<Certification> generalCertificationMutator;
    @Inject
    private Uploader certificationDocumentUploader;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private CertificationMapper certificationMapper;

    @RequestMapping(value = "/suppliers/{id}/certifications", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public JsonCertification createSupplierCertification(@PathVariable String id,
            @RequestBody JsonCertification jsonCertification, Principal principal, Locale locale) {
        Certification createdCertification = supplierCertificationMutator.create(certificationMapper.toNewDomainObject(jsonCertification, id, locale));
        return new JsonCertification(createdCertification, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/products/{productId}/certifications", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public JsonCertification createProductCertification(@PathVariable Long productId,
            @RequestBody JsonCertification jsonCertification, Principal principal, Locale locale) {
        Certification createdCertification = productCertificationMutator.create(certificationMapper.toNewDomainObject(jsonCertification, productId, locale));
        return new JsonCertification(createdCertification, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/certifications/{certificationId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonCertification retrieveCertification(@PathVariable Long certificationId,
            @RequestParam(required = false) Boolean latest, Locale locale, Principal principal) {
        Accessor<Certification> accessor = Boolean.TRUE.equals(latest) ? latestCertificationAccessor
                : approvedCertificationAccessor;
        Certification certification = accessor.load(certificationId, locale);
        return new JsonCertification(certification, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/certifications/{certificationId}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public JsonCertification updateCertification(@PathVariable Long certificationId,
            @RequestBody JsonCertification jsonCertification, Principal principal, Locale locale) {
        Certification updatedCertification = generalCertificationMutator.update(certificationMapper.toExistingDomainObject(certificationId, jsonCertification, locale));
        return new JsonCertification(updatedCertification, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/certifications/{certificationId}", method = RequestMethod.DELETE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    public void deleteCertification(@PathVariable Long certificationId) {
        generalCertificationMutator.delete(certificationId);
    }

    @RequestMapping(value = "/certifications/{id}/documents", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void addCertificateImage(@PathVariable Long id, MultipartHttpServletRequest request,
            HttpServletResponse response, Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        out.write(addDocument(id, request, certificationDocumentUploader, locale));
        out.flush();
        out.close();
    }

    private String addDocument(Long id, MultipartHttpServletRequest request, Uploader uploader, Locale locale) {
        try {
            Iterator<String> itr = request.getFileNames();

            MultipartFile mpf = request.getFile(itr.next());
            LOG.debug(mpf.getOriginalFilename() + " uploaded for certification id " + id + " with content type "
                    + mpf.getContentType());

            ObjectMapper objectMapper = new RootWrappingObjectMapper();
            String uploadedDocumentUri = uploader.uploadDocument(id, mpf.getContentType(), mpf.getInputStream());
            return objectMapper.writeValueAsString(new JsonDocumentUrl(documentUrlFactory.createDocumentUrl(
                    uploadedDocumentUri, locale)));
        } catch (IOException e) {
            throw new UploaderException(e);
        }
    }

}
