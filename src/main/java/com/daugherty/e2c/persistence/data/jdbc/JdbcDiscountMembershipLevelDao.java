package com.daugherty.e2c.persistence.data.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.data.DiscountMembershipLevelReadDao;
import com.daugherty.e2c.persistence.data.DiscountMembershipLevelWriteDao;

/**
 * Spring JDBC implementation of the Discount Membership Level database operations.
 */
@Repository
public class JdbcDiscountMembershipLevelDao extends JdbcDao implements DiscountMembershipLevelReadDao,
        DiscountMembershipLevelWriteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final String DISCOUNT_MEMBERSHIP_TIER_ID_COLUMN_NAME = "discounts_membership_tier_id";
    public static final String DISCOUNT_ID_COLUMN_NAME = "discounts_id";
    public static final String DISCOUNT_MEMBERSHIP_TIER_COLUMN_NAME = "membership_tier_id";

    private SimpleJdbcInsert discountMembershipLevelInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        discountMembershipLevelInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("discounts_membership_tier")
                .usingGeneratedKeyColumns(DISCOUNT_MEMBERSHIP_TIER_ID_COLUMN_NAME)
                .usingColumns(DISCOUNT_ID_COLUMN_NAME, DISCOUNT_MEMBERSHIP_TIER_COLUMN_NAME,
                        LAST_MODIFIED_BY_COLUMN_NAME, LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public Integer insert(Long discountsId, Integer level) {
        LOGGER.info("Creating discount membership level for discount " + discountsId + " level " + level);
        SqlParameterSource parameterSource = new AuditSqlParameterSource().addValue(DISCOUNT_ID_COLUMN_NAME,
                discountsId).addValue(DISCOUNT_MEMBERSHIP_TIER_COLUMN_NAME, level);
        discountMembershipLevelInsert.execute(parameterSource);

        return level;
    }

    @Override
    public List<Integer> insert(Long discountsId, List<Integer> levels) {
        if (!levels.isEmpty()) {
            for (Integer level : levels) {
                insert(discountsId, level);
            }
        }

        return levels;
    }

    @Override
    public void delete(Long discountsId, List<Integer> levels) {
        if (!levels.isEmpty()) {
            LOGGER.info("Deleting discount membership levels " + levels + " for discount " + discountsId);
            String sql = getSql("discountMembershipLevel/delete.sql");
            SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("discountsId", discountsId)
                    .addValue("levels", levels);
            jdbcTemplate.update(sql, parameterSource);

        }
    }

    @Override
    public List<Integer> find(Long discountsId) {
        LOGGER.debug("Finding all Discount Membership Levels for discount " + discountsId);
        String sql = getSql("/discountMembershipLevel/find-by-discountId.sql");

        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource().addValue("discountsId", discountsId),
                Integer.class);
    }

}
