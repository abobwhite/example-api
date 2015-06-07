package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeReadDao;
import com.daugherty.e2c.persistence.data.DiscountSubscriptionTypeWriteDao;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Spring JDBC implementation of the Discount Membership Level database operations.
 */
@Repository
public class JdbcDiscountSubscriptionTypeDao extends JdbcDao implements DiscountSubscriptionTypeReadDao,
        DiscountSubscriptionTypeWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    static final Function<SubscriptionType, Long> SUBSCRIPTION_TYPE_ID_FUNCTION = new Function<SubscriptionType, Long>() {
        @Override
        public Long apply(SubscriptionType result) {
            return result.getId();
        }
    };

    public static final String DISCOUNT_SUBSCRIPTION_TYPE_ID_COLUMN_NAME = "discounts_subscription_type_id";
    public static final String DISCOUNT_ID_COLUMN_NAME = "discounts_id";
    public static final String DISCOUNT_SUBSCRIPTION_TYPE_COLUMN_NAME = "subscription_type_id";

    private SimpleJdbcInsert discountSubscriptionTypeInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        discountSubscriptionTypeInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("discounts_subscription_type")
                .usingGeneratedKeyColumns(DISCOUNT_SUBSCRIPTION_TYPE_ID_COLUMN_NAME)
                .usingColumns(DISCOUNT_ID_COLUMN_NAME, DISCOUNT_SUBSCRIPTION_TYPE_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public SubscriptionType insert(Long discountsId, SubscriptionType subscriptionType) {
        LOGGER.info("Creating discount subscription type for discount " + discountsId + " subscription type "
                + subscriptionType);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(DISCOUNT_ID_COLUMN_NAME,
                discountsId).addValue(DISCOUNT_SUBSCRIPTION_TYPE_COLUMN_NAME, subscriptionType.getId());
        discountSubscriptionTypeInsert.execute(parameterSource);

        return subscriptionType;
    }

    @Override
    public List<SubscriptionType> insert(Long discountsId, List<SubscriptionType> subscriptionTypes) {
        if (!subscriptionTypes.isEmpty()) {
            for (SubscriptionType subscriptionType : subscriptionTypes) {
                insert(discountsId, subscriptionType);
            }

        }
        return subscriptionTypes;
    }

    @Override
    public void delete(Long discountsId, List<SubscriptionType> subscriptionTypes) {
        if (!subscriptionTypes.isEmpty()) {
            LOGGER.info("Deleting discount subscription types " + subscriptionTypes + " for discount " + discountsId);
            String sql = getSql("discountSubscriptionType/delete.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("discountsId", discountsId)
                    .addValue("subscriptionTypes", Lists.transform(subscriptionTypes, SUBSCRIPTION_TYPE_ID_FUNCTION));
            jdbcTemplate.update(sql, parameterSource);
        }
    }

    @Override
    public List<SubscriptionType> find(Long discountsId) {
        LOGGER.debug("Finding all Discount Subscription Types for discount " + discountsId);
        String sql = getSql("/discountSubscriptionType/find-by-discountId.sql");

        return jdbcTemplate.query(sql, new MapSqlParameterSource().addValue("discountsId", discountsId),
                new SubscriptionTypeRowMapper());
    }

    private class SubscriptionTypeRowMapper implements RowMapper<SubscriptionType> {
        @Override
        public SubscriptionType mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SubscriptionType.findById(rs.getLong(DISCOUNT_SUBSCRIPTION_TYPE_COLUMN_NAME));
        }
    }

}
