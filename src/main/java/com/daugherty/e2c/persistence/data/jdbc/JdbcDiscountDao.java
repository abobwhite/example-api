package com.daugherty.e2c.persistence.data.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountReadDao;
import com.daugherty.e2c.persistence.data.DiscountWriteDao;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of the Discount database operations.
 */
@Repository
public class JdbcDiscountDao extends SortAndPaginationJdbcDao implements DiscountReadDao, DiscountWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final String DISCOUNT_ID_COLUMN_NAME = "discounts_id";
    public static final String DISCOUNT_CODE_COLUMN_NAME = "discount_code";
    public static final String DISCOUNT_AMOUNT_TYPE_COLUMN_NAME = "discount_amount_type";
    public static final String DISCOUNT_AMOUNT_COLUMN_NAME = "amount";
    public static final String DISCOUNT_DESCRIPTION_COLUMN_NAME = "description";
    public static final String DISCOUNT_MEMBERSHIP_LEVEL_COLUMN_NAME = "tier";
    public static final String SUBSCRIPTION_TYPE_COLUMN_NAME = "subscription_type_name";
    public static final String DISCOUNT_ONGOING_COLUMN_NAME = "ongoing";
    public static final String DISCOUNT_SPECIAL_COLUMN_NAME = "special";
    public static final String DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    public static final String DISCOUNT_EXPIRATION_DATE_COLUMN_NAME = "expiration_date";

    private SimpleJdbcInsert discountInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        discountInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("discounts")
                .usingGeneratedKeyColumns(DISCOUNT_ID_COLUMN_NAME)
                .usingColumns(DISCOUNT_CODE_COLUMN_NAME, DISCOUNT_AMOUNT_TYPE_COLUMN_NAME, DISCOUNT_AMOUNT_COLUMN_NAME,
                        DISCOUNT_DESCRIPTION_COLUMN_NAME, DISCOUNT_ONGOING_COLUMN_NAME, DISCOUNT_SPECIAL_COLUMN_NAME,
                        DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME, DISCOUNT_EXPIRATION_DATE_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Discount insert(Discount discount) {
        LOGGER.info("Creating discount " + discount);
        SqlParameterSource parameterSource = createDiscountSqlParameterSource(discount);
        Number key = discountInsert.executeAndReturnKey(parameterSource);
        discount.setId(key.longValue());

        return discount;
    }

    @Override
    public Discount update(Discount discount) {
        LOGGER.info("Update discount " + discount);
        String sql = getSql("discount/update.sql");
        SqlParameterSource parameterSource = createDiscountSqlParameterSource(discount);
        jdbcTemplate.update(sql, parameterSource);

        return discount;
    }

    @Override
    protected SqlQueryCriteria createSqlQueryCriteria(Locale locale) {
        return new SqlQueryCriteria(locale, false);
    }

    @Override
    protected void mapDomainObjectPropertiesToColumnNames(Map<String, String> columnsByProperty,
            Map<String, String> translatedColumnsByProperty) {
        columnsByProperty.put(Discount.CODE_SERIAL_PROPERTY, DISCOUNT_CODE_COLUMN_NAME);
        columnsByProperty.put(Discount.MEMBERSHIP_LEVEL_SERIAL_PROPERTY, DISCOUNT_MEMBERSHIP_LEVEL_COLUMN_NAME);
        columnsByProperty.put(Discount.EFFECTIVE_SERIAL_PROPERTY, DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME);
        columnsByProperty.put(Discount.EXPIRATION_SERIAL_PROPERTY, DISCOUNT_EXPIRATION_DATE_COLUMN_NAME);
    }

    @Override
    public QueryCriteria createDiscountQueryCriteria(String couponCode, String couponType, Integer membershipLevel,
            String subscriptionType, Boolean includeExpired, String propertyName, Boolean sortDescending,
            Integer startItem, Integer count, Locale locale) {

        SqlQueryCriteria criteria = createSqlQueryCriteria(propertyName, sortDescending, startItem, count, locale)
                .appendLikeSubClause(DISCOUNT_CODE_COLUMN_NAME, "code", couponCode)
                .appendEqualsSubClause(DISCOUNT_AMOUNT_TYPE_COLUMN_NAME, "type", couponType)
                .appendEqualsSubClause(DISCOUNT_MEMBERSHIP_LEVEL_COLUMN_NAME, "membershipLevel", membershipLevel)
                .appendEqualsSubClause(SUBSCRIPTION_TYPE_COLUMN_NAME, "subscriptionType", subscriptionType);

        if (!Boolean.TRUE.equals(includeExpired)) {
            criteria.append("current_date >= d.effective_date AND current_date <= d.expiration_date");
        }

        return criteria;

    }

    @Override
    public List<Discount> findDiscounts(QueryCriteria criteria) {
        LOGGER.info("Finding all Discounts in the database matching " + criteria);
        String sql = getSql("/discount/get-all.sql") + criteria.getCombinedQueryClauses();
        LOGGER.info("Finding all Discounts in the database matching SQL " + sql);
        return jdbcTemplate.query(sql, criteria.getParameterMap(), new DiscountResultSetExtractor());
    }

    @Override
    public List<Discount> findAllDiscounts(QueryCriteria sortingAndPaginationCriteria) {
        LOGGER.info("Getting all Discounts from the database");
        String sql = getSql("/discount/get-all.sql") + sortingAndPaginationCriteria.getCombinedQueryClauses();
        LOGGER.info("Finding all Discounts in the database matching SQL " + sql);
        return jdbcTemplate
                .query(sql, sortingAndPaginationCriteria.getParameterMap(), new DiscountResultSetExtractor());
    }

    @Override
    public BigDecimal findRenewalDiscountByMonthsRemaining(Integer membershipTier, Integer monthsRemaining) {
        LOGGER.info("Looking up renewal discount for membership tier" + membershipTier + " with months remaining "
                + monthsRemaining);
        String sql = getSql("/discount/findRenewalDiscountByTierAndMonth.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("tier", membershipTier).addValue(
                "monthsRemaining", monthsRemaining);

        return jdbcTemplate.queryForObject(sql, parameterSource, BigDecimal.class);
    }

    @Override
    public Discount loadById(Long discountId) {
        LOGGER.info("Getting Discount from the database " + discountId);
        String sql = getSql("/discount/get-all.sql") + " WHERE d.discounts_id = :discountId ";
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("discountId", discountId);
        return jdbcTemplate.query(sql, parameterSource, new DiscountResultSetExtractor()).get(0);
    }

    private final class DiscountResultSetExtractor implements ResultSetExtractor<List<Discount>> {
        private DiscountRowMapper discountRowMapper = new DiscountRowMapper();

        @Override
        public List<Discount> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Discount> discounts = new LinkedHashMap<Long, Discount>();
            while (rs.next()) {
                Long discountId = rs.getLong(DISCOUNT_ID_COLUMN_NAME);
                Discount discount = discounts.get(discountId);
                if (discount == null) {
                    discount = discountRowMapper.mapRow(rs, 0);
                    discounts.put(discountId, discount);
                }
                Integer membershipLevel = rs.getInt(DISCOUNT_MEMBERSHIP_LEVEL_COLUMN_NAME);
                discount.getMembershipLevels().add(membershipLevel);

                SubscriptionType subscriptionType = SubscriptionType.findByName(rs
                        .getString(SUBSCRIPTION_TYPE_COLUMN_NAME));
                discount.getDiscountTypes().add(subscriptionType);
            }
            return Lists.newArrayList(discounts.values());
        }
    }

    private final class DiscountRowMapper implements RowMapper<Discount> {
        @Override
        public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new Discount(rs.getLong(DISCOUNT_ID_COLUMN_NAME), rs.getString(DISCOUNT_CODE_COLUMN_NAME),
                    DiscountAmountType.findByCode(rs.getString(DISCOUNT_AMOUNT_TYPE_COLUMN_NAME)),
                    rs.getBigDecimal(DISCOUNT_AMOUNT_COLUMN_NAME), rs.getString(DISCOUNT_DESCRIPTION_COLUMN_NAME),
                    rs.getBoolean(DISCOUNT_ONGOING_COLUMN_NAME), rs.getBoolean(DISCOUNT_SPECIAL_COLUMN_NAME),
                    rs.getTimestamp(DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME),
                    rs.getTimestamp(DISCOUNT_EXPIRATION_DATE_COLUMN_NAME));
        }
    }

    private SqlParameterSource createDiscountSqlParameterSource(Discount discount) {
        SqlParameterSource parameterSource = new AuditSqlParameterSource()
                .addValue(DISCOUNT_ID_COLUMN_NAME, discount.getId())
                .addValue(DISCOUNT_CODE_COLUMN_NAME, discount.getCode())
                .addValue(DISCOUNT_AMOUNT_TYPE_COLUMN_NAME, discount.getAmountType().getCode())
                .addValue(DISCOUNT_AMOUNT_COLUMN_NAME, discount.getAmount())
                .addValue(DISCOUNT_DESCRIPTION_COLUMN_NAME, discount.getDescription())
                .addValue(DISCOUNT_ONGOING_COLUMN_NAME, discount.getOngoing())
                .addValue(DISCOUNT_SPECIAL_COLUMN_NAME, discount.getSpecial())
                .addValue(DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME, discount.getEffectiveDate())
                .addValue(DISCOUNT_EXPIRATION_DATE_COLUMN_NAME, discount.getExpirationDate());
        return parameterSource;
    }

}
