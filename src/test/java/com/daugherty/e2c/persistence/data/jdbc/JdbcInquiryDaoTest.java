package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.BusinessType;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.security.Role;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class JdbcInquiryDaoTest extends BaseJdbcDaoTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company SUPPLIER_21_COMPANY = new Company(
            "Megadodo Publications",
            null,
            "Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books. Publisher of The Hitch-Hikers Guide to the Galaxy.  This is a wonderful publisher that is known for publishing good books.",
            new ArrayList<BusinessType>(), "1 - 10", "uww.megadodo.com", 1982, "$1 - $500,000", null, null, null);
    private static final Company SUPPLIER_25_COMPANY = new Company("Jedi Enterprises", null,
            "Peacekeepers of a galaxy far, far away", new ArrayList<BusinessType>(), "51-100", "www.anskywlkr.com",
            -2500, "0.00", null, null, null);
    private static final Company SUPPLIER_30_COMPANY = new Company("Cherry Spice", null, null, null, null, null, null,
            null, null, null, null);

    private static final Contact SUPPLIER_21_CONTACT = new Contact(null, null, "Ford", "Prefect", "Albania", null,
            "prefect@megadodo.com", "prefect-skype", "Really? MSN?", "Really? ICQ?", "000-111-2222", "127.0.0.1",
            Language.ENGLISH, null);
    private static final Contact SUPPLIER_25_CONTACT = new Contact(null, null, "Anakin", "Skywalker", "Tatooine", null,
            "yoda@anskywlkr.com", "go away", "i mean it", "then die", "546-372-8190", "127.0.0.1", Language.ENGLISH,
            null);

    private static final Party BUYER = new Party(22L, "pBVQJNb4", null, BUYER_COMPANY, null, PartyType.BUYER, null,
            null);

    private static final Party SUPPLIER_21 = new Party(21L, "Y40dgNWM", SUPPLIER_21_CONTACT, SUPPLIER_21_COMPANY,
            ApprovalStatus.APPROVED, PartyType.SUPPLIER, 1, 2101L);

    private static final Party SUPPLIER_25 = new Party(25L, "xbPMBVjY", SUPPLIER_25_CONTACT, SUPPLIER_25_COMPANY,
            ApprovalStatus.WAITING_FOR_INFORMATION, PartyType.SUPPLIER, 1, 2501L);
    private static final Party SUPPLIER_30 = new Party(30L, "690O6NL4", null, SUPPLIER_30_COMPANY, null,
            PartyType.SUPPLIER, null, null);

    private static final long BUYER_INQUIRY_ID = 221L;
    private static final long SUPPLIER_30_INQUIRY_ID = 301L;

    @Inject
    private JdbcInquiryDao dao;

    private Authentication originalAuthentication;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("inquiry.sql");
    }

    @Before
    public void rememberAuthentication() {
        originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @After
    public void restoreAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
    }

    @Test
    public void loadBasketWithoutProducts() throws Exception {
        Inquiry inquiry = dao.loadBasket(BUYER.getId());

        assertThat(inquiry, is(notNullValue()));
        assertThat(inquiry.getId(), is(BUYER.getId()));
        assertThat(inquiry.getProductIds().size(), is(0));
    }

    @Test
    public void loadBasketWithProducts() throws Exception {
        Inquiry inquiry = dao.loadBasket(SUPPLIER_21.getId());

        assertThat(inquiry, is(notNullValue()));
        assertThat(inquiry.getId(), is(SUPPLIER_21.getId()));
        assertThat(inquiry.getProductIds().size(), is(3));
        assertThat(inquiry.getProductIds().get(0), is(41L));
        assertThat(inquiry.getProductIds().get(1), is(42L));
        assertThat(inquiry.getProductIds().get(2), is(51L));
    }

    @Test
    public void findSuppliersForProductsInBasket() throws Exception {
        Multimap<Party, Long> productsBySupplier = dao.findSuppliersForProductsInBasket(SUPPLIER_21.getId());

        for (Party party : productsBySupplier.keySet()) {
            if (party.getId().equals(25L)) {
                assertTrue(party.getCompany().equals(SUPPLIER_25_COMPANY));
                assertTrue(party.getContact().equals(SUPPLIER_25_CONTACT));
                assertTrue(party.equals(SUPPLIER_25));
            }
        }

        assertThat(productsBySupplier.keySet().size(), is(2));
        assertThat(productsBySupplier.keySet().contains(SUPPLIER_21), is(true));
        assertThat(productsBySupplier.keySet().contains(SUPPLIER_25), is(true));
        assertThat(productsBySupplier.get(SUPPLIER_21).size(), is(1));
        assertThat(productsBySupplier.get(SUPPLIER_21).contains(41L), is(true));
        assertThat(productsBySupplier.get(SUPPLIER_25).size(), is(2));
        assertThat(productsBySupplier.get(SUPPLIER_25).contains(42L), is(true));
        assertThat(productsBySupplier.get(SUPPLIER_25).contains(51L), is(true));
    }

    @Test
    public void loadPendingInquiryThatExists() throws Exception {
        Inquiry inquiry = dao.loadPendingInquiry(BUYER_INQUIRY_ID);

        assertThatInquiryMatchesExpectedValuesFor221(inquiry);
    }

    @Test
    public void findSuppliersForProductsInPendingInquiry() throws Exception {
        Multimap<Party, Long> productsBySupplier = dao.findSuppliersForProductsInPendingInquiry(BUYER_INQUIRY_ID);

        assertThat(productsBySupplier.keySet().size(), is(1));
        assertThat(productsBySupplier.keySet().contains(SUPPLIER_21), is(true));
        assertThat(productsBySupplier.get(SUPPLIER_21).size(), is(1));
        assertThat(productsBySupplier.get(SUPPLIER_21).contains(41L), is(true));
    }

    @Test
    public void findPendingInquiriesWithoutCriteriaReturnsAllPendingInquiries() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null, null, false, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(2));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(0));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(1));
    }

    @Test
    public void findPendingInquiriesWithSenderIdCriterionOnlyReturnsMatchingPendingInquiries() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(22L, null, null, null, false, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(1));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(0));
    }

    @Test
    public void findPendingInquiriesWithOriginatorCompanyCriterionOnlyReturnsMatchingPendingInquiries()
            throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, "Cherry Spice", null, false, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(1));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
    }

    @Test
    public void findPendingInquiriesWithDisapprovedCriterionOnlyReturnsMatchingPendingInquiries() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, true, null, null, false, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(1));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
    }

    @Test
    public void findPendingInquiriesSortsBySenderName() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null, Inquiry.ORIGINATOR_SERIAL_PROPERTY,
                false, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(2));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(1));
    }

    @Test
    public void findPendingInquiriesSortsBySubject() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null, Inquiry.SUBJECT_SERIAL_PROPERTY, true,
                1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(2));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(1));
    }

    @Test
    public void findPendingInquiriesSortsByBody() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null, Inquiry.BODY_SERIAL_PROPERTY, true, 1,
                25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(2));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(1));
    }

    @Test
    public void findPendingInquiriesSortsBySubmissionTime() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null,
                Inquiry.SUBMISSION_TIME_SERIAL_PROPERTY, true, 1, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(2));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
        assertThatInquiryMatchesExpectedValuesFor221(inquiries.get(1));
    }

    @Test
    public void findPaginates() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(null, null, null, null, false, 2, 25);
        List<Inquiry> inquiries = dao.findPendingInquiries(queryCriteria);

        assertThat(inquiries, is(notNullValue()));
        assertThat(inquiries.size(), is(1));
        assertThatInquiryMatchesExpectedValuesFor301(inquiries.get(0));
    }

    @Test
    public void findProductsForPendingInquiriesThatExist() throws Exception {
        Multimap<Long, Long> productIdsByInquiry = dao.findProductsForPendingInquiries(Lists.newArrayList(
                BUYER_INQUIRY_ID, SUPPLIER_30_INQUIRY_ID));

        assertThat(productIdsByInquiry.keySet().size(), is(2));
        assertThat(productIdsByInquiry.keySet().contains(BUYER_INQUIRY_ID), is(true));
        assertThat(productIdsByInquiry.keySet().contains(SUPPLIER_30_INQUIRY_ID), is(true));
        assertThat(productIdsByInquiry.get(BUYER_INQUIRY_ID).size(), is(1));
        assertThat(productIdsByInquiry.get(BUYER_INQUIRY_ID).contains(41L), is(true));
        assertThat(productIdsByInquiry.get(SUPPLIER_30_INQUIRY_ID).size(), is(2));
        assertThat(productIdsByInquiry.get(SUPPLIER_30_INQUIRY_ID).contains(42L), is(true));
        assertThat(productIdsByInquiry.get(SUPPLIER_30_INQUIRY_ID).contains(51L), is(true));
    }

    @Test
    public void findProductsForPendingInquiriesThatDoNotExist() throws Exception {
        Multimap<Long, Long> productIdsByInquiry = dao.findProductsForPendingInquiries(Lists.<Long> newArrayList());

        assertThat(productIdsByInquiry.size(), is(0));
    }

    @Test
    public void findMessageTagsForPendingInquiriesThatExist() throws Exception {
        Multimap<Long, MessageTag> messageTagsByInquiry = dao.findMessageTagsForPendingInquiries(Lists.newArrayList(
                BUYER_INQUIRY_ID, SUPPLIER_30_INQUIRY_ID));

        assertThat(messageTagsByInquiry.keySet().size(), is(1));
        assertThat(messageTagsByInquiry.keySet().contains(BUYER_INQUIRY_ID), is(true));
        assertThat(messageTagsByInquiry.get(BUYER_INQUIRY_ID).size(), is(1));
        assertThat(messageTagsByInquiry.get(BUYER_INQUIRY_ID).contains(MessageTag.COMPANY_OVERVIEW), is(true));
    }

    @Test
    public void findMessageTagsForPendingInquiriesThatDoNotExist() throws Exception {
        Multimap<Long, MessageTag> messageTagsByInquiry = dao.findMessageTagsForPendingInquiries(Lists
                .<Long> newArrayList());

        assertThat(messageTagsByInquiry.size(), is(0));
    }

    @Test
    public void insertProductForPartyWhenProductNotInInquiryBasket() {
        dao.insertProductForParty(BUYER.getId(), 41L);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "inquiry_basket", "party_id = " + BUYER.getId()),
                is(1));
    }

    @Test
    public void insertProductForPartyWhenProductAlreadyInInquiryBasket() {
        expectedException.expect(DuplicateKeyException.class);
        expectedException.expectMessage("unique constraint or index violation: INQUIRY_BASKET_AK");

        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21.getId()));

        dao.insertProductForParty(SUPPLIER_21.getId(), 41L);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "inquiry_basket",
                "party_id = " + SUPPLIER_21.getId()), is(2));
    }

    @Test
    public void deleteProductForPartyWhenProductExistsInInquiryBasket() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21.getId()));

        dao.deleteProductForParty(SUPPLIER_21.getId(), 41L);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "inquiry_basket",
                "party_id = " + SUPPLIER_21.getId()), is(2));
    }

    @Test
    public void clearBasket() throws Exception {
        dao.clearBasket(SUPPLIER_21.getId());

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "inquiry_basket",
                "party_id = " + SUPPLIER_21.getId()), is(0));
        assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "inquiry_basket"), is(1));
    }

    @Test
    public void insertPendingInquiry() throws Exception {
        Inquiry inquiry = new Inquiry(null, BUYER, Lists.newArrayList(1L, 2L, 3L), "subject", "body",
                Lists.newArrayList(MessageTag.FOB_PRICE));

        Inquiry createdPendingInquiry = dao.insertPendingInquiry(inquiry);

        Map<String, Object> pendingInquiryRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM pending_inquiry WHERE pending_inquiry_id = ?", createdPendingInquiry.getId());
        assertThat((Long) pendingInquiryRowMap.get("party_id"), is(inquiry.getOriginator().getId()));
        assertThat(pendingInquiryRowMap.get("subject").toString(), is(inquiry.getSubject()));
        assertThat(pendingInquiryRowMap.get("body").toString(), is(inquiry.getBody()));
        assertThat((Date) pendingInquiryRowMap.get("submission_timestamp"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
        assertThat((Boolean) pendingInquiryRowMap.get("is_disapproved"), is(false));

        List<Map<String, Object>> pendingInquiryProductRowMaps = jdbcTemplate.queryForList(
                "SELECT * FROM pending_inquiry_product WHERE pending_inquiry_id = ?", createdPendingInquiry.getId());
        assertThat(pendingInquiryProductRowMaps.size(), is(inquiry.getProductIds().size()));
        for (int i = 0; i < inquiry.getProductIds().size(); i++) {
            assertThat((Long) pendingInquiryProductRowMaps.get(i).get("product_id"), is(inquiry.getProductIds().get(i)));
        }

        List<Map<String, Object>> pendingInquiryMessageTagRowMaps = jdbcTemplate
                .queryForList("SELECT * FROM pending_inquiry_message_tag WHERE pending_inquiry_id = ?",
                        createdPendingInquiry.getId());
        assertThat(pendingInquiryMessageTagRowMaps.size(), is(inquiry.getMessageTags().size()));
        for (int i = 0; i < inquiry.getMessageTags().size(); i++) {
            assertThat((Long) pendingInquiryMessageTagRowMaps.get(i).get("message_tag_id"), is(inquiry.getMessageTags()
                    .get(i).getId()));
        }
    }

    @Test
    public void deletePendingInquiryForInquiryThatExists() throws Exception {
        dao.deletePendingInquiry(BUYER_INQUIRY_ID);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "pending_inquiry_message_tag",
                "pending_inquiry_id = " + BUYER_INQUIRY_ID), is(0));
        assertThat(
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "pending_inquiry_product", "pending_inquiry_id = "
                        + BUYER_INQUIRY_ID), is(0));
        assertThat(
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "pending_inquiry", "pending_inquiry_id = "
                        + BUYER_INQUIRY_ID), is(0));
    }

    @Test
    public void deletePendingInquiriesForParty() throws Exception {

        QueryCriteria criteria = dao.createQueryCriteria(30L, true, null, null, false, 1, 100);

        List<Inquiry> pendingInquiries = dao.findPendingInquiries(criteria);

        assertThat(pendingInquiries.size(), is(dao.deletePendingInquiriesByPartyId(30L)));
    }

    @Test
    public void disapprovePendingInquiryThatExists() throws Exception {
        dao.disapprovePendingInquiry(BUYER_INQUIRY_ID);

        Map<String, Object> pendingInquiryRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM pending_inquiry WHERE pending_inquiry_id = ?", BUYER_INQUIRY_ID);
        assertThat((Boolean) pendingInquiryRowMap.get("is_disapproved"), is(true));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Long partyId) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(partyId), true);
        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

    private void assertThatInquiryMatchesExpectedValuesFor221(Inquiry inquiry) {
        assertThat(inquiry.getId(), is(BUYER_INQUIRY_ID));
        assertThat(inquiry.getOriginator().getId(), is(BUYER.getId()));
        assertThat(inquiry.getOriginator().getCompany().getEnglishName(), is(BUYER.getCompany().getEnglishName()));
        assertThat(inquiry.getOriginator().getPartyType(), is(BUYER.getPartyType()));
        assertThat(inquiry.getSubject(), is("Arthur Spam List"));
        assertThat(inquiry.getBody(), is("I need to survive out there in the galaxy!"));
        assertThat(inquiry.getSubmissionTime(),
                is(new GregorianCalendar(2013, Calendar.DECEMBER, 17, 16, 12).getTime()));
    }

    private void assertThatInquiryMatchesExpectedValuesFor301(Inquiry inquiry) {
        assertThat(inquiry.getId(), is(SUPPLIER_30_INQUIRY_ID));
        assertThat(inquiry.getOriginator().getId(), is(SUPPLIER_30.getId()));
        assertThat(inquiry.getOriginator().getCompany().getEnglishName(), is(SUPPLIER_30.getCompany().getEnglishName()));
        assertThat(inquiry.getOriginator().getPartyType(), is(SUPPLIER_30.getPartyType()));
        assertThat(inquiry.getSubject(), is("Cherry Pie Anyone"));
        assertThat(inquiry.getBody(), is("What is all this cherry stuff about anyway?"));
        assertThat(inquiry.getSubmissionTime(),
                is(new GregorianCalendar(2013, Calendar.DECEMBER, 17, 16, 13).getTime()));
    }

}
