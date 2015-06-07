package com.daugherty.e2c.service.security;

import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class RestAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    public void commenceJustReturnsUnauthorizedStatus() throws Exception {
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        entryPoint.commence(request, response, null);
        verify(response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
    }

}
