package com.daugherty.e2c.service.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

/**
 * Null implementation of redirect strategy.
 */
public class NullRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
    }

}
