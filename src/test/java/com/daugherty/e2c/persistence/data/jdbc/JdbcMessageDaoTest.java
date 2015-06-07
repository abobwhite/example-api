package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.security.Role;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcMessageDaoTest extends BaseJdbcDaoTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company BUYER_29_COMPANY = new Company("Hue2 Buyers Inc -Waiting", null, null, null, null,
            null, null, null, null, null, null);
    private static final Company SUPPLIER_21_COMPANY = new Company("Megadodo Publications", null, null, null, null,
            null, null, null, null, null, null);
    private static final Company SUPPLIER_30_COMPANY = new Company("Cherry Spice", null, null, null, null, null, null,
            null, null, null, null);
    private static final Party BUYER = new Party(22L, "pBVQJNb4", null, BUYER_COMPANY, null, PartyType.BUYER, null,
            null);
    private static final Party BUYER_29 = new Party(29L, "Qm0XBVaZ", null, BUYER_29_COMPANY, null, PartyType.BUYER,
            null, null);
    private static final Party SUPPLIER_21 = new Party(21L, "Y40dgNWM", null, SUPPLIER_21_COMPANY, null,
            PartyType.SUPPLIER, null, null);
    private static final Party SUPPLIER_30 = new Party(30L, "690O6NL4", null, SUPPLIER_30_COMPANY, null,
            PartyType.SUPPLIER, null, null);
    @Inject
    private JdbcMessageDao messageDao;

    private Authentication originalAuthentication;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("message.sql");
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
    public void findWithSupplierReceiverIdCriterionOnlyReturnsMatchingMessages() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(SUPPLIER_21.getId(), null, null, null, false, 1,
                25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(0), BUYER, false, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 22, 16, 45, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(1), BUYER_29, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(2), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(3), BUYER, false, null, false);

    }

    @Test
    public void findWithReceiverIdAndFlaggedCriteriaReturnsMatchingMessage() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(SUPPLIER_21.getId(), true, null, null, false, 1,
                25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(1));
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(0), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
    }

    @Test
    public void findWithBuyerReceiverIdCriterionOnlyReturnsMatchingMessages() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(BUYER));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(BUYER.getId(), null, null, null, false, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(0), SUPPLIER_21, false,
                new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 43, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(1), SUPPLIER_21, false, null, true);
        assertThatMessageMatchesExpectedValuesForMessage2230242808(messages.get(2), SUPPLIER_30, false,
                new GregorianCalendar(2013, Calendar.DECEMBER, 4, 13, 32, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(3), SUPPLIER_21, false, null, true);

    }

    @Test
    public void findWithSupplierSenderIdCriterionOnlyReturnsMatchingMessages() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(null, null, SUPPLIER_21.getId(), null, false, 1,
                25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(3));
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(0), BUYER, false, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 22, 16, 43, 0), true);
    }

    @Test
    public void findWithBuyerSenderIdCriterionOnlyReturnsMatchingMessages() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(BUYER));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(null, null, BUYER.getId(), null, false, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(5));
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(0), SUPPLIER_21, false,
                new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 45, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(1), SUPPLIER_21, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage2230242808(messages.get(2), SUPPLIER_30, false,
                new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 15, 53, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(3), SUPPLIER_21, false,
                new GregorianCalendar(2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(4), SUPPLIER_21, false, null, false);
    }

    @Test
    public void findSortsBySubject() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(SUPPLIER_21.getId(), null, null,
                Message.SUBJECT_SERIAL_PROPERTY, true, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(0), BUYER_29, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(1), BUYER, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(2), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(3), BUYER, false, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 22, 16, 45, 0), false);
    }

    @Test
    public void findWithSupplierReceiverIdSortsByOtherPartyBasedOnFromName() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(SUPPLIER_21.getId(), null, null,
                Message.OTHER_PARTY_SERIAL_PROPERTY, true, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(0), BUYER_29, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(1), BUYER, false, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 22, 16, 45, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(2), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(3), BUYER, false, null, false);
    }

    @Test
    public void findWithBuyerReceiverIdSortsByOtherPartyBasedOnToName() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(BUYER));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(BUYER.getId(), null, null,
                Message.OTHER_PARTY_SERIAL_PROPERTY, false, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage2230242808(messages.get(0), SUPPLIER_30, false,
                new GregorianCalendar(2013, Calendar.DECEMBER, 4, 13, 32, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(1), SUPPLIER_21, false,
                new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 43, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(2), SUPPLIER_21, false, null, true);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(3), SUPPLIER_21, false, null, true);
    }

    @Test
    public void findSortsByMostRecentInteractionTime() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao.createQueryCriteria(SUPPLIER_21.getId(), null, null,
                Message.LAST_INTERACTION_SENT_TIME_SERIAL_PROPERTY, true, 1, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(4));
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(0), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222142(messages.get(1), BUYER, false, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 22, 16, 45, 0), false);
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(2), BUYER_29, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(3), BUYER, false, null, false);

    }

    @Test
    public void findPaginates() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        QueryCriteria queryCriteria = messageDao
                .createQueryCriteria(SUPPLIER_21.getId(), null, null, null, null, 2, 25);
        List<Message> messages = messageDao.find(queryCriteria);

        assertThat(messages, is(notNullValue()));
        assertThat(messages.size(), is(3));
        assertThatMessageMatchesExpectedValuesForMessage222144(messages.get(0), BUYER_29, false, null, false);
        assertThatMessageMatchesExpectedValuesForMessage2221911(messages.get(1), BUYER, true, new GregorianCalendar(
                2013, Calendar.NOVEMBER, 25, 9, 39, 0), true);
        assertThatMessageMatchesExpectedValuesForMessage222143(messages.get(2), BUYER, false, null, false);

    }

    @Test
    public void loadUnreadForParty() throws Exception {
        Integer unread = messageDao.loadUnreadMessagesForParty(22L);
        assertThat(unread, is(1));
    }

    @Test
    public void loadMessageSentForParty() throws Exception {
        Integer unread = messageDao.loadSentMessagesForParty(22L);
        assertThat(unread, is(4));
    }

    @Test
    public void insertMessageFlagCreatesDatabaseRecord() throws Exception {
        messageDao.insertMessageFlag(222141L, 21L);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "message_flag",
                "message_id = 222141 AND party_id = 21"), is(1));
    }

    @Test
    public void deleteMessageFlagRemovesDatabaseRecord() throws Exception {
        messageDao.deleteMessageFlag(2221911L, 21L);

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "message_flag",
                "message_id = 2221911 AND party_id = 21"), is(0));
    }

    @Test
    public void deleteMessagesByPartyIdRemovedMessages() throws Exception {
        QueryCriteria criteria = messageDao.createQueryCriteria(23L, true, 23L, null, true, 1, 250);

        List<Message> messages = messageDao.findForParty(criteria);

        int deletes = messageDao.deleteMessagesByPartyId(23L);

        assertThat(messages.size(), is(deletes));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Party messenger) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(messenger.getId()), true);
        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

    private void assertThatMessageMatchesExpectedValuesForMessage222142(Message message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(222142L));
        assertThat(message.getSubject(), is("Inquiry about Black Beans (ABOB)"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.isFlagged(), is(flagged));
        assertThat(message.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(message.getLastInteraction().getBody(), is(nullValue()));
        assertThat(message.getLastInteraction().getSender(), is(nullValue()));
        assertThat(message.getLastInteraction().getReceiver(), is(nullValue()));
        if (lastInteractionTime != null) {
            assertThat(message.getLastInteraction().getSentTime(), is(lastInteractionTime.getTime()));
        }
        assertThat(message.getLastInteraction().isRead(), is(false));
        assertThat(message.isCompletelyRead(), is(completelyRead));
    }

    private void assertThatMessageMatchesExpectedValuesForMessage2221911(Message message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(2221911L));
        assertThat(message.getSubject(), is("Inquiry about Hitch-Hikers Guide to the Galaxy"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.isFlagged(), is(flagged));
        assertThat(message.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(message.getLastInteraction().getBody(), is(nullValue()));
        assertThat(message.getLastInteraction().getSender(), is(nullValue()));
        assertThat(message.getLastInteraction().getReceiver(), is(nullValue()));
        if (lastInteractionTime != null) {
            assertThat(message.getLastInteraction().getSentTime(), is(lastInteractionTime.getTime()));
        }
        assertThat(message.getLastInteraction().isRead(), is(false));
        assertThat(message.isCompletelyRead(), is(completelyRead));
    }

    private void assertThatMessageMatchesExpectedValuesForMessage2230242808(Message message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(2230242808L));
        assertThat(message.getSubject(), is("Inquiry about Devilled Eggs"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.isFlagged(), is(flagged));
        assertThat(message.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(message.getLastInteraction().getBody(), is(nullValue()));
        assertThat(message.getLastInteraction().getSender(), is(nullValue()));
        assertThat(message.getLastInteraction().getReceiver(), is(nullValue()));
        if (lastInteractionTime != null) {
            assertThat(message.getLastInteraction().getSentTime(), is(lastInteractionTime.getTime()));
        }
        assertThat(message.getLastInteraction().isRead(), is(false));
        assertThat(message.isCompletelyRead(), is(completelyRead));
    }

    private void assertThatMessageMatchesExpectedValuesForMessage222143(Message message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(222143L));
        assertThat(message.getSubject(), is("Inquiry about Passenger Transportation Services"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.isFlagged(), is(flagged));
        assertThat(message.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(message.getLastInteraction().getBody(), is(nullValue()));
        assertThat(message.getLastInteraction().getSender(), is(nullValue()));
        assertThat(message.getLastInteraction().getReceiver(), is(nullValue()));
        if (lastInteractionTime != null) {
            assertThat(message.getLastInteraction().getSentTime(), is(lastInteractionTime.getTime()));
        }
        assertThat(message.getLastInteraction().isRead(), is(false));
        assertThat(message.isCompletelyRead(), is(completelyRead));
    }

    private void assertThatMessageMatchesExpectedValuesForMessage222144(Message message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(222144L));
        assertThat(message.getSubject(), is("Inquiry about Passenger Transportation Services"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.isFlagged(), is(flagged));
        assertThat(message.getLastInteraction().getMessage(), is(nullValue()));
        assertThat(message.getLastInteraction().getBody(), is(nullValue()));
        assertThat(message.getLastInteraction().getSender(), is(nullValue()));
        assertThat(message.getLastInteraction().getReceiver(), is(nullValue()));
        if (lastInteractionTime != null) {
            assertThat(message.getLastInteraction().getSentTime(), is(lastInteractionTime.getTime()));
        }
        assertThat(message.getLastInteraction().isRead(), is(false));
        assertThat(message.isCompletelyRead(), is(completelyRead));
    }
}
