package com.daugherty.e2c.business.mutator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.domain.ProductInformation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.persistence.data.ProductMembershipReadDao;
import com.daugherty.e2c.persistence.data.ProductMembershipWriteDao;
import com.daugherty.e2c.persistence.data.SupplierApprovalWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class SupplierApprovalMutatorTest {
    @Mock
    private ProductMembershipReadDao productMembershipReadDao;
    @Mock
    private Accessor<Product> latestProductAccessor;
    @Mock
    private ProductMembershipWriteDao productMembershipWriteDao;
    @Mock
    private SupplierApprovalWriteDao supplierApprovalWriteDao;
    @Mock
    private Product product;
    @Mock
    private ProductInformation productInformation;
    @Mock
    private Supplier supplier;

    @InjectMocks
    private final SupplierApprovalMutator supplierApprovalMutator = new SupplierApprovalMutator();

    @Test
    public void createProfileApprovalUpdatesDatabaseAndSendsEmailIfSubmittingApprovalForFirstVersion() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 1, 242L, "fordp", new Date());

        when(supplierApprovalWriteDao.insert(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.create(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).insert(approval);
    }

    @Test
    public void createProfileApprovalUpdatesDatabaseOnlyIfSubmittingApprovalForNotTheFirstVersion() {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.PENDING_APPROVAL, 2, 242L, "fordp", new Date());

        when(supplierApprovalWriteDao.insert(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.create(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).insert(approval);
    }

    @Test
    public void createProductApprovalUpdatesDatabaseOnly() {
        SupplierApproval approval = new SupplierApproval(42L, "42", "Hitch-hikers Guide to the Galaxy", new Supplier(
                42L), SupplierApproval.PRODUCT_TYPE, ApprovalStatus.PENDING_APPROVAL, 2, 242L, "fordp", new Date());

        when(supplierApprovalWriteDao.insert(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.create(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).insert(approval);
    }

    @Test
    public void createProductApprovalUpdatesDatabaseOnlyAndInsertsInto() {
        SupplierApproval approval = new SupplierApproval(42L, "42", "Hitch-hikers Guide to the Galaxy", new Supplier(
                42L), SupplierApproval.PRODUCT_TYPE, ApprovalStatus.PENDING_APPROVAL, 2, 242L, "fordp", new Date());

        when(productMembershipReadDao.findMembershipByProductId(42L)).thenThrow(new EmptyResultDataAccessException(1));
        when(latestProductAccessor.load(42L, Locale.ENGLISH)).thenReturn(product);
        when(product.getInformation()).thenReturn(productInformation);
        when(productInformation.getSupplier()).thenReturn(supplier);
        when(supplier.getId()).thenReturn(100L);
        when(supplier.getMembershipId()).thenReturn(200L);

        when(supplierApprovalWriteDao.insert(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.create(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).insert(approval);
        verify(productMembershipWriteDao).create(42L, 200L);
    }

    @Test
    public void createCertificationApprovalUpdatesDatabaseOnly() {
        SupplierApproval approval = new SupplierApproval(42L, "42", "I am certifiable", new Supplier(42L),
                SupplierApproval.CERTIFICATION_TYPE, ApprovalStatus.PENDING_APPROVAL, 2, 242L, "fordp", new Date());

        when(supplierApprovalWriteDao.insert(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.create(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).insert(approval);
    }

    @Test
    public void updateProfileApprovalUpdatesDatabase() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "jKNz4P4q", "Megadodo Publications", new Supplier(42L),
                SupplierApproval.PROFILE_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 1, 242L, "fordp", new Date());

        when(supplierApprovalWriteDao.update(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).update(approval);
    }

    @Test
    public void updateProductApprovalUpdatesDatabase() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "42", "Hitch-hikers Guide to the Galaxy", new Supplier(
                42L), SupplierApproval.PRODUCT_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 1, 242L, "fordp",
                new Date());

        when(supplierApprovalWriteDao.update(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).update(approval);
    }

    @Test
    public void updateCertificationApprovalUpdatesDatabase() throws Exception {
        SupplierApproval approval = new SupplierApproval(42L, "42", "I am certifiable", new Supplier(42L),
                SupplierApproval.CERTIFICATION_TYPE, ApprovalStatus.WAITING_FOR_INFORMATION, 1, 242L, "fordp",
                new Date());

        when(supplierApprovalWriteDao.update(approval)).thenReturn(approval);

        SupplierApproval mutatedApproval = supplierApprovalMutator.update(approval);

        assertThat(mutatedApproval, is(notNullValue()));
        verify(supplierApprovalWriteDao).update(approval);
    }

}
