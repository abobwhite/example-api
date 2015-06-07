package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.Company;
import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcInteractionDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcInteractionDao dao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void findWithMessageIdCriterionOnlyReturnsMatchingInteractions() throws Exception {
        QueryCriteria queryCriteria = dao.createQueryCriteria(222142L, null, false, 1, 25);
        List<Interaction> interactions = dao.find(queryCriteria);

        assertThat(interactions, is(notNullValue()));
        assertThat(interactions.size(), is(3));
        assertThatInteractionMatchesExpectedValuesForInteraction2221411(interactions.get(0));
        assertThatInteractionMatchesExpectedValuesForInteraction2221412(interactions.get(1));
        assertThatInteractionMatchesExpectedValuesForInteraction2221413(interactions.get(2));
    }

    @Test
    public void loadReturnsMatchingInteraction() throws Exception {
        Interaction interaction = dao.load(2221412L);

        assertThatInteractionMatchesExpectedValuesForInteraction2221412(interaction);
    }

    @Test
    public void createMessageInsertsDatabaseRecordIntoTable() {
        Company buyerCompany = new Company("buyer", null, null, null, null, null, null, null, null, null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, buyerCompany, null, PartyType.BUYER, null, null);

        Company supplierCompany = new Company("supplier", null, null, null, null, null, null, null, null, null, null);
        Party supplier = new Party(666L, "pBVQwo0b", null, supplierCompany, null, PartyType.SUPPLIER, null, null);

        Interaction interaction = new Interaction(new Message(42L), "body", buyer, supplier);

        Interaction createdInteraction = dao.insert(interaction);
        assertThat(createdInteraction.getId(), notNullValue());

        Map<String, Object> rowMap = jdbcTemplate.queryForMap("SELECT * FROM interaction WHERE interaction_id = ?",
                createdInteraction.getId());
        assertThat((Long) rowMap.get("message_id"), is(interaction.getMessage().getId()));
        assertThat((Long) rowMap.get("from_party_id"), is(interaction.getSender().getId()));
        assertThat((Long) rowMap.get("to_party_id"), is(interaction.getReceiver().getId()));
        assertThat(rowMap.get("body").toString(), is(interaction.getBody()));
        assertThat((Date) rowMap.get("sent_timestamp"), E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
        assertThat((Boolean) rowMap.get("read_by_receiver"), is(interaction.isRead()));
    }

    @Test
    public void updateReadIndicatorUpdatesThatColumnOnlyOnExistingDatabaseRecord() {
        Company buyerCompany = new Company("irrelevant changed sender", null, null, null, null, null, null, null, null,
                null, null);
        Party buyer = new Party(586L, "jKNzKB04", null, buyerCompany, null, PartyType.BUYER, null, null);

        Company supplierCompany = new Company("irrelevant changed receiver", null, null, null, null, null, null, null,
                null, null, null);
        Party supplier = new Party(666L, "pBVQwo0b", null, supplierCompany, null, PartyType.BUYER, null, null);

        Interaction interaction = new Interaction(2221412L, "irrelevant changed value", buyer, supplier, new Date(),
                true);

        dao.updateReadIndicator(interaction);

        Map<String, Object> rowMap = jdbcTemplate
                .queryForMap("SELECT * FROM interaction WHERE interaction_id = 2221412");
        assertThat((Long) rowMap.get("message_id"), is(222142L));
        assertThat((Long) rowMap.get("from_party_id"), is(21L));
        assertThat((Long) rowMap.get("to_party_id"), is(22L));
        assertThat(rowMap.get("body").toString(), is("There should be some in the fridge on the Heart of Gold"));
        assertThat((Date) rowMap.get("sent_timestamp"),
                is(new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 43).getTime()));
        assertThat((Boolean) rowMap.get("read_by_receiver"), is(true));
        assertThat((Date) rowMap.get("last_modified_date"), E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }

    @Test
    public void deleteRemovesIteractions() throws Exception {

        List<Map<String, Object>> existingInteractions = jdbcTemplate
                .queryForList("SELECT * FROM interaction WHERE (to_party_id = 21 OR from_party_id = 21);");

        assertThat(existingInteractions.size(), is(dao.deleteInteractionsByPartyId(21L)));
    }

    private void assertThatInteractionMatchesExpectedValuesForInteraction2221411(Interaction interaction) {
        assertThat(interaction.getId(), is(2221411L));
        assertThat(
                interaction.getBody(),
                is("Do you know where I can buy black beans in this galaxy? I looked at http://blackbeans.com and cannot find any."));
        assertThat(interaction.getSender().getId(), is(22L));
        assertThat(interaction.getSender().getCompany().getEnglishName(), is("East India Tea Company"));
        assertThat(interaction.getSender().getPartyType(), is(PartyType.BUYER));
        assertThat(interaction.getReceiver().getId(), is(21L));
        assertThat(interaction.getReceiver().getCompany().getEnglishName(), is("Megadodo Publications"));
        assertThat(interaction.getReceiver().getPartyType(), is(PartyType.SUPPLIER));
        assertThat(interaction.getSentTime(), is(new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 15, 38).getTime()));
        assertThat(interaction.isRead(), is(true));
    }

    private void assertThatInteractionMatchesExpectedValuesForInteraction2221412(Interaction interaction) {
        assertThat(interaction.getId(), is(2221412L));
        assertThat(interaction.getBody(), is("There should be some in the fridge on the Heart of Gold"));
        assertThat(interaction.getSender().getId(), is(21L));
        assertThat(interaction.getSender().getCompany().getEnglishName(), is("Megadodo Publications"));
        assertThat(interaction.getSender().getPartyType(), is(PartyType.SUPPLIER));
        assertThat(interaction.getReceiver().getId(), is(22L));
        assertThat(interaction.getReceiver().getCompany().getEnglishName(), is("East India Tea Company"));
        assertThat(interaction.getReceiver().getPartyType(), is(PartyType.BUYER));
        assertThat(interaction.getSentTime(), is(new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 43).getTime()));
        assertThat(interaction.isRead(), is(true));
    }

    private void assertThatInteractionMatchesExpectedValuesForInteraction2221413(Interaction interaction) {
        assertThat(interaction.getId(), is(2221413L));
        assertThat(interaction.getBody(), is("I think Trillian ate them all with her last Pan-Galactic Gargle Blaster"));
        assertThat(interaction.getSender().getId(), is(22L));
        assertThat(interaction.getSender().getCompany().getEnglishName(), is("East India Tea Company"));
        assertThat(interaction.getSender().getPartyType(), is(PartyType.BUYER));
        assertThat(interaction.getReceiver().getId(), is(21L));
        assertThat(interaction.getReceiver().getCompany().getEnglishName(), is("Megadodo Publications"));
        assertThat(interaction.getReceiver().getPartyType(), is(PartyType.SUPPLIER));
        assertThat(interaction.getSentTime(), is(new GregorianCalendar(2013, Calendar.NOVEMBER, 22, 16, 45).getTime()));
        assertThat(interaction.isRead(), is(false));
    }

}
