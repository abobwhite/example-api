package com.daugherty.e2c.business.accessor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.persistence.data.MembershipLevelReadDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MembershipLevelAccessorTest {
    private static final MembershipLevel LEVEL_ONE = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5,
            2147483647, 600, true, 0, false, false, false, 0, false, false, false, false, false, false, false);

    @Mock
    private MembershipLevelReadDao membershipLevelReadDao;

    @InjectMocks
    private MembershipLevelAccessor membershipLevelAccessor = new MembershipLevelAccessor();

    @Test
    public void loadAll() {
        when(membershipLevelReadDao.loadAll()).thenReturn(Lists.newArrayList(LEVEL_ONE));

        List<MembershipLevel> membershipLevels = membershipLevelAccessor.find(null);

        MembershipLevel level = membershipLevels.get(0);

        assertThat(membershipLevels.size(), is(1));
        assertThat(level, is(notNullValue()));
        assertThat(level.getId(), is(1L));
        assertThat(level.getValue(), is(1));
        assertThat(level.getPrice().toString(), is("5.86"));
        assertThat(level.getMonthsOfTerm(), is(6));
        assertThat(level.getProductCount(), is(5));
        assertThat(level.getMessageCount(), is(2147483647));
        assertThat(level.getTranslationCount(), is(600));
        assertThat(level.isProfilePublic(), is(true));
        assertThat(level.getHotProductCount(), is(0));
        assertThat(level.isIncludedInProductAlerts(), is(false));
        assertThat(level.isSupplierMessagingEnabled(), is(false));
        assertThat(level.isExportTutorialAccessible(), is(false));
        assertThat(level.getAdditionalProductImageCount(), is(0));
        assertThat(level.isVerifiableByThirdParty(), is(false));
        assertThat(level.isAdvancedWebAndMailCapabilityEnabled(), is(false));
        assertThat(level.isVideoUploadable(), is(false));
    }

    @Test
    public void load() {
        when(membershipLevelReadDao.loadByValue(1L)).thenReturn(LEVEL_ONE);

        MembershipLevel level = membershipLevelAccessor.load(1L, Locale.ENGLISH);

        assertThat(level, is(notNullValue()));
        assertThat(level.getId(), is(1L));
        assertThat(level.getValue(), is(1));
        assertThat(level.getPrice().toString(), is("5.86"));
        assertThat(level.getMonthsOfTerm(), is(6));
        assertThat(level.getProductCount(), is(5));
        assertThat(level.getMessageCount(), is(2147483647));
        assertThat(level.getTranslationCount(), is(600));
        assertThat(level.isProfilePublic(), is(true));
        assertThat(level.getHotProductCount(), is(0));
        assertThat(level.isIncludedInProductAlerts(), is(false));
        assertThat(level.isSupplierMessagingEnabled(), is(false));
        assertThat(level.isExportTutorialAccessible(), is(false));
        assertThat(level.getAdditionalProductImageCount(), is(0));
        assertThat(level.isVerifiableByThirdParty(), is(false));
        assertThat(level.isAdvancedWebAndMailCapabilityEnabled(), is(false));
        assertThat(level.isVideoUploadable(), is(false));
    }

}
