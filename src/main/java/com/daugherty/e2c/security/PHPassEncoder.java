package com.daugherty.e2c.security;

import org.springframework.security.authentication.encoding.PasswordEncoder;


/**
 * Implementation of Spring PasswordEncoder
 */
@SuppressWarnings("deprecation")
public class PHPassEncoder implements PasswordEncoder {

    private PHPassHasher phpassHasher = new PHPassHasher();

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return phpassHasher.createHash(rawPass);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        return phpassHasher.isMatch(rawPass, encPass);
    }
}
