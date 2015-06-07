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

import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.DiscountAmountType;
import com.daugherty.e2c.domain.MembershipDiscount;
import com.daugherty.e2c.persistence.data.MembershipDiscountReadDao;
import com.daugherty.e2c.persistence.data.MembershipDiscountWriteDao;

/**
 * Spring-JDBC implementation of the Membership Discount database access operations.
 */
@Repository
public class JdbcMembershipDiscountDao extends JdbcDao implements MembershipDiscountReadDao, MembershipDiscountWriteDao {
    private static final String MEMBERSHIP_AUDIT_DISCOUNTS_ID_COLUMN_NAME = "membership_audit_discounts_id";
    private static final String MEMBERSHIP_AUDIT_ID_COLUMN_NAME = "membership_audit_id";
    private static final String DISCOUNTS_ID_COLUMN_NAME = "discounts_id";
    public static final String DISCOUNT_ID_COLUMN_NAME = "discounts_id";
    public static final String DISCOUNT_CODE_COLUMN_NAME = "discount_code";
    public static final String DISCOUNT_AMOUNT_TYPE_COLUMN_NAME = "discount_amount_type";
    public static final String DISCOUNT_AMOUNT_COLUMN_NAME = "discount_amount";
    public static final String DISCOUNT_DESCRIPTION_COLUMN_NAME = "description";
    public static final String DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    public static final String DISCOUNT_EXPIRATION_DATE_COLUMN_NAME = "expiration_date";
    private static final String AMOUNT_COLUMN_NAME = "amount";
    public static final String ONGOING_COLUMN_NAME = "ongoing";
    public static final String SPECIAL_COLUMN_NAME = "special";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private SimpleJdbcInsert membershipDiscountInsert;

    @Override
    protected void createSimpleJdbcInserts(DataSource dataSource) {
        membershipDiscountInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("membership_audit_discounts")
                .usingGeneratedKeyColumns(MEMBERSHIP_AUDIT_DISCOUNTS_ID_COLUMN_NAME)
                .usingColumns(MEMBERSHIP_AUDIT_ID_COLUMN_NAME, DISCOUNTS_ID_COLUMN_NAME, AMOUNT_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_BY_COLUMN_NAME,
                        AuditSqlParameterSource.LAST_MODIFIED_DATE_COLUMN_NAME);
    }

    @Override
    public List<MembershipDiscount> findBySnapshotId(Long snapshotId) {
        LOGGER.info("Retrieving membership discounts for membership with snapshot Id " + snapshotId);
        String sql = getSql("membershipdiscount/findByMembershipSnapshotId.sql");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("snapshotId", snapshotId);
        RowMapper<MembershipDiscount> mapper = new RowMapper<MembershipDiscount>() {
            @Override
            public MembershipDiscount mapRow(ResultSet rs, int rowNum) throws SQLException {
                Discount discount = new Discount(rs.getLong(DISCOUNT_ID_COLUMN_NAME),
                        rs.getString(DISCOUNT_CODE_COLUMN_NAME), DiscountAmountType.findByCode(rs
                                .getString(DISCOUNT_AMOUNT_TYPE_COLUMN_NAME)),
                        rs.getBigDecimal(DISCOUNT_AMOUNT_COLUMN_NAME), rs.getString(DISCOUNT_DESCRIPTION_COLUMN_NAME),
                        rs.getBoolean(ONGOING_COLUMN_NAME), rs.getBoolean(SPECIAL_COLUMN_NAME),
                        rs.getTimestamp(DISCOUNT_EFFECTIVE_DATE_COLUMN_NAME),
                        rs.getTimestamp(DISCOUNT_EXPIRATION_DATE_COLUMN_NAME));

                return new MembershipDiscount(rs.getLong(MEMBERSHIP_AUDIT_DISCOUNTS_ID_COLUMN_NAME),
                        rs.getLong(MEMBERSHIP_AUDIT_ID_COLUMN_NAME), discount, rs.getBigDecimal(AMOUNT_COLUMN_NAME));
            }
        };

        return jdbcTemplate.query(sql, parameterSource, mapper);
    }

    @Override
    public List<MembershipDiscount> updateMembershipDiscounts(Long snapshotId,
            List<MembershipDiscount> membershipDiscounts) {
        LOGGER.info("Creating Membership Discounts with snapshot Id " + snapshotId);

        for (MembershipDiscount membershipDiscount : membershipDiscounts) {
            SqlParameterSource parameterSource = new AuditSqlParameterSource()
                    .addValue(MEMBERSHIP_AUDIT_ID_COLUMN_NAME, snapshotId)
                    .addValue(DISCOUNTS_ID_COLUMN_NAME, membershipDiscount.getDiscount().getId())
                    .addValue(AMOUNT_COLUMN_NAME, membershipDiscount.getAmount());
            Number membershipDiscountKey = membershipDiscountInsert.executeAndReturnKey(parameterSource);
            membershipDiscount.setId(membershipDiscountKey.longValue());
        }

        return membershipDiscounts;
    }

}
