package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.domain.MembershipProductSummary;
import com.daugherty.e2c.persistence.data.MembershipProductSummaryReadDao;

/**
 * Spring JDBC implementation of the Supplier Product Summary database operations.
 */
@Repository
public class JdbcMembershipProductSummaryDao extends JdbcDao implements MembershipProductSummaryReadDao {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String MEMBERSHIP_ID_COLUMN_NAME = "membership_id";
    private static final String TOTAL_COLUMN_NAME = "total";
    private static final String PUBLISHED_COLUMN_NAME = "published";
    private static final String PENDING_APPROVAL_COLUMN_NAME = "pending_approval";
    private static final String PENDING_TRANSLATION_COLUMN_NAME = "pending_translation";
    private static final String WAITING_FOR_INFORMATION_COLUMN_NAME = "waiting_for_info";
    private static final String UNPUBLISHED_COLUMN_NAME = "unpublished";
    private static final String HOT_PRODUCT_COLUMN_NAME = "hot_product";
    private static final String APPROVED_COLUMN_NAME = "approved";
    private static final String DISAPPROVED_COLUMN_NAME = "disapproved";
    private static final String DRAFT_COLUMN_NAME = "draft";

    @Override
    public MembershipProductSummary loadByMembershipId(Long membershipId) {
        LOGGER.debug("Looking up product summary for membership " + membershipId);
        String sql = getSql("productsummary/load-by-membership.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("membershipId", membershipId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new ProductSummaryRowMapper());
    }

    @Override
    public MembershipProductSummary loadByProductId(Long productId) {
        LOGGER.debug("Looking up product summary for product " + productId);
        String sql = getSql("productsummary/load-by-product.sql");
        SqlParameterSource parameterSource = new MapSqlParameterSource("productId", productId);
        return jdbcTemplate.queryForObject(sql, parameterSource, new ProductSummaryRowMapper());
    }

    private final class ProductSummaryRowMapper implements RowMapper<MembershipProductSummary> {
        @Override
        public MembershipProductSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            MembershipProductSummary productSummary = new MembershipProductSummary(rs.getLong(MEMBERSHIP_ID_COLUMN_NAME),
                    rs.getInt(TOTAL_COLUMN_NAME), rs.getInt(PUBLISHED_COLUMN_NAME),
                    rs.getInt(PENDING_APPROVAL_COLUMN_NAME), rs.getInt(PENDING_TRANSLATION_COLUMN_NAME),
                    rs.getInt(WAITING_FOR_INFORMATION_COLUMN_NAME), rs.getInt(UNPUBLISHED_COLUMN_NAME),
                    rs.getInt(HOT_PRODUCT_COLUMN_NAME), 0, rs.getInt(APPROVED_COLUMN_NAME),
                    rs.getInt(DISAPPROVED_COLUMN_NAME), rs.getInt(DRAFT_COLUMN_NAME));

            return productSummary;
        }
    }

}
