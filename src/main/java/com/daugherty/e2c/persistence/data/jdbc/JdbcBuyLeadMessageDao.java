package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.BuyLeadMessage;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.persistence.data.BuyLeadMessageReadDao;
import com.daugherty.e2c.persistence.data.BuyLeadMessageWriteDao;

/**
 * Spring JDBC implementation of the Buy Lead Message database operations.
 */
@Repository("buyLeadMessageDao")
public class JdbcBuyLeadMessageDao extends JdbcMessageDao implements BuyLeadMessageReadDao, BuyLeadMessageWriteDao {

    private static final String BUY_LEAD_ID_COLUMN_NAME = "buy_lead_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert messageBuyLeadInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        messageBuyLeadInsert = new SimpleJdbcInsert(dataSource).withTableName("message_buy_lead").usingColumns(
                MESSAGE_ID_COLUMN_NAME, BUY_LEAD_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public BuyLeadMessage load(Long id) {
        LOGGER.debug("Finding Buy Lead Message in the database with Id " + SecurityUtils.getAuthenticatedUserPartyId());
        String sql = getSql("/buyleadmessage/load.sql");
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id).addValue("userPartyId",
                SecurityUtils.getAuthenticatedUserPartyId());
        return jdbcTemplate.queryForObject(sql, parameters, new BuyLeadMessageRowMapper());
    }

    @Override
    public BuyLeadMessage insert(BuyLeadMessage message) {
        LOGGER.info("Creating Buy Lead Message " + message);
        insertMessage(message);
        insertBuyLeadAssociation(message.getBuyLead().getId(), message.getId());
        return message;
    }

    private void insertBuyLeadAssociation(Long buyLeadId, Long messageId) {
        LOGGER.info("Associating Buy Leads with Message " + messageId);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(MESSAGE_ID_COLUMN_NAME, messageId)
                .addValue(BUY_LEAD_ID_COLUMN_NAME, buyLeadId);
        messageBuyLeadInsert.execute(parameterSource);
    }

    /**
     * Maps database records to Message domain objects.
     */
    private final class BuyLeadMessageRowMapper implements RowMapper<BuyLeadMessage> {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();

        @Override
        public BuyLeadMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BuyLeadMessage(rs.getLong(MESSAGE_ID_COLUMN_NAME), rs.getString(PUBLIC_MESSAGE_ID_COLUMN_NAME),rs.getString(SUBJECT_COLUMN_NAME),
                    getOtherParty(rs), rs.getBoolean("flagged"), rs.getTimestamp("last_interaction_sent_time"),
                    rs.getBoolean("all_interactions_read"), new BuyLeadRowMapper().mapRow(rs, rowNum, "bl_"));
        }

        private Party getOtherParty(ResultSet rs) throws SQLException {
            return partyRowMapper.mapRow(rs, 0);
        }
    }
}
