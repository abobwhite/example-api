package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Buyer;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.google.common.collect.Lists;

public class JdbcBuyerDaoTest extends BaseJdbcDaoTest {

    static final int BUYER_SQL_PARTY_COUNT = 1;
    static final int BUYER_SQL_PARTY_AUDIT_COUNT = 2;

    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private JdbcBuyerDao buyerDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyer.sql");
    }

    @Test
    public void loadByIdThatExistsReturnsBuyer() {
        Buyer buyer = buyerDao.loadLatest(99911L);

        assertThat(buyer.getId(), is(99911L));
        assertThat(buyer.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(buyer.getVersion(), is(1));
        assertThat(buyer.getSnapshotId(), is(999112L));

        assertThat(buyer.getImportLicenseRefId(), is("License 112"));

        Contact contact = buyer.getContact();
        assertThat(contact.getTitle(), is("Title 112"));
        assertThat(contact.getFirstName(), is("First 112"));
        assertThat(contact.getLastName(), is("Last 112"));
        assertThat(contact.getCountry(), is("Country 112"));
        assertThat(contact.getProvince(), is("Province 112"));
        assertThat(contact.getEmailAddress(), is("Email 112"));
        assertThat(contact.getSkypeRefId(), is("Skype 112"));
        assertThat(contact.getMsnRefId(), is("MSN 112"));
        assertThat(contact.getIcqRefId(), is("ICQ 112"));
        assertThat(contact.getBusinessTelephoneNumber(), is("Business 112"));
        assertThat(contact.getIpAddress(), is("127.0.0.1"));
        assertThat(contact.getLanguage(), is(Language.CHINESE));

        Company company = buyer.getCompany();
        assertThat(company.getEnglishName(), is("English Name 112"));
        assertThat(company.getChineseName(), is("Chinese Name 112"));
        assertThat(company.getDescription(), is("Description 112"));
        assertThat(company.getNumberOfEmployees(), is("11-20"));
        assertThat(company.getWebsite(), is("http://Website 112"));
        assertThat(company.getYearEstablished(), is(2012));
        assertThat(company.getTotalAnnualSales(), is("1122-11222"));
        assertThat(company.getTotalImportAmount(), is("112-1122"));
        assertThat(company.getLogoRefId(), is("Company Logo 112"));
    }

    @Test
    public void loadByIdThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        buyerDao.loadLatest(123456789L);
    }

    @Test
    public void createInsertsIntoBaseSnapshotAndEventTables() {
        Contact contact = new Contact("Tiger", "Woods", "USA", "St. Louis", "tiger.woods@test.com", "123.123.123.123",
                Language.CHINESE, new Date());
        Buyer buyer = new Buyer(null, contact, null);

        Buyer createdBuyer = buyerDao.insert(buyer);
        assertThat(createdBuyer.getId(), notNullValue());
        assertThat(createdBuyer.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ?", createdBuyer.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(createdBuyer.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(createdBuyer.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?",
                createdBuyer.getId());
        assertThat((Date) partyRowMap.get("registration_date"), is(today()));
        assertThat(partyRowMap.get("ip_address").toString(), is("123.123.123.123"));
        assertThat((Long) partyRowMap.get("language_id"), is(Language.CHINESE.getId()));

        Map<String, Object> partyAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit WHERE party_audit_id = ?", createdBuyer.getSnapshotId());
        assertThat(partyAuditRowMap.get("first_name").toString(), is(buyer.getContact().getFirstName()));
        assertThat(partyAuditRowMap.get("last_name").toString(), is(buyer.getContact().getLastName()));
        assertThat(partyAuditRowMap.get("country").toString(), is(buyer.getContact().getCountry()));
        assertThat(partyAuditRowMap.get("province").toString(), is(buyer.getContact().getProvince()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(buyer.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("party_type").toString(), is(PartyType.BUYER.toString()));
    }

    @Test
    public void updateInsertsIntoSnapshotAndEventTables() {
        Contact contact = new Contact("title", null, "first_name_updated", "last_name_updated", "country",
                "province_updated", "email_address_updated", "skype_id", "msn_id", "icq_id", "businessPhone",
                "123.123.123.123", Language.CHINESE, new Date());
        Company company = new Company("company_name_english", "company_name_chinese", "description",
                new ArrayList<BusinessType>(), "1-10", "company_website", 1985, "200-300", "100-150",
                "company_logo_link", null);
        Buyer buyer = new Buyer(111L, "g3Vl2w0b", contact, company, ApprovalStatus.DRAFT, 2, null, "license_link");

        Buyer updatedBuyer = buyerDao.update(buyer);
        assertThat(updatedBuyer.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        111L, 111L);
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(111L));
        assertThat((String) partyAuditStatusRowMap.get("event_type"), is(ApprovalStatus.DRAFT.getName()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(2));

        assertThat((String) partyAuditStatusRowMap.get("license_link"), is("license_link"));

        assertThat((String) partyAuditStatusRowMap.get("title"), is("title"));
        assertThat((String) partyAuditStatusRowMap.get("first_name"), is("first_name_updated"));
        assertThat((String) partyAuditStatusRowMap.get("last_name"), is("last_name_updated"));
        assertThat((String) partyAuditStatusRowMap.get("country"), is("country"));
        assertThat((String) partyAuditStatusRowMap.get("province"), is("province_updated"));
        assertThat((String) partyAuditStatusRowMap.get("email_address"), is("email_address_updated"));
        assertThat((String) partyAuditStatusRowMap.get("skype_id"), is("skype_id"));
        assertThat((String) partyAuditStatusRowMap.get("msn_id"), is("msn_id"));
        assertThat((String) partyAuditStatusRowMap.get("icq_id"), is("icq_id"));
        assertThat((String) partyAuditStatusRowMap.get("business_phone"), is("businessPhone"));

        assertThat((String) partyAuditStatusRowMap.get("company_name_english"), is("company_name_english"));
        assertThat((String) partyAuditStatusRowMap.get("company_name_chinese"), is("company_name_chinese"));
        assertThat((String) partyAuditStatusRowMap.get("company_description"), is("description"));
        assertThat((String) partyAuditStatusRowMap.get("number_of_employees"), is("1-10"));
        assertThat((String) partyAuditStatusRowMap.get("company_website"), is("http://company_website"));
        assertThat((Integer) partyAuditStatusRowMap.get("year_established"), is(1985));
        assertThat((String) partyAuditStatusRowMap.get("company_logo_link"), is("company_logo_link"));
    }

    @Test
    public void recordEventInsertsIntoEventTableOnly() {
        Contact contact = new Contact("Tiger", "Woods", "USA", "St. Louis", "tiger.woods@test.com", "123.123.123.123",
                Language.CHINESE, new Date());
        Buyer buyer = new Buyer(null, contact, null);
        buyer.setId(99911L);
        buyer.setSnapshotId(999111L);

        buyerDao.recordEvent(buyer);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit_status WHERE party_id = ? AND party_audit_status_id NOT IN (9991111, 9991121)",
                        buyer.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(buyer.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(buyer.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT + BUYER_SQL_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + BUYER_SQL_PARTY_AUDIT_COUNT)));
    }

    @Test
    public void switchToBuyer() {
        Party party = partyDao.loadById(21L);

        Buyer buyer = new Buyer(party.getId(), party.getPublicId(), party.getContact(), party.getCompany(),
                ApprovalStatus.DRAFT, 2, party.getSnapshotId(), null);

        buyerDao.switchToBuyer(buyer);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        21L, 21L);
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(21L));
        assertThat((String) partyAuditStatusRowMap.get("event_type"), is(ApprovalStatus.DRAFT.getName()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(2));
        assertThat((String) partyAuditStatusRowMap.get("party_type"), is(PartyType.BUYER.getType()));

        assertThat((String) partyAuditStatusRowMap.get("first_name"), is("Ford"));
        assertThat((String) partyAuditStatusRowMap.get("last_name"), is("Prefect"));
        assertThat((String) partyAuditStatusRowMap.get("country"), is("Albania"));
        assertThat((String) partyAuditStatusRowMap.get("email_address"), is("prefect@megadodo.com"));
        assertThat((String) partyAuditStatusRowMap.get("skype_id"), is("prefect-skype"));
        assertThat((String) partyAuditStatusRowMap.get("msn_id"), is("Really? MSN?"));
        assertThat((String) partyAuditStatusRowMap.get("icq_id"), is("Really? ICQ?"));

        assertThat((String) partyAuditStatusRowMap.get("company_name_english"), is("Megadodo Publications"));
        assertThat(
                (String) partyAuditStatusRowMap.get("company_description"),
                is("Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books. Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books."));
        assertThat((String) partyAuditStatusRowMap.get("number_of_employees"), is("1 - 10"));
        assertThat((String) partyAuditStatusRowMap.get("company_website"), is("http://uww.megadodo.com"));
        assertThat((Integer) partyAuditStatusRowMap.get("year_established"), is(1982));
    }

}
