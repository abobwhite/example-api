package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.BusinessType;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class JdbcPartyBusinessTypeDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcPartyBusinessTypeDao partyBusinessTypeDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("buyer.sql");
    }

    @Test
    public void findByBuyerSnapshotIdThatExistsReturnsEnums() {
        List<BusinessType> businessTypes = partyBusinessTypeDao.findBySnapshotId(999112L);

        assertThat(businessTypes.size(), is(2));
        assertThat(businessTypes.get(0), is(BusinessType.PROCUREMENT_OFFICE));
        assertThat(businessTypes.get(1), is(BusinessType.MANUFACTURER));
    }

    @Test
    public void findByBuyerSnapshotIdThatDoesNotExistReturnsEmptyList() {
        List<BusinessType> businessTypes = partyBusinessTypeDao.findBySnapshotId(123456789L);

        assertThat(businessTypes.size(), is(0));
    }

    @Test
    public void findByBuyerSnapshotIdsThatExistsReturnsEnums() {
        Multimap<Long, BusinessType> businessTypesBySnapshot = partyBusinessTypeDao.findBySnapshotIds(Lists
                .newArrayList(999112L));

        assertThat(businessTypesBySnapshot.size(), is(2));
        Collection<BusinessType> businessTypes = businessTypesBySnapshot.get(999112L);
        assertTrue(businessTypes.contains(BusinessType.PROCUREMENT_OFFICE));
        assertTrue(businessTypes.contains(BusinessType.MANUFACTURER));
    }

    @Test
    public void findByBuyerSnapshotIdaThatDoNotExistReturnsEmptyList() {
        Multimap<Long, BusinessType> businessTypesBySnapshot = partyBusinessTypeDao.findBySnapshotIds(Lists
                .newArrayList(123456789L));

        assertThat(businessTypesBySnapshot.size(), is(0));
    }

    @Test
    public void updateBuyerBusinessTypes() {
        partyBusinessTypeDao.updateBusinessTypes(123456789L, Lists.newArrayList(BusinessType.BUSINESS_SERVICE));

        Map<String, Object> partyAuditBusinessTypeRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_business_type WHERE party_audit_id = ?", 123456789L);
        assertThat((Long) partyAuditBusinessTypeRowMap.get("party_audit_id"), is(123456789L));
        assertThat((Long) partyAuditBusinessTypeRowMap.get("business_type_id"), is(5L));
        assertThat((Date) partyAuditBusinessTypeRowMap.get("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
    }
}
