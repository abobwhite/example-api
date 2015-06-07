package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import com.daugherty.e2c.domain.MembershipLevel;

public class JsonMembershipLevelTest {

    @Test
    public void constructor() {

        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, true, false, false);

        JsonMembershipLevel level = new JsonMembershipLevel(membershipLevel);

        assertThat(level, is(notNullValue()));
        assertThat(level.getLevel(), is(1));
        assertThat(level.getPrice().toString(), is("5.86"));
        assertThat(level.getMonthsOfTerm(), is(6));
        assertThat(level.getProductCount(), is(5));
        assertThat(level.getMessageCount(), is(2147483647));
        assertThat(level.getTranslationCount(), is(600));
        assertThat(level.getProfilePublic(), is(true));
        assertThat(level.getHotProductCount(), is(0));
        assertThat(level.getIncludedInProductAlerts(), is(false));
        assertThat(level.getSupplierMessagingEnabled(), is(false));
        assertThat(level.getExportTutorialAccessible(), is(false));
        assertThat(level.getAdditionalProductImageCount(), is(0));
        assertThat(level.getVerifiableByThirdParty(), is(false));
        assertThat(level.getAdvancedWebAndMailCapabilityEnabled(), is(false));
        assertThat(level.getVideoUploadable(), is(false));
        assertThat(level.getMarketAnalysis(), is(true));
        assertThat(level.getBuyerSearch(), is(false));
        assertThat(level.getLogisticsAssistance(), is(false));
        assertThat(level.getVideoUploadable(), is(false));
        assertThat(level.getVideoUploadable(), is(false));
    }
}
