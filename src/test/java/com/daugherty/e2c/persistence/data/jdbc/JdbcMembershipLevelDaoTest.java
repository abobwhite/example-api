package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.daugherty.e2c.domain.MembershipLevel;
import com.google.common.collect.Lists;

public class JdbcMembershipLevelDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcMembershipLevelDao membershipLevelDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void loadByValueThatExistsReturnsDomainObject() throws Exception {
        MembershipLevel level = membershipLevelDao.loadByValue(2);

        assertThat(level, is(notNullValue()));
        assertThat(level.getId(), is(2L));
        assertThat(level.getValue(), is(2));
        assertThat(level.getPrice().toString(), is("488.000"));
        assertThat(level.getMonthsOfTerm(), is(6));
        assertThat(level.getProductCount(), is(20));
        assertThat(level.getMessageCount(), is(2147483647));
        assertThat(level.getTranslationCount(), is(2400));
        assertThat(level.isProfilePublic(), is(true));
        assertThat(level.getHotProductCount(), is(10));
        assertThat(level.isIncludedInProductAlerts(), is(false));
        assertThat(level.isSupplierMessagingEnabled(), is(true));
        assertThat(level.isExportTutorialAccessible(), is(true));
        assertThat(level.getAdditionalProductImageCount(), is(2));
        assertThat(level.isVerifiableByThirdParty(), is(false));
        assertThat(level.isAdvancedWebAndMailCapabilityEnabled(), is(false));
        assertThat(level.isVideoUploadable(), is(false));
    }

    @Test
    public void loadByValueThatDoesNotExistThrowsException() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        membershipLevelDao.loadByValue(123456789);
    }

    @Test
    public void loadAllReturnsDomainObject() throws Exception {
        List<MembershipLevel> levels = membershipLevelDao.loadAll();

        assertThat(levels.size(), is(4));

        assertThat(levels.get(0).getId(), is(5L));
        assertThat(levels.get(1).getId(), is(2L));
        assertThat(levels.get(2).getId(), is(3L));
        assertThat(levels.get(3).getId(), is(4L));
    }

    @Test
    public void expireMembershipLevel() throws Exception {
        expectedException.expect(EmptyResultDataAccessException.class);

        membershipLevelDao.expireMembershipLevel(2);

        jdbcTemplate.queryForMap("SELECT * FROM active_membership_level_view WHERE level = ?", 2);

    }

    @Test
    public void createMembershipLevel() throws Exception {
        MembershipLevel membershipLevel = new MembershipLevel(1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600, true,
                0, true, false, true, 8, false, true, false, true, true, false, true);

        MembershipLevel createdMembershipLevel = membershipLevelDao.create(membershipLevel);

        Map<String, Object> membershipLevelRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM membership_level WHERE membership_level_id = ?", createdMembershipLevel.getId());
        assertThat((Long) membershipLevelRowMap.get("membership_level_id"), is(createdMembershipLevel.getId()));
        assertThat((Long) membershipLevelRowMap.get("membership_tier_id"), is(createdMembershipLevel.getValue()
                .longValue()));
        assertThat((Integer) membershipLevelRowMap.get("term"), is(createdMembershipLevel.getMonthsOfTerm()));
        assertThat(((BigDecimal) membershipLevelRowMap.get("base_price")).doubleValue(), is(createdMembershipLevel
                .getPrice().doubleValue()));
        assertThat((Integer) membershipLevelRowMap.get("number_of_product"),
                is(createdMembershipLevel.getProductCount()));
        assertThat((Long) membershipLevelRowMap.get("number_of_messages"), is(createdMembershipLevel.getMessageCount()
                .longValue()));
        assertThat((Integer) membershipLevelRowMap.get("number_of_translations"), is(createdMembershipLevel
                .getTranslationCount().intValue()));
        assertThat((Boolean) membershipLevelRowMap.get("company_showroom"),
                is(createdMembershipLevel.isProfilePublic()));
        assertThat((Integer) membershipLevelRowMap.get("hot_product_listing"),
                is(createdMembershipLevel.getHotProductCount()));
        assertThat((Boolean) membershipLevelRowMap.get("product_alert"),
                is(createdMembershipLevel.isIncludedInProductAlerts()));
        assertThat((Boolean) membershipLevelRowMap.get("supplier_to_supplier_inquiry"),
                is(createdMembershipLevel.isSupplierMessagingEnabled()));
        assertThat((Boolean) membershipLevelRowMap.get("export_to_china_tutorial"),
                is(createdMembershipLevel.isExportTutorialAccessible()));
        assertThat((Integer) membershipLevelRowMap.get("enhance_profile"),
                is(createdMembershipLevel.getAdditionalProductImageCount()));
        assertThat((Boolean) membershipLevelRowMap.get("third_party_verification"),
                is(createdMembershipLevel.isVerifiableByThirdParty()));
    }

}
