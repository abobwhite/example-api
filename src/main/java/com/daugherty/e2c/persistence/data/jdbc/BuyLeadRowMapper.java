package com.daugherty.e2c.persistence.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.ProductCategory;

/**
 * Map a Result Set to a Buy Lead Domain Object
 */
public class BuyLeadRowMapper implements RowMapper<BuyLead> {
    private static final String BUY_LEAD_ID_COLUMN_NAME = "buy_lead_id";
    private static final String EFFECTIVE_DATE_COLUMN_NAME = "effective_date";
    private static final String EXPIRATION_DATE_COLUMN_NAME = "expiration_date";

    PartyRowMapper partyRowMapper = new PartyRowMapper();

    @Override
    public BuyLead mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.mapRow(rs, rowNum, "");
    }

    public BuyLead mapRow(ResultSet rs, int rowNum, String partyPrefix) throws SQLException {
        return new BuyLead(rs.getLong(BUY_LEAD_ID_COLUMN_NAME), partyRowMapper.mapRow(rs, partyPrefix),
                new ProductCategoryRowMapper().mapRow(rs, rowNum), rs.getTimestamp(EFFECTIVE_DATE_COLUMN_NAME),
                rs.getTimestamp(EXPIRATION_DATE_COLUMN_NAME));
    }

    private class ProductCategoryRowMapper implements RowMapper<ProductCategory> {

        @Override
        public ProductCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProductCategory(rs.getLong(JdbcProductCategoryDao.PRODUCT_CATEGORY_ID_COLUMN_NAME),
                    rs.getString(JdbcProductCategoryDao.NAME_COLUMN_NAME), 1L,
                    rs.getString(JdbcProductCategoryDao.LINK_COLUMN_NAME), null, null);
        }
    };
};
