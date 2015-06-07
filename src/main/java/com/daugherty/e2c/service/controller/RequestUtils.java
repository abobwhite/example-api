package com.daugherty.e2c.service.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for manipulating HTTP requests.
 */
public class RequestUtils {

    private final static Logger LOG = LoggerFactory.getLogger(RequestUtils.class);

    private RequestUtils() {
    }

    @SuppressWarnings("unchecked")
    public static String remoteIp(HttpServletRequest request) {
        // Based on http://java-monitor.com/forum/showthread.php?p=1101#post1101
        final Enumeration<String> headers = request.getHeaders("X-Forwarded-For");
        if (headers == null) {
            LOG.warn("No access to HTTP headers");
        } else {
            while (headers.hasMoreElements()) {
                final String[] ips = headers.nextElement().split(",");
                for (int i = 0; i < ips.length; i++) {
                    final String proxy = ips[i].trim();
                    if (!"unknown".equals(proxy) && !proxy.isEmpty()) {
                        try {
                            InetAddress.getByName(proxy);
                            return proxy;
                        } catch (UnknownHostException e) {
                            LOG.warn("Ignoring unknown host " + proxy + ": " + e.getMessage());
                        }
                    }
                }
            }
        }

        return request.getRemoteAddr();
    }

}
