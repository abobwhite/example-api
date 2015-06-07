package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class JdbcProductMessageDaoTest extends BaseJdbcDaoTest {

    private static final Company BUYER_COMPANY = new Company("East India Tea Company", null, null, null, null, null,
            null, null, null, null, null);
    private static final Company SUPPLIER_21_COMPANY = new Company("Megadodo Publications", null, null, null, null,
            null, null, null, null, null, null);
    private static final Party BUYER = new Party(22L, "pBVQJNb4", null, BUYER_COMPANY, null, PartyType.BUYER, null, null);
    private static final Party SUPPLIER_21 = new Party(21L, "Y40dgNWM", null, SUPPLIER_21_COMPANY, null, PartyType.SUPPLIER, null,
            null);
    @Inject
    private Hashids hashids;
    @Inject
    private JdbcProductMessageDao productMessageDao;

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
    public void loadReturnsExistingMessage() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(buildAuthenticationToken(SUPPLIER_21));

        ProductMessage message = productMessageDao.load(222142L);

        assertThatMessageMatchesExpectedValuesForMessage222142(message, BUYER, false, null, false);
    }

    @Test
    public void findProductIdsByMessageIdsThatExistBuildsMultimap() throws Exception {
        Multimap<Long, Long> productIdListsByMessageIds = productMessageDao.findProductIdListsByMessageIds(Lists
                .newArrayList(222142L, 2230242808L));

        assertThat(productIdListsByMessageIds, is(notNullValue()));

        assertThat(productIdListsByMessageIds.get(222142L), is(notNullValue()));
        assertThat(productIdListsByMessageIds.get(222142L).size(), is(1));
        assertThat(productIdListsByMessageIds.get(222142L).iterator().next(), is(41L));

        assertThat(productIdListsByMessageIds.get(2230242808L), is(notNullValue()));
        assertThat(productIdListsByMessageIds.get(2230242808L).size(), is(2));
        Iterator<Long> iterator22666242808 = productIdListsByMessageIds.get(2230242808L).iterator();
        assertThat(iterator22666242808.next(), is(242L));
        assertThat(iterator22666242808.next(), is(808L));
    }

    @Test
    public void findProductIdsByMessageIdsThatDoNotExistBuildsEmptyMultimap() throws Exception {
        Multimap<Long, Long> productIdListsByMessageIds = productMessageDao.findProductIdListsByMessageIds(Lists
                .<Long> newArrayList());

        assertThat(productIdListsByMessageIds, is(notNullValue()));
        assertThat(productIdListsByMessageIds.isEmpty(), is(true));
    }

    @Test
    public void createMessageInsertsDatabaseRecordsForMessageAndChildTables() {
        Interaction interaction = new Interaction(null, "body", BUYER, SUPPLIER_21);
        ProductMessage message = new ProductMessage("subject", BUYER, 242L, interaction);
        message.addMessageTags(Lists.newArrayList(MessageTag.MINIMUM_ORDER_QUANTITY));

        ProductMessage createdMessage = productMessageDao.insert(message);
        assertThat(createdMessage.getId(), notNullValue());

        Map<String, Object> messageRowMap = jdbcTemplate.queryForMap("SELECT * FROM message WHERE message_id = ?",
                createdMessage.getId());
        assertThat(messageRowMap.get("subject").toString(), is(message.getSubject()));
        assertThat((Long) messageRowMap.get("from_party_id"), is(message.getOtherParty().getId()));
        assertThat((Long) messageRowMap.get("to_party_id"), is(interaction.getReceiver().getId()));

        Map<String, Object> messageProductRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM message_product WHERE message_id = ?", createdMessage.getId());
        assertThat((Long) messageProductRowMap.get("product_id"), is(message.getProductIds().get(0)));

        Map<String, Object> messageTagRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM message_message_tag WHERE message_id = ?", createdMessage.getId());
        assertThat((Long) messageTagRowMap.get("message_tag_id"), is(message.getMessageTags().get(0).getId()));
    }
    
    @Test
    public void assertNewlyCreatedProductMessageUpdatesWithPublicId() {
        Interaction interaction = new Interaction(null, "body", BUYER, SUPPLIER_21);
        ProductMessage message = new ProductMessage("subject", BUYER, 242L, interaction);
        message.addMessageTags(Lists.newArrayList(MessageTag.MINIMUM_ORDER_QUANTITY));

        ProductMessage createdMessage = productMessageDao.insert(message);

        Map<String, Object> messageRowMap = jdbcTemplate.queryForMap("SELECT * FROM message WHERE message_id = ?",
                createdMessage.getId());

        assertThat((String) messageRowMap.get("public_message_id"), is(hashids.encode(createdMessage.getId())));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Party messenger) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(messenger.getId()), true);
        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

    private void assertThatMessageMatchesExpectedValuesForMessage222142(ProductMessage message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(222142L));
        assertThat(message.getSubject(), is("Inquiry about Black Beans (ABOB)"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.getProductIds().toString(), is(Lists.newArrayList().toString()));
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
