package com.daugherty.e2c.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.daugherty.e2c.persistence.data.UserWriteDao;

/**
 * Extends <tt>SavedRequestAwareAuthenticationSuccessHandler</tt> by resetting the user's failure count after a
 * successful login..
 */
public class FailureCountResettingAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private UserWriteDao userWriteDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        LOGGER.debug(authentication.getName() + " successfully authenticated, so resetting failure count");
        userWriteDao.resetFailureCount(authentication.getName());
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * This setter exists because we cannot rely on @Inject annotation; this class is instantiated in security.xml,
     * whose beans are instantiated using setter injection, not classpath component scanning.
     */
    public void setUserWriteDao(UserWriteDao userWriteDao) {
        this.userWriteDao = userWriteDao;
    }

}
