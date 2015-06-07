package com.daugherty.e2c.business;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.mail.sender.RegistrationApprovedMailSender;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Sends Emails to Supplier once Profile has been Published
 */
@Service
@Transactional
public class ProfilePublisher {

    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private Accessor<MembershipProductSummary> membershipProductSummaryAccessor;
    @Inject
    private RegistrationApprovedMailSender supplierTranslationPublishedMailSender;
    @Inject
    private Hashids hashids;

    public void publish(String publicSupplierId) {
        Long supplierId = hashids.decode(publicSupplierId)[0];
        Supplier supplier = supplierReadDao.loadLatest(supplierId, Locale.ENGLISH);

        if (isVersion1(supplier)) {
            if (hasProducts(supplier.getMembershipId())) {
                supplierTranslationPublishedMailSender.sendToSupplierWithProducts(supplier.getContact().getFirstName(),
                        supplier.getEmailWithPersonalName(), supplier.getContact().getLanguage());
            } else {
                supplierTranslationPublishedMailSender.sendToSupplier(supplier.getContact().getFirstName(),
                        supplier.getEmailWithPersonalName(), supplier.getContact().getLanguage());
            }
        }

    }

    private boolean isVersion1(Supplier supplier) {
        return Integer.valueOf(1).equals(supplier.getVersion());
    }

    private boolean hasProducts(Long id) {
        MembershipProductSummary supplierProductSummary = membershipProductSummaryAccessor.load(id, Locale.ENGLISH);
        return supplierProductSummary.getPendingApproval() + supplierProductSummary.getPendingTranslation()
                + supplierProductSummary.getWaitingForInformation() + supplierProductSummary.getApproved() > 0;
    }
}
