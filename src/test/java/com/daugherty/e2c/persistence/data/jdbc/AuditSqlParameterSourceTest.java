package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.SecurityUtils;

public class AuditSqlParameterSourceTest {

    private Authentication originalAuthentication;

    @Before
    public void rememberOriginalAuthentication() {
        originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
    }

    @After
    public void restoreOriginalAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
    }

    @Test
    public void createWithoutAuthenticatedUserAddsDefaultUsername() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(null);

        AuditSqlParameterSource parameterSource = new AuditSqlParameterSource();
        assertThat((Date) parameterSource.getValue("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
        assertThat(parameterSource.getValue("last_modified_by").toString(), is(SecurityUtils.UNREGISTERED_USER));
    }

    @Test
    public void createWithAuthenticatedUserAddsAuthenticatedUsername() throws Exception {
        Authentication authentication = new RememberMeAuthenticationToken("unitTestKey", "apdent",
                new ArrayList<GrantedAuthority>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuditSqlParameterSource parameterSource = new AuditSqlParameterSource();
        assertThat((Date) parameterSource.getValue("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
        assertThat(parameterSource.getValue("last_modified_by").toString(), is("apdent"));
    }

    @Test
    public void createWithAuthenticatedUsernameAddsThatUsername() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("apdent", "towel");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuditSqlParameterSource parameterSource = new AuditSqlParameterSource();
        assertThat((Date) parameterSource.getValue("last_modified_date"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));
        assertThat(parameterSource.getValue("last_modified_by").toString(), is("apdent"));
    }
}
