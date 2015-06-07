package com.daugherty.e2c.service.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.daugherty.e2c.business.ProductPublisher;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.accessor.MembershipProductSummaryAccessor;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.ProductFilter;
import com.daugherty.e2c.business.mapper.ProductMapper;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonDocumentUrl;
import com.daugherty.e2c.service.json.JsonLatestProduct;
import com.daugherty.e2c.service.json.JsonMembershipProductSummary;
import com.daugherty.e2c.service.json.JsonSupplierLatestProducts;
import com.daugherty.e2c.service.json.RootWrappingObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST resource for Products.
 */
@Controller
public class LatestProductService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Accessor<Product> latestProductAccessor;
    @Inject
    private MembershipProductSummaryAccessor membershipProductSummaryAccessor;
    @Inject
    private Uploader productImageUploader;
    @Inject
    private Uploader primaryProductImageUploader;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private Mutator<Product> productMutator;
    @Inject
    private ProductPublisher publisher;
    @Inject
    private ProductMapper productMapper;

    @RequestMapping(value = "/suppliers/{id}/latestProducts", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonSupplierLatestProducts retrieveSupplierProducts(@PathVariable String id, @RequestParam(
            value = ProductFilter.PRODUCT_NAME, required = false) String productName, @RequestParam(
            value = ProductFilter.STATUS, required = false) String approvalStatus, @RequestParam(
            value = ProductFilter.HOT, required = false) Boolean hotProdcuts, @RequestParam(
            value = ProductFilter.PUBLISHED, required = false) Boolean published, @RequestParam(
            value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(value = BaseFilter.SORT_DESC,
            defaultValue = "false") Boolean sortDesc,
            @RequestParam(value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale, Principal principal) {

        ProductFilter filter = new ProductFilter(null, null, hotProdcuts, null, null, null, id, productName,
                verifyApprovalStatusParameter(approvalStatus), published, sortBy, sortDesc, startItem, count, locale);

        List<Product> products = latestProductAccessor.find(filter);

        JsonSupplierLatestProducts jsonSupplierProducts = new JsonSupplierLatestProducts();
        for (Product product : products) {
            jsonSupplierProducts.add(new JsonLatestProduct(product, documentUrlFactory, locale, principal));
        }
        return jsonSupplierProducts;
    }

    @RequestMapping(value = "/memberships/{id}/productSummary", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonMembershipProductSummary retrieveMembershipProductSummary(@PathVariable Long id) {
        return new JsonMembershipProductSummary(membershipProductSummaryAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(value = "/latestProducts/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonLatestProduct retrieveProduct(@PathVariable Long id, Locale locale, Principal principal) {

        Product product = latestProductAccessor.load(id, locale);
        return new JsonLatestProduct(product, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/latestProducts", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public JsonLatestProduct createProduct(@RequestBody JsonLatestProduct jsonProduct, Principal principal,
            Locale locale) {
        Product product = productMutator.create(productMapper.toNewDomainObject(jsonProduct));
        return new JsonLatestProduct(product, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/latestProducts/{id}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR, Role.ADMIN })
    @ResponseBody
    public JsonLatestProduct updateProduct(@PathVariable Long id, @RequestBody JsonLatestProduct jsonProduct,
            Principal principal, Locale locale) {
        Product product = productMutator.update(productMapper.toExistingDomainObject(id, jsonProduct, locale));
        return new JsonLatestProduct(product, documentUrlFactory, locale, principal);
    }

    @RequestMapping(value = "/latestProducts/{id}/images", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER, Role.SUPPLIER_MODERATOR })
    @ResponseBody
    public void addImage(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") Boolean primary,
            MultipartHttpServletRequest request, HttpServletResponse response, Locale locale) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpStatus.OK.value());

        PrintWriter out = response.getWriter();
        Uploader uploader = primary ? primaryProductImageUploader : productImageUploader;
        out.write(addDocument(id, request, uploader, locale));
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/latestProducts/{id}/published", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void publishProduct(@PathVariable Long id) {
        publisher.publish(id);
    }

    @RequestMapping(value = "/latestProducts/{id}/unpublished", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void unpublishProduct(@PathVariable Long id) {
        publisher.unpublish(id);
    }

    @RequestMapping(value = "/latestProducts/{id}/hot", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void heatProduct(@PathVariable Long id) {
        publisher.heat(id);
    }

    @RequestMapping(value = "/latestProducts/{id}/cold", method = RequestMethod.POST)
    @Secured({ Role.SUPPLIER })
    @ResponseBody
    public void coolProduct(@PathVariable Long id) {
        publisher.cool(id);
    }

    @RequestMapping(value = "/latestProducts/{id}/addHotProductOverride", method = RequestMethod.POST)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public void addHotProductOverride(@PathVariable Long id) {
        publisher.addHotProductOverride(id);
    }

    @RequestMapping(value = "/latestProducts/{id}/removeHotProductOverride", method = RequestMethod.POST)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public void removeHotProductOverride(@PathVariable Long id) {
        publisher.removeHotProductOverride(id);
    }

    private ApprovalStatus verifyApprovalStatusParameter(String approvalStatus) {
        return StringUtils.isBlank(approvalStatus) ? null : ApprovalStatus.findByName(approvalStatus);
    }

    private String addDocument(Long id, MultipartHttpServletRequest request, Uploader uploader, Locale locale) {
        try {
            Iterator<String> itr = request.getFileNames();

            MultipartFile mpf = request.getFile(itr.next());
            LOG.debug(mpf.getOriginalFilename() + " uploaded for party id " + id + " with content type "
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
