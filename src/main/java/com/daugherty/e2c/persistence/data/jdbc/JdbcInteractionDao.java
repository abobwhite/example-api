package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Interaction;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.InteractionReadDao;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of Interaction database operations.
 */
@Repository
public class JdbcInteractionDao extends SortAndPaginationJdbcDao implements InteractionReadDao, InteractionWriteDao {

    private static final String INTERACTION_ID_COLUMN_NAME = "interaction_id";
    private static final String BODY_COLUMN_NAME = "body";
    private static final String FROM_PARTY_ID_COLUMN_NAME = "from_party_id";
    private static final String TO_PARTY_ID_COLUMN_NAME = "to_party_id";
    private static final String SENT_TIMESTAMP_COLUMN_NAME = "sent_timestamp";
    private static final String READ_COLUMN_NAME = "read_by_receiver";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert interactionInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        interactionInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("interaction")
                .usingGeneratedKeyColumns(INTERACTION_ID_COLUMN_NAME)
                .usingColumns(MESSAGE_ID_COLUMN_NAME, BODY_COLUMN_NAME, FROM_PARTY_ID_COLUMN_NAME,
                        TO_PARTY_ID_COLUMN_NAME, SENT_TIMESTAMP_COLUMN_NAME, READ_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    public QueryCriteria createQueryCriteria(Long messageId, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count) {
        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null).appendEqualsSubClause(
                MESSAGE_ID_COLUMN_NAME, "messageId", messageId);
    }

    @Override
    public List<Interaction> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Interactions in the database matching " + criteria.toString());
        String sql = getSql("/interaction/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new InteractionRowMapper());
    }

    @Override
    public Interaction load(Long id) {
        LOGGER.debug("Looking up Interaction in the database for Id " + id);
        String sql = getSql("/interaction/get-all.sql") + " WHERE i.interaction_id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, new InteractionRowMapper());
    }

    @Override
    public Interaction insert(Interaction interaction) {
        LOGGER.info("Creating Interaction " + interaction);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(MESSAGE_ID_COLUMN_NAME, interaction.getMessage().getId())
                .addValue(BODY_COLUMN_NAME, interaction.getBody())
                .addValue(FROM_PARTY_ID_COLUMN_NAME, interaction.getSender().getId())
                .addValue(TO_PARTY_ID_COLUMN_NAME, interaction.getReceiver().getId())
                .addValue(SENT_TIMESTAMP_COLUMN_NAME, new Date()).addValue(READ_COLUMN_NAME, false);
        Number key = interactionInsert.executeAndReturnKey(parameterSource);

        interaction.setId(key.longValue());
        return interaction;
    }

    @Override
    public void updateReadIndicator(Interaction interaction) {
        LOGGER.info("The receiver has changed read status of " + interaction);
        String sql = getSql("/interaction/update-read-indicator.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("id", interaction.getId())
                .addValue("read", interaction.isRead());
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public int deleteInteractionsByPartyId(Long partyId) {
        LOGGER.info("Deleting interatcions for party" + partyId);
        String sql = getSql("/interaction/deleteByPartyId.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId);
        int numberOfDeletes = jdbcTemplate.update(sql, parameterSource);
        LOGGER.info("Deleted " + numberOfDeletes + " interatcions for party" + partyId);

        return numberOfDeletes;
    }

    /**
     * Maps ResultSet rows to Interaction domain objects.
     */
    private final class InteractionRowMapper implements RowMapper<Interaction> {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();

        @Override
        public Interaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Party sender = partyRowMapper.mapRow(rs, "sender_");
            Party receiver = partyRowMapper.mapRow(rs, "receiver_");

            return new Interaction(rs.getLong(INTERACTION_ID_COLUMN_NAME), rs.getString(BODY_COLUMN_NAME), sender,
                    receiver, rs.getTimestamp(SENT_TIMESTAMP_COLUMN_NAME), rs.getBoolean(READ_COLUMN_NAME));
        }
    }

}
