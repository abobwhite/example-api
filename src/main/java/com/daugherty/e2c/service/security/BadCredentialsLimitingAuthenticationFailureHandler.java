package com.daugherty.e2c.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.daugherty.e2c.persistence.data.UserWriteDao;

/**
 * <tt>AuthenticationFailureHandler</tt> that updates the failure count (the "strike" count) for the user if a
 * <tt>BadCredentialsException</tt> is received.
 * <p>
 * This handler always just sends a 401 response to the client, with the error message from the
 * <tt>AuthenticationException</tt> which caused the failure.
 */
public class BadCredentialsLimitingAuthenticationFailureHandler implements AuthenticationFailureHandler {

    static final String USERNAME_PARAMETER = "userId";

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private UserWriteDao userWriteDao;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            String username = request.getParameter(USERNAME_PARAMETER);
            LOGGER.debug(username + " entered bad credentials, so incrementing their failure count");
            userWriteDao.incrementFailureCount(username);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
    }

    /**
     * This setter exists because we cannot rely on @Inject annotation; this class is instantiated in security.xml,
     * whose beans are instantiated using setter injection, not classpath component scanning.
     */
    public void setUserWriteDao(UserWriteDao userWriteDao) {
        this.userWriteDao = userWriteDao;
    }

}
