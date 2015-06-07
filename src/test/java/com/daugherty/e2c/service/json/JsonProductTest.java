package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class JsonProductTest {

    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void constructingWithNewDomainObjectPopulatesSubsetOfFields() throws Exception {
        List<ProductCategory> categories = Lists.newArrayList(new ProductCategory(1L));
        Product entityProduct = new Product(new ProductInformation(586L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications"),
                categories);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonProduct.getSupplier().getId(), is(entityProduct.getInformation().getSupplier().getPublicId()));
        assertThat(jsonProduct.getName(), is(entityProduct.getInformation().getName()));
        assertThat(jsonProduct.getDescription(), is(entityProduct.getInformation().getDescription()));
        assertThat(jsonProduct.getPaymentTerms(), is(entityProduct.getInformation().getPaymentTerms()));
        assertThat(jsonProduct.getMinimumOrder(), is(entityProduct.getInformation().getMinimumOrder()));
        assertThat(jsonProduct.getFreightOnBoardPort(), is(entityProduct.getInformation().getFreightOnBoardPort()));
        assertThat(jsonProduct.getFreightOnBoardPrice(), is(entityProduct.getInformation().getFreightOnBoardPrice()));
        assertThat(jsonProduct.getCountry(), is(entityProduct.getInformation().getCountry()));
        assertThat(jsonProduct.getLeadTime(), is(entityProduct.getInformation().getLeadTime()));
        assertThat(jsonProduct.getModel(), is(entityProduct.getInformation().getModelNumber()));
        assertThat(jsonProduct.getSpecifications(), is(entityProduct.getInformation().getSpecifications()));
        assertThat(jsonProduct.getMetaTags(), is(entityProduct.getMetadata().getMetaTags()));
        assertThat(jsonProduct.getKeywords(), is(entityProduct.getMetadata().getKeywords()));

        assertThat(jsonProduct.getCategories().size(), is(1));
        assertThat(jsonProduct.getCategories().get(0), is(entityProduct.getMetadata().getCategories().get(0).getId()));
    }

    @Test
    public void constructingWithFullyPopulatedDomainObjectPopulatesSubsetOfFields() throws Exception {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);
        entityProduct.getMetadata().setCategories(Lists.newArrayList(new ProductCategory(1L)));
        ProductImage entityImage = new ProductImage(421L, "productImageLink", true);
        entityProduct.setImages(Lists.newArrayList(entityImage));

        when(documentUrlFactory.createDocumentUrl(entityImage.getProductImageLink(), Locale.ENGLISH)).thenReturn(
                "productImageUrl");

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonProduct.getId(), is(entityProduct.getId()));
        assertThat(jsonProduct.getSupplier().getId(), is(entityProduct.getInformation().getSupplier().getPublicId()));
        assertThat(jsonProduct.getName(), is(entityProduct.getInformation().getName()));
        assertThat(jsonProduct.getDescription(), is(entityProduct.getInformation().getDescription()));
        assertThat(jsonProduct.getPaymentTerms(), is(entityProduct.getInformation().getPaymentTerms()));
        assertThat(jsonProduct.getMinimumOrder(), is(entityProduct.getInformation().getMinimumOrder()));
        assertThat(jsonProduct.getFreightOnBoardPort(), is(entityProduct.getInformation().getFreightOnBoardPort()));
        assertThat(jsonProduct.getFreightOnBoardPrice(), is(entityProduct.getInformation().getFreightOnBoardPrice()));
        assertThat(jsonProduct.getCountry(), is(entityProduct.getInformation().getCountry()));
        assertThat(jsonProduct.getLeadTime(), is(entityProduct.getInformation().getLeadTime()));
        assertThat(jsonProduct.getModel(), is(entityProduct.getInformation().getModelNumber()));
        assertThat(jsonProduct.getSpecifications(), is(entityProduct.getInformation().getSpecifications()));
        assertThat(jsonProduct.getChineseName(), is(entityProduct.getMetadata().getChineseName()));
        assertThat(jsonProduct.getEnglishName(), is(entityProduct.getMetadata().getEnglishName()));
        assertThat(jsonProduct.getMetaTags(), is(entityProduct.getMetadata().getMetaTags()));
        assertThat(jsonProduct.getKeywords(), is(entityProduct.getMetadata().getKeywords()));
        assertThat(jsonProduct.isPublished(), is(entityProduct.getMetadata().isPublished()));
        assertThat(jsonProduct.isHotProduct(), is(entityProduct.getMetadata().isHot()));
        assertThat(jsonProduct.getApprovalStatus(), is(entityProduct.getApprovalStatus().getName()));
        assertThat(jsonProduct.getVersion(), is(entityProduct.getVersion()));

        assertThat(jsonProduct.getCategories().size(), is(1));
        assertThat(jsonProduct.getCategories().get(0), is(entityProduct.getMetadata().getCategories().get(0).getId()));

        assertThat(jsonProduct.getImages().size(), is(1));
        assertThat(jsonProduct.getImages().get(0).getImageUrl(), is("productImageUrl"));
        assertThat(jsonProduct.getImages().get(0).isPrimary(), is(entityImage.isPrimary()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAnonymousUsers() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonProduct.getSupplier().getEmail(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAuthenticatedAdmin() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonProduct.getSupplier().getEmail(), is(entityProduct.getInformation().getSupplier().getContact()
                .getEmailAddress()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAuthenticatedBuyer() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonProduct.getSupplier().getEmail(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAuthenticatedBuyerModerator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonProduct.getSupplier().getEmail(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreEmailAddressForAuthenticatedSupplierMatchingThisOne() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 42L, "jKNz4P4q"));

        assertThat(jsonProduct.getSupplier().getEmail(), is(entityProduct.getInformation().getSupplier().getContact()
                .getEmailAddress()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAuthenticatedSupplierNotMatchingThisOne() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 586L, "jKNzKB04"));

        assertThat(jsonProduct.getSupplier().getEmail(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreEmailAddressForAuthenticatedSupplierModerator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER_MODERATOR, null, null));

        assertThat(jsonProduct.getSupplier().getEmail(), is(entityProduct.getInformation().getSupplier().getContact()
                .getEmailAddress()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreEmailAddressForAuthenticatedTranslator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonProduct.getSupplier().getEmail(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAnonymousUsers() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonProduct.getSupplier().getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAnonymousUsers() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH, null);

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedAdmin() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedAdmin() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.ADMIN, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedBuyer() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedBuyer() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER, 586L, "jKNzKB04"));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedBuyerModerator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedBuyerModerator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.BUYER_MODERATOR, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreNonPublicWebsiteForAuthenticatedSupplierMatchingThisOne() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 42L, "jKNz4P4q"));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedSupplierNotMatchingThisOne() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 666L, "pBVQwo0b"));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedSupplierNotMatchingThisOne() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER, 666L, "pBVQwo0b"));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnoreNonPublicWebsiteForAuthenticatedSupplierModerator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.SUPPLIER_MODERATOR, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    @Test
    public void existingSupplierConstructionShouldIgnoreNonPublicWebsiteForAuthenticatedTranslator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(false);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(nullValue()));
    }

    @Test
    public void existingSupplierConstructionShouldNotIgnorePublicWebsiteForAuthenticatedTranslator() {
        Product entityProduct = buildDomainObjectWithoutCategoriesAndImages(true);

        JsonProduct jsonProduct = new JsonProduct(entityProduct, documentUrlFactory, Locale.ENGLISH,
                buildAuthentication(Role.TRANSLATOR, null, null));

        assertThat(jsonProduct.getSupplier().getWebsite(), is(entityProduct.getInformation().getSupplier().getCompany()
                .getWebsite()));
    }

    private Product buildDomainObjectWithoutCategoriesAndImages(boolean advancedWebAndMailCapabilityEnabled) {
        ProductInformation information = new ProductInformation(42L, "name", "description", "paymentTerms",
                "minimumOrder", "freightOnBoardPort", "1.23", "country", "leadTime", "model", "specifications");
        information.setSupplier(buildSupplier(advancedWebAndMailCapabilityEnabled));
        return new Product(242L, information, new ProductMetadata("chineseName", "englishName", "metaTags", "keywords",
                true, true, false, new Date()), 666L, ApprovalStatus.APPROVED, 13, 421L);
    }

    private Supplier buildSupplier(boolean advancedWebAndMailCapabilityEnabled) {
        Membership membership = new Membership(1L, new MembershipLevel(1L, 1, BigDecimal.valueOf(10), 6, 10, 20, 20,
                false, 10, false, false, false, 10, false, advancedWebAndMailCapabilityEnabled, false, true, false,
                false, false));

        Contact contact = new Contact(null, Gender.MALE, "first", "last", "country", null, "email", "skype", "msn",
                "icq", null, "123.123.123.123", Language.ENGLISH, new Date());
        List<BusinessType> businessTypes = Lists.newArrayList(BusinessType.AGENT, BusinessType.GOVERNMENT);
        Company company = new Company("companyName", null, "description", businessTypes, "1-10", "website", 2013,
                "200-300", null, "logoRefId", "videoRefId");

        Supplier supplier = new Supplier(42L, "jKNz4P4q", contact, company, ApprovalStatus.APPROVED, 13, 586L,
                "exportLicenseRefId", null, 1234L);
        supplier.getMemberships().add(membership);

        return supplier;
    }

    private Authentication buildAuthentication(String role, Long partyId, String publicPartyId) {
        E2CUser user = new E2CUser("username", "password", true, 0, AuthorityUtils.createAuthorityList(role),
                new Party(partyId, publicPartyId), true);

        return new UsernamePasswordAuthenticationToken(user, null);
    }

}
