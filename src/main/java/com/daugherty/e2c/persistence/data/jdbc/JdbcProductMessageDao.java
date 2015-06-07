package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.persistence.data.ProductMessageReadDao;
import com.daugherty.e2c.persistence.data.ProductMessageWriteDao;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Spring JDBC implementation of the Product Message database operations.
 */
@Repository("productMessageDao")
public class JdbcProductMessageDao extends JdbcMessageDao implements ProductMessageReadDao, ProductMessageWriteDao {

    private static final String MESSAGE_TAG_ID_COLUMN_NAME = "message_tag_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert messageProductInsert;
    private SimpleJdbcInsert messageMessageTagInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        messageProductInsert = new SimpleJdbcInsert(dataSource).withTableName("message_product").usingColumns(
                MESSAGE_ID_COLUMN_NAME, PRODUCT_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                LAST_MODIFIED_DATE_COLUMN_NAME);
        messageMessageTagInsert = new SimpleJdbcInsert(dataSource).withTableName("message_message_tag").usingColumns(
                MESSAGE_ID_COLUMN_NAME, MESSAGE_TAG_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public ProductMessage load(Long id) {
        return load(id, SecurityUtils.getAuthenticatedUserPartyId());
    }
    
    @Override
    public ProductMessage load(Long id, Long retrieveAsPartyId) {
        LOGGER.debug("Finding Product Message in the database with Id " + id);
        String sql = getSql("/productmessage/load.sql");
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id).addValue("userPartyId", retrieveAsPartyId);
        return jdbcTemplate.queryForObject(sql, parameters, new ProductMessageRowMapper());
    }

    @Override
    public Multimap<Long, Long> findProductIdListsByMessageIds(List<Long> messageIds) {
        LOGGER.debug("Finding all Product Ids in the database matching " + messageIds);
        if (messageIds.isEmpty()) {
            return ArrayListMultimap.create();
        }

        String sql = getSql("/productmessage/find-product-ids-by-message.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("messageIds", messageIds);
        ProductMessageCallbackHandler productMessageCallbackHandler = new ProductMessageCallbackHandler();
        jdbcTemplate.query(sql, parameterSource, productMessageCallbackHandler);
        return productMessageCallbackHandler.getProductIdListsByMessageIds();
    }

    @Override
    public ProductMessage insert(ProductMessage message) {
        LOGGER.info("Creating Product Message " + message);
        insertMessage(message);
        insertProductAssociations(message.getProductIds(), message.getId());
        insertMessageTagAssociations(message.getMessageTags(), message.getId());
        return message;
    }

    private void insertProductAssociations(List<Long> productIds, Long messageId) {
        LOGGER.info("Associating Products with Message " + messageId);
        for (Long productId : productIds) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(MESSAGE_ID_COLUMN_NAME,
                    messageId).addValue(PRODUCT_ID_COLUMN_NAME, productId);
            messageProductInsert.execute(parameterSource);
        }
    }

    private void insertMessageTagAssociations(List<MessageTag> messageTags, Long messageId) {
        LOGGER.info("Associating Message Tags with Message " + messageId);
        for (MessageTag messageTag : messageTags) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(MESSAGE_ID_COLUMN_NAME,
                    messageId).addValue(MESSAGE_TAG_ID_COLUMN_NAME, messageTag.getId());
            messageMessageTagInsert.execute(parameterSource);
        }
    }

    /**
     * Maps database records to Message domain objects.
     */
    private final class ProductMessageRowMapper implements RowMapper<ProductMessage> {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();

        @Override
        public ProductMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProductMessage(rs.getLong(MESSAGE_ID_COLUMN_NAME), rs.getString(PUBLIC_MESSAGE_ID_COLUMN_NAME), rs.getString(SUBJECT_COLUMN_NAME),
                    getOtherParty(rs), rs.getBoolean("flagged"), rs.getTimestamp("last_interaction_sent_time"),
                    rs.getBoolean("all_interactions_read"));
        }

        private Party getOtherParty(ResultSet rs) throws SQLException {
            return partyRowMapper.mapRow(rs, 0);
        }
    }

    /**
     * Maps Message IDs to a list of Product IDs with which they are associated.
     */
    private static class ProductMessageCallbackHandler implements RowCallbackHandler {
        private final Multimap<Long, Long> productIdListsByMessageIds = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            productIdListsByMessageIds.put(rs.getLong(MESSAGE_ID_COLUMN_NAME), rs.getLong(PRODUCT_ID_COLUMN_NAME));
        }

        public Multimap<Long, Long> getProductIdListsByMessageIds() {
            return productIdListsByMessageIds;
        }

    }

}
