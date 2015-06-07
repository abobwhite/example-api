package com.daugherty.e2c.business.uploader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.ImageCreator;
import com.daugherty.e2c.business.MembershipRenewalTypeHelper;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductImage;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.ProductMetadata;
import com.daugherty.e2c.persistence.data.MembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductImageReadDao;
import com.daugherty.e2c.persistence.data.ProductImageWriteDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProductImageUploaderTest {
    private static final long SNAPSHOT_ID = 32L;

    private final Product product = new Product(1L, new ProductInformation(42L, "Name", "Description", "payPal", "100",
            "port", "10", "country", "10 days", "Model ZX 234", "specs"), new ProductMetadata(null, null, "metaTage",
            "keywords", false, true, false, new Date()), 22L, ApprovalStatus.DRAFT, 1, SNAPSHOT_ID);

    private final ProductImage existingProduct = new ProductImage(SNAPSHOT_ID, "productImageLink", false);
    private final ProductImage existingPrimaryProduct = new ProductImage(SNAPSHOT_ID, "productImageLink", true);

    MembershipLevel membershipLevel = new MembershipLevel(1L, 1, null, 6, 5, 2147483647, 600, true, 0, false, false,
            false, 1, false, false, false, false, false, false, false);
    Membership membership = new Membership(42L, membershipLevel);

    @Mock
    private DocumentDao documentDao;
    @Mock
    private ProductReadDao productReadDao;
    @Mock
    private ProductImageReadDao productImageReadDao;
    @Mock
    private ProductImageWriteDao productImageWriteDao;
    @Mock
    private MembershipReadDao membershipReadDao;
    @Mock
    private InputStream inputStream;
    @Mock
    private ImageCreator thumbnailImage;
    @Mock
    private MembershipRenewalTypeHelper membershipRenewalTypeHelper;

    @InjectMocks
    private final ProductImageUploader productImageUploader = new ProductImageUploader();

    @Captor
    private ArgumentCaptor<List<ProductImage>> productImageCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void uploadDocumentReturnsURLAndMarksAsPrimary() throws IOException {
        Long id = 1L;
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                new ArrayList<ProductImage>());
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        String uploadedDocumentKey = productImageUploader.uploadDocument(id, contentType, inputStream);

        verify(productImageWriteDao).updateImages(Matchers.anyLong(), productImageCaptor.capture());
        verifyZeroInteractions(membershipReadDao);
        ProductImage productImage = productImageCaptor.getValue().get(0);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertFalse(productImage.isPrimary());
        assertThat(productImage.getProductImageLink(), is(documentKey));
    }

    @Test
    public void uploadDocumentReturnsURLAndDoesNotMarkAsPrimary() throws IOException {
        Long id = 1L;
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                Lists.newArrayList(existingPrimaryProduct));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(membership);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        String uploadedDocumentKey = productImageUploader.uploadDocument(id, contentType, inputStream);

        verify(productImageWriteDao).updateImages(Matchers.anyLong(), productImageCaptor.capture());
        ProductImage productImage = productImageCaptor.getValue().get(0);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertFalse(productImage.isPrimary());
        assertThat(productImage.getProductImageLink(), is(documentKey));
    }

    @Test
    public void uploadDocumentReturnsURLWhenProvisionalMembershipAllowsMoreThanActiveMembershipMaxImages()
            throws IOException {
        Long id = 1L;
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        MembershipLevel provisionalLevel = new MembershipLevel(2L, 3, null, 6, 5, 2147483647, 600, true, 0, false,
                false, false, 2, false, false, false, true, false, false, false);
        Membership provisionalMembership = new Membership(42L, provisionalLevel);

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                Lists.newArrayList(existingPrimaryProduct, existingProduct));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(membership);
        when(membershipReadDao.loadProvisionalBySupplierId(42L)).thenReturn(provisionalMembership);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        String uploadedDocumentKey = productImageUploader.uploadDocument(id, contentType, inputStream);

        verify(productImageWriteDao).updateImages(Matchers.anyLong(), productImageCaptor.capture());
        ProductImage productImage = productImageCaptor.getValue().get(0);

        assertThat(uploadedDocumentKey, is(documentKey));
        assertFalse(productImage.isPrimary());
        assertThat(productImage.getProductImageLink(), is(documentKey));
    }

    @Test
    public void uploadDocumentReturnsValidationErrorWhenExceedsActiveMembershipMaxImages() throws IOException {
        expectedException.expect(ValidationException.class);

        Long id = 1L;
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                Lists.newArrayList(existingPrimaryProduct, existingProduct));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(membership);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        productImageUploader.uploadDocument(id, contentType, inputStream);

        verifyZeroInteractions(productImageWriteDao);
    }

    @Test
    public void uploadDocumentReturnsValidationErrorWhenExceedsActiveAndProvisionalMaxImages() throws IOException {
        expectedException.expect(ValidationException.class);

        Long id = 1L;
        String documentKey = DocumentType.PRODUCT_IMAGE.getType() + id;
        String contentType = "contentType";

        MembershipLevel provisionalLevel = new MembershipLevel(2L, 2, null, 6, 5, 2147483647, 600, true, 0, false,
                false, false, 1, false, false, false, true, false, false, false);
        Membership provisionalMembership = new Membership(42L, provisionalLevel);

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                Lists.newArrayList(existingPrimaryProduct, existingProduct));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(membership);
        when(membershipReadDao.loadProvisionalBySupplierId(42L)).thenReturn(provisionalMembership);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenReturn(documentKey);

        productImageUploader.uploadDocument(id, contentType, inputStream);

        verifyZeroInteractions(productImageWriteDao);
    }

    @Test
    public void uploadDocumentReturnsUploaderExceptionWhenImageReadExceptionIsThrown() throws IOException {
        expectedException.expect(UploaderException.class);

        Long id = 1L;
        String contentType = "contentType";

        MembershipLevel provisionalLevel = new MembershipLevel(2L, 2, null, 6, 5, 2147483647, 600, true, 0, false,
                false, false, 1, false, false, false, true, false, false, false);
        Membership provisionalMembership = new Membership(42L, provisionalLevel);

        when(inputStream.read(new byte[1024])).thenReturn(-1);
        when(productReadDao.loadLatest(id, Locale.ENGLISH)).thenReturn(product);
        when(productImageReadDao.loadBySnapshotIds(Lists.newArrayList(SNAPSHOT_ID))).thenReturn(
                Lists.newArrayList(existingPrimaryProduct));
        when(membershipReadDao.loadBySupplierId(42L)).thenReturn(membership);
        when(membershipReadDao.loadProvisionalBySupplierId(42L)).thenReturn(provisionalMembership);
        when(documentDao.create(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(InputStream.class)))
                .thenThrow(new RuntimeException());

        productImageUploader.uploadDocument(id, contentType, inputStream);

        verifyZeroInteractions(productImageWriteDao);
    }
}
