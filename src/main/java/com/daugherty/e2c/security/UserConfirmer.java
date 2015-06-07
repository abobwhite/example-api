package com.daugherty.e2c.security;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PendingUser;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.Validatable;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.BuyerReadDao;
import com.daugherty.e2c.persistence.data.BuyerWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserReadDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;
import com.daugherty.e2c.persistence.data.SupplierWriteDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.google.common.collect.Lists;

/**
 * Confirms a pending user.
 */
@Component
@Transactional
public class UserConfirmer {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private UserWriteDao userWriteDao;
    @Inject
    private PendingUserReadDao pendingUserReadDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private BuyerReadDao buyerReadDao;
    @Inject
    private BuyerWriteDao buyerWriteDao;
    @Inject
    private SupplierReadDao supplierReadDao;
    @Inject
    private SupplierWriteDao supplierWriteDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    private final PHPassHasher phpassHasher = new PHPassHasher();

    public void confirmUser(String confirmationToken, String username, String password, String role) {
        PendingUser pendingUser = retrievePendingUser(confirmationToken, username, password);
        Long partyId = createUserForParty(pendingUser, role);
        if (Role.BUYER.equals(role)) {
            transitionBuyerState(partyId);
        } else if (Role.SUPPLIER.equals(role)) {
            transitionSupplierState(partyId);
        }
    }

    private PendingUser retrievePendingUser(String confirmationToken, String username, String password) {

        ValidationError errors = new ValidationError();

        PendingUser pendingUser = pendingUserReadDao.loadByConfirmationToken(confirmationToken);
        if (!pendingUser.getUsername().equalsIgnoreCase(username)) {
            LOGGER.warn("Entered username '" + username + "' does not match username '" + pendingUser.getUsername()
                    + "' of PendingUser identified by confirmation token '" + confirmationToken + "'");
            errors.add("username", Validatable.PENDING_USER_USERNAME);
        }
        if (!phpassHasher.isMatch(password, pendingUser.getPassword())) {
            LOGGER.warn("Entered password does not match hashed password of PendingUser identified by confirmation token '"
                    + confirmationToken + "'");
            errors.add("password", Validatable.PENDING_USER_PASSWORD);
        }

        if (errors.hasErrors()) {
            ValidationException validationException = new ValidationException(errors);
            throw validationException;
        }

        return pendingUser;
    }

    private Long createUserForParty(PendingUser pendingUser, String role) {
        E2CUser confirmedUser = new E2CUser(pendingUser.getUsername(), pendingUser.getPassword(), true, 0,
                Lists.newArrayList(new SimpleGrantedAuthority(role)), new Party(pendingUser.getPartyId()), false);

        userWriteDao.insert(confirmedUser);

        pendingUserWriteDao.delete(pendingUser.getId());
        return pendingUser.getPartyId();
    }

    private void transitionBuyerState(Long id) {
        Buyer buyer = buyerReadDao.loadLatest(id);
        buyer.visit(changeDataVisitor);
        buyerWriteDao.recordEvent(buyer);
    }

    private void transitionSupplierState(Long id) {
        Supplier supplier = supplierReadDao.loadLatest(id, Locale.ENGLISH);
        supplier.visit(changeDataVisitor);
        supplierWriteDao.recordEvent(supplier);
    }

}
