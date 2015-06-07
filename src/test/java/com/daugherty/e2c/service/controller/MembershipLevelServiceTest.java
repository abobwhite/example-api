package com.daugherty.e2c.service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.MembershipLevel;
import com.daugherty.e2c.service.json.JsonMembershipLevel;
import com.daugherty.e2c.service.json.JsonMembershipLevels;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MembershipLevelServiceTest {
    @Mock
    private Accessor<MembershipLevel> membershipLevelAccessor;
    @Mock
    private Mutator<MembershipLevel> membershipLevelMutator;

    @InjectMocks
    private final MembershipLevelService service = new MembershipLevelService();

    @Test
    public void retrieveMembershipLevelDelegatesToAccessor() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false);

        when(membershipLevelAccessor.load(1L, Locale.ENGLISH)).thenReturn(membershipLevel);

        JsonMembershipLevel level = service.retrieveMembershipLevel(1L);

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
    }

    @Test
    public void retrieveMembershipLevelsDelegatesToAccessor() {
        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false);

        when(membershipLevelAccessor.find(null)).thenReturn(Lists.newArrayList(membershipLevel));

        JsonMembershipLevels jsonMembershipLevels = service.retrieveMembershipLevels();

        JsonMembershipLevel level = jsonMembershipLevels.get(0);

        assertThat(jsonMembershipLevels.size(), is(1));
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
    }

    @Test
    public void createMembershipLevelsDelegatesToMutator() {

        MembershipLevel membershipLevel = new MembershipLevel(1L, 1, BigDecimal.valueOf(5.86), 6, 5, 2147483647, 600,
                true, 0, false, false, false, 0, false, false, false, false, false, false, false);

        JsonMembershipLevel jsonMembershipLevel = new JsonMembershipLevel(membershipLevel);

        when(membershipLevelMutator.create(Mockito.any(MembershipLevel.class))).thenReturn(membershipLevel);

        JsonMembershipLevel createdMembershipLevel = service.createMembershipLevel(jsonMembershipLevel);

        assertThat(createdMembershipLevel.getLevel(), is(1));
    }
}
