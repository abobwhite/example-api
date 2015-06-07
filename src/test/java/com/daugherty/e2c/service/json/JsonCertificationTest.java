package com.daugherty.e2c.service.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Certification;
import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.security.Role;

@RunWith(MockitoJUnitRunner.class)
public class JsonCertificationTest {

    private static final Principal AUTHENTICATION = new UsernamePasswordAuthenticationToken(new E2CUser("username",
            "password", true, 0, AuthorityUtils.createAuthorityList(Role.BUYER), new Party(1L), true), null);

    @Mock
    private DocumentUrlFactory documentUrlFactory;

    @Test
    public void existingCertificationConstructionWithFullyPopulatedDomainObjectPopulatesAllFieldsForAuthenticatedUsers() {
        Certification domainCertificate = new Certification(42L, "standard", "certNbr123", "link", 43L, "issuedBy",
                1234567890L, "scopeRange", ApprovalStatus.APPROVED, 2, 1L);
        when(documentUrlFactory.createDocumentUrl("link", Locale.ENGLISH)).thenReturn("url");

        JsonCertification jsonCertification = new JsonCertification(domainCertificate, documentUrlFactory,
                Locale.ENGLISH, AUTHENTICATION);

        assertThat(jsonCertification.getId(), is(domainCertificate.getId()));
        assertThat(jsonCertification.getStandard(), is(domainCertificate.getStandard()));
        assertThat(jsonCertification.getCertificateNumber(), is(domainCertificate.getCertificateNumber()));
        assertThat(jsonCertification.getLink(), is("url"));
        assertThat(jsonCertification.getIssuedBy(), is(domainCertificate.getIssuedBy()));
        assertThat(jsonCertification.getIssuedDate(), is(new Date(domainCertificate.getIssuedDate())));
        assertThat(jsonCertification.getScopeRange(), is(domainCertificate.getScopeRange()));
        assertThat(jsonCertification.getApprovalStatus(), is(domainCertificate.getApprovalStatus().getName()));
        assertThat(jsonCertification.getVersion(), is(domainCertificate.getVersion()));
        assertThat(jsonCertification.getSnapshotId(), is(domainCertificate.getSnapshotId()));
    }

    @Test
    public void existingBuyerConstructionShouldIgnoreEmailAndChatIdentitiesForAnonymousUsers() {
        Certification domainCertificate = new Certification(42L, "standard", "certNbr123", "link", 43L, "issuedBy",
                1234567890L, "scopeRange", ApprovalStatus.APPROVED, 2, 1L);
        when(documentUrlFactory.createDocumentUrl("link", Locale.ENGLISH)).thenReturn("url");

        JsonCertification jsonCertification = new JsonCertification(domainCertificate, documentUrlFactory,
                Locale.ENGLISH, null);

        assertThat(jsonCertification.getLink(), is(nullValue()));
    }

}
