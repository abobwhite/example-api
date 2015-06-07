package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcPartyDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcPartyDao partyDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyer.sql");
    }

    @Test
    public void loadByEmailAddress() {
        Party party = partyDao.loadByEmailAddress("Email 112");

        assertParty99911(party);
    }

    @Test
    public void loadById() {
        Party party = partyDao.loadById(99911L);

        assertParty99911(party);
    }

    @Test
    public void hasBeenApproved() {
        assertTrue(partyDao.hasBeenApprovedOnce(99911L));
    }

    @Test
    public void getAllByUsername() {
        QueryCriteria queryCriteria = partyDao.createQueryCriteria("buyer99", PartyType.BUYER.toString(), null, false,
                1, 25, Locale.ENGLISH);
        List<Party> parties = partyDao.getAll(queryCriteria);

        assertThat(parties.size(), is(1));
        assertParty99911(parties.get(0));
    }

    @Test
    public void deletePartyByPartyId() {
        assertThat(1, is(partyDao.deletePartyByPartyId(99911L)));
    }

    private void assertParty99911(Party party) {
        assertThat(party.getId(), is(99911L));
        assertThat(party.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(party.getVersion(), is(1));
        assertThat(party.getSnapshotId(), is(999112L));

        Contact contact = party.getContact();
        assertThat(contact.getFirstName(), is("First 112"));
        assertThat(contact.getLastName(), is("Last 112"));
        assertThat(contact.getCountry(), is("Country 112"));
        assertThat(contact.getEmailAddress(), is("Email 112"));
        assertThat(contact.getSkypeRefId(), is("Skype 112"));
        assertThat(contact.getMsnRefId(), is("MSN 112"));
        assertThat(contact.getIcqRefId(), is("ICQ 112"));
        assertThat(contact.getBusinessTelephoneNumber(), is("Business 112"));
        assertThat(contact.getIpAddress(), is("127.0.0.1"));
        assertThat(contact.getLanguage(), is(Language.ENGLISH));

        Company company = party.getCompany();
        assertThat(company.getEnglishName(), is("English Name 112"));
        assertThat(company.getDescription(), is("Description 112"));
        assertThat(company.getNumberOfEmployees(), is("11-20"));
        assertThat(company.getWebsite(), is("http://Website 112"));
        assertThat(company.getYearEstablished(), is(2012));
        assertThat(company.getTotalAnnualSales(), is("1122-11222"));
        assertThat(company.getLogoRefId(), is("Company Logo 112"));
    }
}
