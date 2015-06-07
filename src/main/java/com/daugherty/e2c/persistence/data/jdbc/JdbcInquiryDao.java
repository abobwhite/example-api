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
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.MessageTag;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.InquiryReadDao;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Spring JDBC implementation of Inquiry database operations.
 */
@Repository
public class JdbcInquiryDao extends SortAndPaginationJdbcDao implements InquiryReadDao, InquiryWriteDao {

    private static final String PARTY_TYPE_COLUMN_NAME = "party_type";
    private static final String PENDING_INQUIRY_ID_COLUMN_NAME = "pending_inquiry_id";
    private static final String SUBJECT_COLUMN_NAME = "subject";
    private static final String BODY_COLUMN_NAME = "body";
    private static final String SUBMISSION_TIME_COLUMN_NAME = "submission_timestamp";
    private static final String DISAPPROVED_COLUMN_NAME = "is_disapproved";
    private static final String MESSAGE_TAG_ID_COLUMN_NAME = "message_tag_id";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert inquiryBasketInsert;
    private SimpleJdbcInsert pendingInquiryInsert;
    private SimpleJdbcInsert pendingInquiryProductInsert;
    private SimpleJdbcInsert pendingInquiryMessageTagInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        super.createSimpleJdbcInserts(dataSource);
        inquiryBasketInsert = new SimpleJdbcInsert(dataSource).withTableName("inquiry_basket").usingColumns(
                PARTY_ID_COLUMN_NAME, PRODUCT_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                LAST_MODIFIED_DATE_COLUMN_NAME);
        pendingInquiryInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("pending_inquiry")
                .usingGeneratedKeyColumns(PENDING_INQUIRY_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, SUBJECT_COLUMN_NAME, BODY_COLUMN_NAME, SUBMISSION_TIME_COLUMN_NAME,
                        DISAPPROVED_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
        pendingInquiryProductInsert = new SimpleJdbcInsert(dataSource).withTableName("pending_inquiry_product")
                .usingColumns(PENDING_INQUIRY_ID_COLUMN_NAME, PRODUCT_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
        pendingInquiryMessageTagInsert = new SimpleJdbcInsert(dataSource).withTableName("pending_inquiry_message_tag")
                .usingColumns(PENDING_INQUIRY_ID_COLUMN_NAME, MESSAGE_TAG_ID_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME,
                        LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Inquiry.ORIGINATOR_SERIAL_PROPERTY, ENGLISH_NAME_COLUMN_NAME);
        columnsByProperty.put(Inquiry.SUBJECT_SERIAL_PROPERTY, SUBJECT_COLUMN_NAME);
        columnsByProperty.put(Inquiry.BODY_SERIAL_PROPERTY, BODY_COLUMN_NAME);
        columnsByProperty.put(Inquiry.SUBMISSION_TIME_SERIAL_PROPERTY, SUBMISSION_TIME_COLUMN_NAME);
    }

    @Override
    public Inquiry loadBasket(Long partyId) {
        LOGGER.debug("Retrieving inquiry basket for party " + partyId);
        return new Inquiry(partyId, findProductIdsForParty(partyId));
    }

    private List<Long> findProductIdsForParty(Long partyId) {
        String sql = getSql("/inquiry/load-inquiry-basket.sql");
        RowMapper<Long> rowMapper = new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(PRODUCT_ID_COLUMN_NAME);
            }
        };
        return jdbcTemplate.query(sql, new MapSqlParameterSource("partyId", partyId), rowMapper);
    }

    @Override
    public Multimap<Party, Long> findSuppliersForProductsInBasket(Long partyId) {
        LOGGER.debug("Retrieving suppliers for products in inquiry basket for party " + partyId);
        String sql = getSql("/inquiry/find-suppliers-for-products.sql");
        ProductsBySupplierRowCallbackHandler rowCallbackHandler = new ProductsBySupplierRowCallbackHandler();
        jdbcTemplate.query(sql, new MapSqlParameterSource("partyId", partyId), rowCallbackHandler);
        return rowCallbackHandler.getProductsBySupplier();
    }

    @Override
    public Inquiry loadPendingInquiry(Long inquiryId) {
        LOGGER.debug("Loading pending Inquiry in the database for Id " + inquiryId);
        String sql = getSql("/inquiry/get-all.sql") + " WHERE pending_inquiry_id = :inquiryId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("inquiryId", inquiryId),
                new InquiryRowMapper());
    }

    @Override
    public Multimap<Party, Long> findSuppliersForProductsInPendingInquiry(Long id) {
        LOGGER.debug("Retrieving suppliers for products in Pending Inquiry " + id);
        String sql = getSql("/inquiry/find-suppliers-for-inquiries.sql");
        ProductsBySupplierRowCallbackHandler rowCallbackHandler = new ProductsBySupplierRowCallbackHandler();
        jdbcTemplate.query(sql, new MapSqlParameterSource("inquiryId", id), rowCallbackHandler);
        return rowCallbackHandler.getProductsBySupplier();
    }

    @Override
    public QueryCriteria createQueryCriteria(Long senderId, Boolean disapproved, String originatorCompany,
            String propertyName, Boolean sortDescending, Integer startItem, Integer count) {
        return createSqlQueryCriteria(propertyName, sortDescending, startItem, count, null)
                .appendEqualsSubClause(PARTY_ID_COLUMN_NAME, "senderId", senderId)
                .appendEqualsSubClause(DISAPPROVED_COLUMN_NAME, "disapproved", disapproved)
                .appendLikeSubClause(ENGLISH_NAME_COLUMN_NAME, "originatorCompany", originatorCompany);
    }

    @Override
    public List<Inquiry> findPendingInquiries(QueryCriteria criteria) {
        LOGGER.debug("Finding all pending Inquiries in the database matching " + criteria.toString());
        String sql = getSql("/inquiry/get-all.sql") + criteria.getCombinedQueryClauses();
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new InquiryRowMapper());
    }

    @Override
    public Multimap<Long, Long> findProductsForPendingInquiries(List<Long> inquiryIds) {
        LOGGER.debug("Retrieving Product Ids for pending Inquiries " + inquiryIds);
        ProductsByInquiryRowCallbackHandler rowCallbackHandler = new ProductsByInquiryRowCallbackHandler();
        if (!inquiryIds.isEmpty()) {
            String sql = getSql("/inquiry/find-products-for-inquiries.sql");
            jdbcTemplate.query(sql, new MapSqlParameterSource("inquiryIds", inquiryIds), rowCallbackHandler);
        }
        return rowCallbackHandler.getProductsByInquiry();
    }

    @Override
    public Multimap<Long, MessageTag> findMessageTagsForPendingInquiries(List<Long> inquiryIds) {
        LOGGER.debug("Retrieving Message Tags for pending Inquiries " + inquiryIds);
        MessageTagsByInquiryRowCallbackHandler rowCallbackHandler = new MessageTagsByInquiryRowCallbackHandler();
        if (!inquiryIds.isEmpty()) {
            String sql = getSql("/inquiry/find-message-tags-for-inquiries.sql");
            jdbcTemplate.query(sql, new MapSqlParameterSource("inquiryIds", inquiryIds), rowCallbackHandler);
        }
        return rowCallbackHandler.getMessageTagsByInquiry();
    }

    @Override
    public void insertProductForParty(Long partyId, Long productId) {
        LOGGER.debug("Adding product " + productId + " to the inquiry basket for party " + partyId);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PARTY_ID_COLUMN_NAME, partyId)
                .addValue(PRODUCT_ID_COLUMN_NAME, productId);
        inquiryBasketInsert.execute(parameterSource);
    }

    @Override
    public void deleteProductForParty(Long partyId, Long productId) {
        LOGGER.debug("Removing product " + productId + " from the inquiry basket for party " + partyId);
        String sql = getSql("/inquiry/delete-product-from-inquiry-basket.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId).addValue(
                "productId", productId);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void clearBasket(Long partyId) {
        LOGGER.info("Removing products from the inquiry basket for party " + partyId);
        String sql = getSql("/inquiry/clear-inquiry-basket.sql");
        jdbcTemplate.update(sql, new MapSqlParameterSource("partyId", partyId));
    }

    @Override
    public Inquiry insertPendingInquiry(Inquiry inquiry) {
        LOGGER.info("Creating pending Inquiry " + inquiry);
        Inquiry createdInquiry = createPendingInquiry(inquiry);
        associateProductsWithPendingInquiry(createdInquiry);
        associateMessageTagsWithPendingInquiry(createdInquiry);
        return createdInquiry;
    }

    @Override
    public int deletePendingInquiriesByPartyId(Long partyId) {
        LOGGER.info("Deleting pending inquiries for party" + partyId);

        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId);

        jdbcTemplate.update(getSql("/inquiry/deletePendingInquiryMessageTagByPartyId.sql"), parameterSource);
        jdbcTemplate.update(getSql("/inquiry/deletePendingInquiryProductByPartyId.sql"), parameterSource);
        int numberOfDeletes = jdbcTemplate
                .update(getSql("/inquiry/deletePendingInquiryByPartyId.sql"), parameterSource);

        LOGGER.info("Deleted " + numberOfDeletes + " pending inquiries for party" + partyId);

        return numberOfDeletes;
    }

    private Inquiry createPendingInquiry(Inquiry inquiry) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PARTY_ID_COLUMN_NAME, inquiry.getOriginator().getId())
                .addValue(SUBJECT_COLUMN_NAME, inquiry.getSubject()).addValue(BODY_COLUMN_NAME, inquiry.getBody())
                .addValue(SUBMISSION_TIME_COLUMN_NAME, new Date()).addValue(DISAPPROVED_COLUMN_NAME, false);
        Number key = pendingInquiryInsert.executeAndReturnKey(parameterSource);

        inquiry.setId(key.longValue());
        return inquiry;
    }

    private void associateProductsWithPendingInquiry(Inquiry createdInquiry) {
        LOGGER.info("Associating Products with pending Inquiry " + createdInquiry.getId());
        for (Long productId : createdInquiry.getProductIds()) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PENDING_INQUIRY_ID_COLUMN_NAME,
                    createdInquiry.getId()).addValue(PRODUCT_ID_COLUMN_NAME, productId);
            pendingInquiryProductInsert.execute(parameterSource);
        }
    }

    private void associateMessageTagsWithPendingInquiry(Inquiry createdInquiry) {
        LOGGER.info("Associating Message Tags with pending Inquiry " + createdInquiry.getId());
        for (MessageTag messageTag : createdInquiry.getMessageTags()) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(PENDING_INQUIRY_ID_COLUMN_NAME,
                    createdInquiry.getId()).addValue(MESSAGE_TAG_ID_COLUMN_NAME, messageTag.getId());
            pendingInquiryMessageTagInsert.execute(parameterSource);
        }
    }

    @Override
    public void deletePendingInquiry(Long id) {
        LOGGER.info("Removing pending inquiry parent and child records for ID " + id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("inquiryId", id);
        jdbcTemplate.update(getSql("/inquiry/delete-pending-inquiry-message-tag.sql"), parameterSource);
        jdbcTemplate.update(getSql("/inquiry/delete-pending-inquiry-product.sql"), parameterSource);
        jdbcTemplate.update(getSql("/inquiry/delete-pending-inquiry.sql"), parameterSource);
    }

    @Override
    public void disapprovePendingInquiry(Long id) {
        LOGGER.info("Updating disapproved flag on Pending Inquiry with ID " + id);
        String sql = getSql("/inquiry/disapprove-pending-inquiry.sql");
        jdbcTemplate.update(sql, new MapSqlParameterSource("inquiryId", id));
    }

    /**
     * Maps ResultSet rows to Inquiry domain objects.
     */
    private final class InquiryRowMapper implements RowMapper<Inquiry> {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();

        @Override
        public Inquiry mapRow(ResultSet rs, int rowNum) throws SQLException {
            Party party = partyRowMapper.mapRow(rs, rowNum);

            return new Inquiry(rs.getLong(PENDING_INQUIRY_ID_COLUMN_NAME), party, rs.getString(SUBJECT_COLUMN_NAME),
                    rs.getString(BODY_COLUMN_NAME), rs.getTimestamp(SUBMISSION_TIME_COLUMN_NAME));
        }
    }

    /**
     * Maps Product Ids to Supplier Messengers.
     */
    private class ProductsBySupplierRowCallbackHandler implements RowCallbackHandler {
        private final PartyRowMapper partyRowMapper = new PartyRowMapper();
        private final Multimap<Party, Long> productsBySupplier = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            Party supplier = partyRowMapper.mapRow(rs, "");

            productsBySupplier.put(supplier, rs.getLong(PRODUCT_ID_COLUMN_NAME));
        }

        public Multimap<Party, Long> getProductsBySupplier() {
            return productsBySupplier;
        }

    }

    /**
     * Maps Product Ids to Inquiry Ids.
     */
    public class ProductsByInquiryRowCallbackHandler implements RowCallbackHandler {
        private final Multimap<Long, Long> productsByInquiry = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            productsByInquiry.put(rs.getLong(PENDING_INQUIRY_ID_COLUMN_NAME), rs.getLong(PRODUCT_ID_COLUMN_NAME));
        }

        public Multimap<Long, Long> getProductsByInquiry() {
            return productsByInquiry;
        }

    }

    /**
     * Maps Message Tags to Inquiry Ids.
     */
    public class MessageTagsByInquiryRowCallbackHandler implements RowCallbackHandler {
        private final Multimap<Long, MessageTag> messageTagsByInquiry = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            messageTagsByInquiry.put(rs.getLong(PENDING_INQUIRY_ID_COLUMN_NAME),
                    MessageTag.findById(rs.getLong(MESSAGE_TAG_ID_COLUMN_NAME)));
        }

        public Multimap<Long, MessageTag> getMessageTagsByInquiry() {
            return messageTagsByInquiry;
        }

    }
}
