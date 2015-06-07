package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.Anonymous;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.PartyType;
import com.google.common.collect.Lists;

public class JdbcAnonymousDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcAnonymousDao anonymousDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("anonymous.sql");
    }

    @Test
    public void loadByIdThatExistsReturnsAnonymous() {
        Anonymous anonymous = anonymousDao.load(99911L);

        assertThat(anonymous.getId(), is(99911L));
        assertThat(anonymous.getApprovalStatus(), is(ApprovalStatus.UNPROFILED));
        assertThat(anonymous.getVersion(), is(1));
        assertThat(anonymous.getSnapshotId(), is(999111L));

        Contact contact = anonymous.getContact();
        assertThat(contact.getFirstName(), is("First 111"));
        assertThat(contact.getLastName(), is("last_name"));
        assertThat(contact.getEmailAddress(), is("Email 111"));
        assertThat(contact.getBusinessTelephoneNumber(), is("Mobile 111"));
        assertThat(contact.getIpAddress(), is("127.0.0.1"));
        assertThat(contact.getLanguage(), is(Language.ENGLISH));
        assertThat(contact.getCountry(), is("Country 111"));

        Company company = anonymous.getCompany();
        assertThat(company.getEnglishName(), is("English Name 111"));
        assertThat(company.getWebsite(), is("http://Website 111"));
    }

    @Test
    public void createInsertsIntoBaseSnapshotAndEventTables() {
        Contact contact = new Contact(null, null, "Tiger", "Woods", null, null, "tiger.woods@test.com", null, null,
                null, "1234567", "123.123.123.123", null, new Date());
        Company company = new Company("English Company Name", null, null, null, null, "website@website.com", null,
                null, null, null, null);
        Anonymous anonymous = new Anonymous(contact, company);

        Anonymous createdAnonymous = anonymousDao.insert(anonymous);
        assertThat(createdAnonymous.getId(), notNullValue());
        assertThat(createdAnonymous.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ?", createdAnonymous.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(createdAnonymous.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(createdAnonymous.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?",
                createdAnonymous.getId());
        assertThat((Date) partyRowMap.get("registration_date"), is(today()));
        assertThat(partyRowMap.get("ip_address").toString(), is("123.123.123.123"));

        Map<String, Object> partyAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit WHERE party_audit_id = ?", createdAnonymous.getSnapshotId());
        assertThat(partyAuditRowMap.get("first_name").toString(), is(anonymous.getContact().getFirstName()));
        assertThat(partyAuditRowMap.get("last_name").toString(), is(anonymous.getContact().getLastName()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(anonymous.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("business_phone").toString(), is(anonymous.getContact()
                .getBusinessTelephoneNumber()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(anonymous.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("party_type").toString(), is(PartyType.ANONYMOUS.toString()));
    }

    @Test
    public void updateInsertsIntoSnapshotAndEventTables() {
        Contact contact = new Contact(null, null, "first_name_updated", "last_name", null, null,
                "email_address_updated", null, null, null, "businessPhone", "123.123.123.123", null, new Date());
        Company company = new Company("company_name_english", null, null, null, null, "company_website", null, null,
                null, null, null);

        Anonymous anonymous = new Anonymous(contact, company);
        anonymous.setId(99911L);

        Anonymous updatedAnonymous = anonymousDao.update(anonymous);
        assertThat(updatedAnonymous.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        99911L, 99911L);
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(99911L));
        assertThat((String) partyAuditStatusRowMap.get("event_type"), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));

        assertThat((String) partyAuditStatusRowMap.get("first_name"), is("first_name_updated"));
        assertThat((String) partyAuditStatusRowMap.get("last_name"), is("last_name"));
        assertThat((String) partyAuditStatusRowMap.get("email_address"), is("email_address_updated"));
        assertThat((String) partyAuditStatusRowMap.get("business_phone"), is("businessPhone"));

        assertThat((String) partyAuditStatusRowMap.get("company_name_english"), is("company_name_english"));
        assertThat((String) partyAuditStatusRowMap.get("company_website"), is("http://company_website"));
    }
}
