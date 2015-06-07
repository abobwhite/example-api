package com.daugherty.e2c.business;

import javax.inject.Inject;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.domain.visitor.PartySwitchVisitor;
import com.daugherty.e2c.persistence.data.jdbc.JdbcBuyerDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcSupplierDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcUserDao;
import com.daugherty.e2c.security.Role;

/**
 * Changes a Party from a Buyer to a Supplier and Vice Versa
 */
@Service
@Transactional
public class UserSwitcher {

    @Inject
    private JdbcUserDao jdbcUserDao;
    @Inject
    private JdbcBuyerDao buyerDao;
    @Inject
    private JdbcSupplierDao supplierDao;
    @Inject
    private PartySwitchVisitor partySwitchVisitor;

    public User switchUser(Long userId) {
        User user = jdbcUserDao.find(userId);

        if (PartyType.BUYER.equals(user.getParty().getPartyType())) {
            switchToSupplier(user);
            switchUserRoles(user, Role.BUYER, Role.SUPPLIER);
        } else if (PartyType.SUPPLIER.equals(user.getParty().getPartyType())) {
            switchToBuyer(user);
            switchUserRoles(user, Role.SUPPLIER, Role.BUYER);
        }

        return user;
    }

    private void switchToBuyer(User user) {
        Party party = user.getParty();
        Buyer buyer = new Buyer(party.getId(), party.getPublicId(), party.getContact(), party.getCompany(), party.getApprovalStatus(),
                party.getVersion(), party.getSnapshotId(), null);

        buyer.visit(partySwitchVisitor);

        buyerDao.switchToBuyer(buyer);

        user.setParty(buyer);
    }

    private void switchToSupplier(User user) {
        Party party = user.getParty();
        Supplier supplier = new Supplier(party.getId(), party.getPublicId(), party.getContact(), party.getCompany(),
                party.getApprovalStatus(), party.getVersion(), party.getSnapshotId(), null, null, null);

        supplier.visit(partySwitchVisitor);

        supplierDao.switchToSupplier(supplier);

        user.setParty(supplier);
    }

    private void switchUserRoles(User user, String fromRole, String toRole) {
        jdbcUserDao.deleteUserRole(user.getId(), new SimpleGrantedAuthority(fromRole));
        jdbcUserDao.insertUserRole(user.getId(), new SimpleGrantedAuthority(toRole));

        user.getRoles().remove(fromRole);
        user.getRoles().add(toRole);
    }
}
