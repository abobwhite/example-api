package com.daugherty.e2c.service.security;

import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import com.daugherty.e2c.domain.E2CUser;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.daugherty.e2c.security.Role;

@RunWith(MockitoJUnitRunner.class)
public class FailureCountResettingAuthenticationSuccessHandlerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserWriteDao userWriteDao;

    @InjectMocks
    private final FailureCountResettingAuthenticationSuccessHandler handler = new FailureCountResettingAuthenticationSuccessHandler();

    @Test
    public void onAuthenticationSuccessResetsFailureCountForUsername() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                buildAuthentication(), null);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect(null);
        verify(userWriteDao).resetFailureCount(authentication.getName());
    }

    private Authentication buildAuthentication() {
        E2CUser user = new E2CUser("username", "password", true, 3, AuthorityUtils.createAuthorityList(Role.ADMIN),
                new Party(1L), true);
        return new UsernamePasswordAuthenticationToken(user, null);
    }

}
