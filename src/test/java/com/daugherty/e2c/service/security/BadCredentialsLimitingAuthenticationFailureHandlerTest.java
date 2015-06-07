package com.daugherty.e2c.service.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;

import com.daugherty.e2c.persistence.data.UserWriteDao;

@RunWith(MockitoJUnitRunner.class)
public class BadCredentialsLimitingAuthenticationFailureHandlerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserWriteDao userWriteDao;

    @InjectMocks
    private final BadCredentialsLimitingAuthenticationFailureHandler handler = new BadCredentialsLimitingAuthenticationFailureHandler();

    @Test
    public void onAuthenticationFailureAfterBadCredentialsExceptionUpdatesDatabaseAndReturns401() throws Exception {
        when(request.getParameter(BadCredentialsLimitingAuthenticationFailureHandler.USERNAME_PARAMETER)).thenReturn(
                "username");

        handler.onAuthenticationFailure(request, response, new BadCredentialsException("Really bad, dude"));

        verify(userWriteDao).incrementFailureCount("username");
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: Really bad, dude");
    }

    @Test
    public void onAuthenticationFailureAfterOtherExceptionJustReturns401() throws Exception {
        handler.onAuthenticationFailure(request, response, new LockedException("Really bad, dude"));

        verifyZeroInteractions(request, userWriteDao);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: Really bad, dude");
    }

}
