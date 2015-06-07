package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
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

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.security.Role;
import com.google.common.collect.Lists;

public class JdbcBuyLeadMessageDaoTest extends BaseJdbcDaoTest {

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
    private JdbcBuyLeadMessageDao buyLeadMessageDao;

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

        BuyLeadMessage message = buyLeadMessageDao.load(222143L);

        assertThatMessageMatchesExpectedValuesForMessage222143(message, BUYER, false, null, false);
    }

    @Test
    public void createMessageInsertsDatabaseRecordsForMessageAndChildTables() {
        Interaction interaction = new Interaction(null, "body", BUYER, SUPPLIER_21);
        BuyLeadMessage message = new BuyLeadMessage("subject", BUYER, new BuyLead(3L, BUYER, null, null, null),
                interaction);

        BuyLeadMessage createdMessage = buyLeadMessageDao.insert(message);
        assertThat(createdMessage.getId(), notNullValue());

        Map<String, Object> messageRowMap = jdbcTemplate.queryForMap("SELECT * FROM message WHERE message_id = ?",
                createdMessage.getId());
        assertThat(messageRowMap.get("subject").toString(), is(message.getSubject()));
        assertThat((Long) messageRowMap.get("from_party_id"), is(message.getOtherParty().getId()));
        assertThat((Long) messageRowMap.get("to_party_id"), is(interaction.getReceiver().getId()));

        Map<String, Object> messageCategoryRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM message_buy_lead WHERE message_id = ?", createdMessage.getId());
        assertThat((Long) messageCategoryRowMap.get("buy_lead_id"), is(message.getBuyLead().getId()));
    }
    
    @Test
    public void assertNewlyCreatedBuyLeadMessageUpdatesWithPublicId() {
        Interaction interaction = new Interaction(null, "body", BUYER, SUPPLIER_21);
        BuyLeadMessage message = new BuyLeadMessage("subject", BUYER, new BuyLead(3L, BUYER, null, null, null),
                interaction);

        BuyLeadMessage createdMessage = buyLeadMessageDao.insert(message);

        Map<String, Object> messageRowMap = jdbcTemplate.queryForMap("SELECT * FROM message WHERE message_id = ?",
                createdMessage.getId());

        assertThat((String) messageRowMap.get("public_message_id"), is(hashids.encode(createdMessage.getId())));
    }

    private RememberMeAuthenticationToken buildAuthenticationToken(Party messenger) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.SUPPLIER, Role.BUYER);
        E2CUser user = new E2CUser("username", "password", true, 0, authorities, new Party(messenger.getId()), true);
        return new RememberMeAuthenticationToken("e2c", user, authorities);
    }

    private void assertThatMessageMatchesExpectedValuesForMessage222143(BuyLeadMessage message, Party messenger,
            boolean flagged, Calendar lastInteractionTime, boolean completelyRead) {
        assertThat(message.getId(), is(222143L));
        assertThat(message.getSubject(), is("Inquiry about Passenger Transportation Services"));
        assertThat(message.getOtherParty().getId(), is(messenger.getId()));
        assertThat(message.getOtherParty().getCompany().getEnglishName(), is(messenger.getCompany().getEnglishName()));
        assertThat(message.getOtherParty().getPartyType(), is(messenger.getPartyType()));
        assertThat(message.getBuyLead().getId(), is(3L));
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
