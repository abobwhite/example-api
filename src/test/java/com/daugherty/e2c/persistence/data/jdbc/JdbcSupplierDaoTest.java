package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Gender;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.persistence.data.Hashids;
import com.google.common.collect.Lists;

public class JdbcSupplierDaoTest extends BaseJdbcDaoTest {

    static final int SUPPLIER_SQL_PARTY_COUNT = 1;
    static final int SUPPLIER_SQL_PARTY_AUDIT_COUNT = 2;

    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private JdbcSupplierDao supplierDao;
    @Inject
    private Hashids hashids;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("supplier.sql");
    }

    @Test
    public void loadLatestByIdThatExistsReturnsSupplierInEnglish() {
        Supplier supplier = supplierDao.loadLatest(99911L, Locale.ENGLISH);

        assertSupplier99911(supplier, "Description 112");
    }

    @Test
    public void loadLatestByIdThatExistsReturnsSupplierInChinese() {
        Supplier supplier = supplierDao.loadLatest(99911L, Locale.CHINESE);

        assertSupplier99911(supplier, "translated description");
    }

    @Test
    public void loadLatestByIdThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        supplierDao.loadLatest(123456789L, Locale.ENGLISH);
    }

    @Test
    public void loadApprovedByIdThatExistsReturnsSupplierInEnglish() {
        Supplier supplier = supplierDao.loadApproved(99911L, Locale.ENGLISH);

        assertSupplier99911(supplier, "Description 112");
    }

    @Test
    public void loadApprovedByIdThatExistsReturnsSupplierInChinese() {
        Supplier supplier = supplierDao.loadApproved(99911L, Locale.CHINESE);

        assertSupplier99911(supplier, "translated description");
    }

    @Test
    public void loadApprovedByIdsThatExistsReturnsSuppliersInEnglish() {
        List<Supplier> suppliers = supplierDao.loadApprovedBySupplierIds(Lists.newArrayList(99911L), Locale.ENGLISH);

        assertSupplier99911(suppliers.get(0), "Description 112");
    }

    @Test
    public void loadApprovedByIdsThatExistsReturnsSuppliersInChinese() {
        List<Supplier> suppliers = supplierDao.loadApprovedBySupplierIds(Lists.newArrayList(99911L), Locale.CHINESE);

        assertSupplier99911(suppliers.get(0), "translated description");
    }

    @Test
    public void loadApprovedByIdThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        supplierDao.loadApproved(123456789L, Locale.ENGLISH);
    }

    @Test
    public void loadSupplierIdMatchingLegacyIdForApprovedSupplierReturnsMatchingId() throws Exception {
        assertThat(supplierDao.loadSupplierIdMatchingLegacyId(888L), is("Y40dgNWM"));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void loadSupplierIdMatchingLegacyIdThatDoesNotExistThrowsEmptyResultDataAccessException() throws Exception {
        supplierDao.loadSupplierIdMatchingLegacyId(999L);
    }

    @Test
    public void createInsertsIntoBaseSnapshotAndEventTables() {
        Contact contact = new Contact("Tiger", "Woods", "USA", "St. Louis", "tiger.woods@test.com", "123.123.123.123",
                Language.ENGLISH, new Date());
        Supplier supplier = new Supplier(null, contact, null);

        Supplier createdSupplier = supplierDao.insert(supplier);
        assertThat(createdSupplier.getId(), notNullValue());
        assertThat(createdSupplier.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ?", createdSupplier.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(createdSupplier.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(createdSupplier.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?",
                createdSupplier.getId());
        assertThat((Date) partyRowMap.get("registration_date"), is(today()));
        assertThat(partyRowMap.get("ip_address").toString(), is("123.123.123.123"));
        assertThat((Long) partyRowMap.get("language_id"), is(Language.ENGLISH.getId()));

        Map<String, Object> partyAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit WHERE party_audit_id = ?", createdSupplier.getSnapshotId());
        assertThat(partyAuditRowMap.get("first_name").toString(), is(supplier.getContact().getFirstName()));
        assertThat(partyAuditRowMap.get("last_name").toString(), is(supplier.getContact().getLastName()));
        assertThat(partyAuditRowMap.get("country").toString(), is(supplier.getContact().getCountry()));
        // assertThat(partyAuditRowMap.get("province").toString(), is(supplier.getContact().getProvince()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(supplier.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("party_type").toString(), is(PartyType.SUPPLIER.toString()));
    }

    @Test
    public void assertNewlyCreatedPartyUpdatesWithPublicId() {
        Contact contact = new Contact("Jerome", "Bollocks", "USA", "St. Louis", "Jerome.Bollocks@muessig.com",
                "123.123.123.123", Language.ENGLISH, new Date());
        Supplier supplier = new Supplier(null, contact, null);

        Supplier createdSupplier = supplierDao.insert(supplier);

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?",
                createdSupplier.getId());

        assertThat((String) partyRowMap.get("public_party_id"), is(hashids.encode(createdSupplier.getId())));
    }

    @Test
    public void updateInsertsIntoSnapshotAndEventTables() {
        Contact contact = new Contact("title", Gender.FEMALE, "first_name_updated", "last_name_updated", "country",
                "province_updated", "email_address_updated", "skype_id", "msn_id", "icq_id", "mobilePhone",
                "123.123.123.123", Language.SPANISH, new Date());
        Company company = new Company("company_name_english", "company_name_chinese", "description",
                new ArrayList<BusinessType>(), "1-10", "company_website", 1985, "200-300", "100-150",
                "company_logo_link", "video_link");

        Supplier supplier = new Supplier(99911L, "3VlkjQDP", contact, company, ApprovalStatus.DRAFT, 2, null,
                "license_link", null, null);

        Supplier updatedSupplier = supplierDao.update(supplier);
        assertThat(updatedSupplier.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        99911L, 99911L);
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(99911L));
        assertThat((String) partyAuditStatusRowMap.get("event_type"), is(ApprovalStatus.DRAFT.getName()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(2));

        assertThat((String) partyAuditStatusRowMap.get("license_link"), is("license_link"));

        assertThat((String) partyAuditStatusRowMap.get("gender"), is(Gender.FEMALE.getReadableName()));
        assertThat((String) partyAuditStatusRowMap.get("first_name"), is("first_name_updated"));
        assertThat((String) partyAuditStatusRowMap.get("last_name"), is("last_name_updated"));
        assertThat((String) partyAuditStatusRowMap.get("country"), is("country"));
        assertThat((String) partyAuditStatusRowMap.get("email_address"), is("email_address_updated"));
        assertThat((String) partyAuditStatusRowMap.get("skype_id"), is("skype_id"));
        assertThat((String) partyAuditStatusRowMap.get("msn_id"), is("msn_id"));
        assertThat((String) partyAuditStatusRowMap.get("icq_id"), is("icq_id"));

        assertThat((String) partyAuditStatusRowMap.get("company_name_english"), is("company_name_english"));
        assertThat((String) partyAuditStatusRowMap.get("company_description"), is("description"));
        assertThat((String) partyAuditStatusRowMap.get("number_of_employees"), is("1-10"));
        assertThat((String) partyAuditStatusRowMap.get("company_website"), is("http://company_website"));
        assertThat((Integer) partyAuditStatusRowMap.get("year_established"), is(1985));
        assertThat((String) partyAuditStatusRowMap.get("company_logo_link"), is("company_logo_link"));
        assertThat((String) partyAuditStatusRowMap.get("video_link"), is("video_link"));
    }

    @Test
    public void recordEventInsertsIntoEventTableOnly() {
        Contact contact = new Contact("Tiger", "Woods", "USA", "St. Louis", "tiger.woods@test.com", "123.123.123.123",
                Language.ENGLISH, new Date());
        Supplier supplier = new Supplier(null, contact, null);
        supplier.setId(99911L);
        supplier.setSnapshotId(999111L);

        supplierDao.recordEvent(supplier);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit_status WHERE party_id = ? AND party_audit_status_id NOT IN (9991111, 9991121)",
                        supplier.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(supplier.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(supplier.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.UNPROFILED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT count(*) party_count FROM party");
        assertThat((Long) partyRowMap.get("party_count"), is(new Long(DATA_SQL_PARTY_COUNT + SUPPLIER_SQL_PARTY_COUNT)));

        Map<String, Object> partyAuditRowMap = jdbcTemplate
                .queryForMap("SELECT count(*) party_audit_count FROM party_audit");
        assertThat((Long) partyAuditRowMap.get("party_audit_count"), is(new Long(DATA_SQL_PARTY_AUDIT_COUNT
                + SUPPLIER_SQL_PARTY_AUDIT_COUNT)));
    }

    @Test
    public void switchToSupplier() {
        Party party = partyDao.loadById(22L);

        Supplier supplier = new Supplier(party.getId(), party.getPublicId(), party.getContact(), party.getCompany(),
                ApprovalStatus.DRAFT, 2, party.getSnapshotId(), null, null, null);

        supplierDao.switchToSupplier(supplier);

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        22L, 22L);
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(22L));
        assertThat((String) partyAuditStatusRowMap.get("event_type"), is(ApprovalStatus.DRAFT.getName()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(2));
        assertThat((String) partyAuditStatusRowMap.get("party_type"), is(PartyType.SUPPLIER.getType()));

        assertThat((String) partyAuditStatusRowMap.get("first_name"), is("Arthur"));
        assertThat((String) partyAuditStatusRowMap.get("last_name"), is("Dent"));
        assertThat((String) partyAuditStatusRowMap.get("country"), is("United Kingdom"));
        assertThat((String) partyAuditStatusRowMap.get("email_address"), is("dent@eastindiatea.com"));
        assertThat((String) partyAuditStatusRowMap.get("skype_id"), is("dent-skype"));
        assertThat((String) partyAuditStatusRowMap.get("msn_id"), is("People use MSN?"));
        assertThat((String) partyAuditStatusRowMap.get("icq_id"), is("People use ICQ?"));

        assertThat((String) partyAuditStatusRowMap.get("company_name_english"), is("East India Tea Company"));
        assertThat((String) partyAuditStatusRowMap.get("company_description"),
                is("Brings tea from India back to England"));
        assertThat((String) partyAuditStatusRowMap.get("number_of_employees"), is("1-10"));
        assertThat((String) partyAuditStatusRowMap.get("company_website"), is("http://www.eastindiatea.com"));
        assertThat((Integer) partyAuditStatusRowMap.get("year_established"), is(1800));
    }

    private void assertSupplier99911(Supplier supplier, String companyDescription) {
        assertThat(supplier.getId(), is(99911L));
        assertThat(supplier.getApprovalStatus(), is(ApprovalStatus.APPROVED));
        assertThat(supplier.getVersion(), is(1));
        assertThat(supplier.getSnapshotId(), is(999112L));

        assertThat(supplier.getExportLicenseRefId(), is("License 112"));
        assertThat(supplier.getCertificationId(), is(555L));
        assertThat(supplier.getMembershipLevel(), is(1));
        assertThat(supplier.isAdvancedWebAndMailCapabilityEnabled(), is(true));
        assertThat(supplier.getProvisionalMembership().getLevel().getValue(), is(2));

        Contact contact = supplier.getContact();
        assertThat(contact.getGender(), is(Gender.FEMALE));
        assertThat(contact.getFirstName(), is("First 112"));
        assertThat(contact.getLastName(), is("Last 112"));
        assertThat(contact.getCountry(), is("Country 112"));
        assertThat(contact.getEmailAddress(), is("Email 112"));
        assertThat(contact.getSkypeRefId(), is("Skype 112"));
        assertThat(contact.getMsnRefId(), is("MSN 112"));
        assertThat(contact.getIcqRefId(), is("ICQ 112"));
        assertThat(contact.getIpAddress(), is("127.0.0.1"));
        assertThat(contact.getLanguage(), is(Language.ENGLISH));

        Company company = supplier.getCompany();
        assertThat(company.getEnglishName(), is("Approved Supplier 99911"));
        assertThat(company.getDescription(), is(companyDescription));
        assertThat(company.getNumberOfEmployees(), is("11-20"));
        assertThat(company.getWebsite(), is("http://Website 112"));
        assertThat(company.getYearEstablished(), is(2012));
        assertThat(company.getTotalAnnualSales(), is("1122-11222"));
        assertThat(company.getLogoRefId(), is("Company Logo 112"));
    }

}
