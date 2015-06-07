package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.MessageType;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.MessageReadDao;
import com.daugherty.e2c.persistence.data.MessageWriteDao;
import com.daugherty.persistence.QueryCriteria;

/**
 * Spring JDBC implementation of the Product Message database operations.
 */
@Repository("messageDao")
public class JdbcMessageDao extends SortAndPaginationJdbcDao implements MessageReadDao, MessageWriteDao {

    protected static final String SUBJECT_COLUMN_NAME = "subject";
    protected static final String FROM_PARTY_ID_COLUMN_NAME = "from_party_id";
    protected static final String TO_PARTY_ID_COLUMN_NAME = "to_party_id";
    protected static final String TYPE_COLUMN_NAME = "message_type";
    protected static final String MESSAGE_ID_COLUMN_NAME = "message_id";
    protected static final String PUBLIC_MESSAGE_ID_COLUMN_NAME = "public_message_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert messageInsert;
    private SimpleJdbcInsert messageFlagInsert;
    
    @Inject
    private Hashids hashids;

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Message.SUBJECT_SERIAL_PROPERTY, SUBJECT_COLUMN_NAME);
        columnsByProperty.put(Message.OTHER_PARTY_SERIAL_PROPERTY, "company_name_english");
        columnsByProperty.put(Message.LAST_INTERACTION_SENT_TIME_SERIAL_PROPERTY, "last_interaction_sent_time");
    }

    @Override
    public QueryCriteria createQueryCriteria(Long receiverId, Boolean flagged, Long senderId, String propertyName,
            Boolean sortDescending, Integer startItem, Integer count) {
        SqlQueryCriteria criteria = createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null);

        if ((receiverId != null && receiverId.equals(senderId))) {
            criteria.append("(m.to_party_id = :receiverId OR m.from_party_id = :senderId) ");
            criteria.getParameterMap().put("receiverId", receiverId);
            criteria.getParameterMap().put("senderId", senderId);
            criteria.getParameterMap().put("userPartyId", senderId);
        } else {
            criteria.appendEqualsSubClause("li.to_party_id", "receiverId", receiverId).appendEqualsSubClause(
                    "li.from_party_id", "senderId", senderId);
            criteria.getParameterMap().put("userPartyId", SecurityUtils.getAuthenticatedUserPartyId());
        }

        if (Boolean.TRUE.equals(flagged)) {
            criteria.appendSubQuerySubClause("m.", MESSAGE_ID_COLUMN_NAME, "message_flag", PARTY_ID_COLUMN_NAME,
                    "flagPartyId", SecurityUtils.getAuthenticatedUserPartyId());
        }

        return criteria;
    }

    @Override
    public Message load(Long id) {
        return load(id, SecurityUtils.getAuthenticatedUserPartyId());
    }

    @Override
    public Message load(Long id, Long retrieveAsPartyId) {
        LOGGER.debug("Finding Product Message in the database with Id " + id);
        String sql = getSql("/message/load.sql");
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id).addValue("userPartyId",
                retrieveAsPartyId);
        return jdbcTemplate.queryForObject(sql, parameters, new MessageRowMapper());
    }

    @Override
    public List<Message> find(QueryCriteria criteria) {
        LOGGER.debug("Finding all Messages in the database matching " + criteria.toString());
        String sql = getSql("/message/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new MessageRowMapper());
    }

    @Override
    public List<Message> findForParty(QueryCriteria criteria) {
        LOGGER.debug("Finding all Messages in the database matching " + criteria.toString());
        String sql = getSql("/message/get-all-for-party.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new MessageRowMapper());
    }

    @Override
    public Integer loadUnreadMessagesForParty(Long id) {
        LOGGER.debug("Loading message summary for party with ID " + id);
        String sql = getSql("/message/load-unread-by-party.sql");
        return jdbcTemplate.queryForInt(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Integer loadSentMessagesForParty(Long id) {
        LOGGER.debug("Loading messages sent for party with ID " + id);
        String sql = getSql("/message/load-sent-by-party.sql");
        return jdbcTemplate.queryForInt(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        messageInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("message")
                .usingGeneratedKeyColumns(MESSAGE_ID_COLUMN_NAME)
                .usingColumns(SUBJECT_COLUMN_NAME, FROM_PARTY_ID_COLUMN_NAME, TO_PARTY_ID_COLUMN_NAME,
                        TYPE_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);

        messageFlagInsert = new SimpleJdbcInsert(dataSource).withTableName("message_flag").usingColumns(
                MESSAGE_ID_COLUMN_NAME, PARTY_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    protected void insertMessage(Message message) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(SUBJECT_COLUMN_NAME, message.getSubject())
                .addValue(FROM_PARTY_ID_COLUMN_NAME, message.getLastInteraction().getSender().getId())
                .addValue(TO_PARTY_ID_COLUMN_NAME, message.getLastInteraction().getReceiver().getId())
                .addValue(TYPE_COLUMN_NAME, message.getMessageType().getType());
        Number key = messageInsert.executeAndReturnKey(parameterSource);

        message.setId(key.longValue());
        message.setPublicId(hashids.encode(message.getId()));
        updateMessage(message);
    }
    
    private Message updateMessage(Message message) {
        String sql = getSql("message/update.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PUBLIC_MESSAGE_ID_COLUMN_NAME, message.getPublicId())
                .addValue(MESSAGE_ID_COLUMN_NAME, message.getId());

        jdbcTemplate.update(sql, parameterSource);

        return message;
    }

    @Override
    public void insertMessageFlag(Long messageId, Long partyId) {
        LOGGER.info("Message " + messageId + " has been flagged by party " + partyId);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(MESSAGE_ID_COLUMN_NAME, messageId)
                .addValue(PARTY_ID_COLUMN_NAME, partyId);
        messageFlagInsert.execute(parameterSource);
    }

    @Override
    public void deleteMessageFlag(Long messageId, Long partyId) {
        LOGGER.info("Message " + messageId + " has been unflagged by party " + partyId);
        String sql = getSql("/message/unflag.sql");
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue("messageId", messageId).addValue(
                "partyId", partyId);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public int deleteMessagesByPartyId(Long partyId) {
        LOGGER.info("Deleting messages for party" + partyId);

        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        jdbcTemplate.update(getSql("/buyleadmessage/deleteByPartyId.sql"), parameterSource);
        jdbcTemplate.update(getSql("/productmessage/deleteByPartyId.sql"), parameterSource);
        jdbcTemplate.update(getSql("/message/deleteMessageFlagByPartyId.sql"), parameterSource);
        jdbcTemplate.update(getSql("/message/deleteMessageTagByPartyId.sql"), parameterSource);
        int numberOfMessageDeletes = jdbcTemplate.update(getSql("/message/deleteByPartyId.sql"), parameterSource);

        LOGGER.info("Deleted " + numberOfMessageDeletes + " messages for party" + partyId);

        return numberOfMessageDeletes;
    }

    /**
     * Maps database records to Message domain objects.
     */
    private final class MessageRowMapper implements RowMapper<Message> {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(rs.getLong(MESSAGE_ID_COLUMN_NAME), rs.getString(PUBLIC_MESSAGE_ID_COLUMN_NAME), rs.getString(SUBJECT_COLUMN_NAME),
                    getOtherParty(rs), rs.getBoolean("flagged"), rs.getTimestamp("last_interaction_sent_time"),
                    rs.getBoolean("all_interactions_read"), MessageType.findByType(rs.getString(TYPE_COLUMN_NAME)));
        }

        private Party getOtherParty(ResultSet rs) throws SQLException {
            return partyRowMapper.mapRow(rs, 0);
        }
    }
}
