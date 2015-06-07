package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.domain.visitor.PartySwitchVisitor;
import com.daugherty.e2c.persistence.data.jdbc.JdbcBuyerDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcSupplierDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcUserDao;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserSwitcherTest {

    @Mock
    private JdbcUserDao jdbcUserDao;
    @Mock
    private JdbcBuyerDao jdbcBuyerDao;
    @Mock
    private JdbcSupplierDao jdbcSupplierDao;
    @Mock
    private PartySwitchVisitor partySwitchVisitor;
    @InjectMocks
    private UserSwitcher userSwitcher = new UserSwitcher();

    @Test
    public void switchBuyerToSupplier() {
        Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
        Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", contact, company, ApprovalStatus.APPROVED, PartyType.BUYER, 1, null);
        User user = new User(23L, "username", "password", Lists.newArrayList(Role.BUYER), 0, true, false, true, party,
                true, new Date());

        when(jdbcUserDao.find(23L)).thenReturn(user);
        when(partySwitchVisitor.getNewVersion(1, ApprovalStatus.APPROVED)).thenReturn(2);
        when(partySwitchVisitor.getNewApprovalStatus(ApprovalStatus.APPROVED)).thenReturn(ApprovalStatus.DRAFT);

        User supplier = userSwitcher.switchUser(23L);

        assertThat(supplier.getParty().getPartyType(), is(PartyType.SUPPLIER));
        assertThat(supplier.getParty().getVersion(), is(2));
        assertThat(supplier.getParty().getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(partySwitchVisitor).getNewApprovalStatus(ApprovalStatus.APPROVED);
        verify(jdbcSupplierDao).switchToSupplier(any(Supplier.class));
        verify(jdbcUserDao).deleteUserRole(23L, new SimpleGrantedAuthority(Role.BUYER));
        verify(jdbcUserDao).insertUserRole(23L, new SimpleGrantedAuthority(Role.SUPPLIER));
    }

    @Test
    public void switchSupplierToBuyer() {
        Contact contact = new Contact("first", "last", "country", null, "email@address.net", null, null, new Date());
        Company company = new Company("company name", null, null, null, null, null, null, null, null, null, null);
        Party party = new Party(586L, "jKNzKB04", contact, company, ApprovalStatus.APPROVED, PartyType.SUPPLIER, 1, null);
        User user = new User(23L, "username", "password", Lists.newArrayList(Role.SUPPLIER), 0, true, false, true,
                party, true, new Date());

        when(jdbcUserDao.find(23L)).thenReturn(user);
        when(partySwitchVisitor.getNewVersion(1, ApprovalStatus.APPROVED)).thenReturn(2);
        when(partySwitchVisitor.getNewApprovalStatus(ApprovalStatus.APPROVED)).thenReturn(ApprovalStatus.DRAFT);

        User buyer = userSwitcher.switchUser(23L);

        assertThat(buyer.getParty().getPartyType(), is(PartyType.BUYER));
        assertThat(buyer.getParty().getVersion(), is(2));
        assertThat(buyer.getParty().getApprovalStatus(), is(ApprovalStatus.DRAFT));
        verify(partySwitchVisitor).getNewApprovalStatus(ApprovalStatus.APPROVED);
        verify(jdbcBuyerDao).switchToBuyer(any(Buyer.class));
        verify(jdbcUserDao).deleteUserRole(23L, new SimpleGrantedAuthority(Role.SUPPLIER));
        verify(jdbcUserDao).insertUserRole(23L, new SimpleGrantedAuthority(Role.BUYER));
    }
}
