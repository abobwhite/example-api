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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.BuyLeadReadDao;
import com.daugherty.e2c.persistence.data.BuyLeadWriteDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

@Repository("buyLeadDao")
public class JdbcBuyLeadDao extends SortAndPaginationJdbcDao implements BuyLeadReadDao, BuyLeadWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final String BUY_LEAD_ID_COLUMN_NAME = "buy_lead_id";
    public static final String PARTY_ID_COLUMN_NAME = "party_id";
    public static final String PARTY_EMAIL_COLUMN_NAME = "email_address";
    public static final String PARTY_PROVINCE_COLUMN_NAME = "province";
    public static final String PRODUCT_CATEGORY_ID_COLUMN_NAME = "product_category_id";
    public static final String EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    public static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";

    private SimpleJdbcInsert buyLeadInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        buyLeadInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("buy_lead")
                .usingGeneratedKeyColumns(BUY_LEAD_ID_COLUMN_NAME)
                .usingColumns(PARTY_ID_COLUMN_NAME, PRODUCT_CATEGORY_ID_COLUMN_NAME, EFFECTIVE_DATE_COLUMN_NAME,
                        EXPIRATION_DATE_COLUMN_NAME, LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public BuyLead insert(BuyLead buyLead) {
        LOGGER.info("Creating buy lead " + buyLead);
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(PARTY_ID_COLUMN_NAME, buyLead.getRequester().getId())
                .addValue(PRODUCT_CATEGORY_ID_COLUMN_NAME, buyLead.getProductCategory().getId())
                .addValue(EFFECTIVE_DATE_COLUMN_NAME, buyLead.getEffectiveDate())
                .addValue(EXPIRATION_DATE_COLUMN_NAME, buyLead.getExpirationDate());

        Number key = buyLeadInsert.executeAndReturnKey(parameterSource);
        buyLead.setId(key.longValue());

        return buyLead;
    }

    @Override
    public QueryCriteria createBuyLeadQueryCriteria(String emailAddress, List<Long> categoryIds, String province,
            Date effectiveSince, Boolean includeExpired, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count) {
        SqlQueryCriteria criteria = createSqlQueryCriteria(propertyName, sortDescending, startItem, count,
                Locale.ENGLISH).appendLikeSubClause(PARTY_EMAIL_COLUMN_NAME, "email", emailAddress)
                .appendEqualsSubClause(PARTY_PROVINCE_COLUMN_NAME, "province", province)
                .appendGreaterThanOrEqualSubClause(EFFECTIVE_DATE_COLUMN_NAME, "effectiveDate", effectiveSince);

        if (!CollectionUtils.isEmpty(categoryIds)) {
            criteria.append("product_category_id IN (" + Joiner.on(",").join(categoryIds) + ")");
        }

        if (!Boolean.TRUE.equals(includeExpired)) {
            criteria.append("current_date >= effective_date AND current_date <= expiration_date");
        }

        return criteria;
    }

    @Override
    public List<BuyLead> findBuyLeads(QueryCriteria criteria) {
        LOGGER.debug("Finding all BuyLeads in the database matching " + criteria);
        String sql = getSql("/buyLead/get-all.sql") + criteria.getCombinedQueryClauses();
        LOGGER.debug("Finding all Buy Leads in the database matching SQL " + sql);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new BuyLeadRowMapper());
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Party.EMAIL_SERIAL_PROPERTY, PARTY_EMAIL_COLUMN_NAME);
        columnsByProperty.put(Party.PROVINCE_SERIAL_PROPERTY, PARTY_EMAIL_COLUMN_NAME);
        columnsByProperty.put(BuyLead.EFFECTIVE_DATE_SERIAL_PROPERTY, EFFECTIVE_DATE_COLUMN_NAME);
        columnsByProperty.put(BuyLead.LAST_MODIFIED_SERIAL_PROPERTY, "buy_lead_" + LAST_MODIFIED_DATE_COLUMN_NAME);
        columnsByProperty.put(BuyLead.EXPIRATION_DATE_SERIAL_PROPERTY, EXPIRATION_DATE_COLUMN_NAME);

    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, true);
    }

    @Override
    public BuyLead findByPartyAndCategory(Long partyId, Long productCategoryId) {
        LOGGER.info("Finding Buy Lead in the database matching party " + partyId + " productCategory "
                + productCategoryId);
        String sql = getSql("/buyLead/get-all.sql")
                + " WHERE party_id = :partyId and product_category_id = :productCategoryId";
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("partyId", partyId).addValue(
                "productCategoryId", productCategoryId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new BuyLeadRowMapper());
    }

    @Override
    public ArrayListMultimap<Long, Long> findSuppliersThatRespondedToBuyLeads(List<Long> buyLeadIds) {
        LOGGER.debug("Getting all Suppliers from the database for that have responded to buy leads" + buyLeadIds);
        String sql = getSql("/buyLead/findByBuyLeadIds.sql");
        BuyLeadIdSupplierIdCallbackHandler handler = new BuyLeadIdSupplierIdCallbackHandler();

        for (List<Long> partitionedIds : Lists.partition(buyLeadIds, 1000)) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource("buyLeadIds", partitionedIds);
            jdbcTemplate.query(sql, paramSource, handler);
        }
        return handler.getSupplierResponseForBuyLead();
    }

    /**
     * Maps child categories to parent categories based on the association table.
     */
    private final class BuyLeadIdSupplierIdCallbackHandler implements RowCallbackHandler {

        private final ArrayListMultimap<Long, Long> supplierResponseForBuyLead = ArrayListMultimap.create();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            supplierResponseForBuyLead.put(rs.getLong(BUY_LEAD_ID_COLUMN_NAME), rs.getLong(PARTY_ID_COLUMN_NAME));
        }

        public ArrayListMultimap<Long, Long> getSupplierResponseForBuyLead() {
            return supplierResponseForBuyLead;
        }
    }
}
