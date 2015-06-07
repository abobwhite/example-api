package com.daugherty.e2c.security;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Generates passwords for non-registered users. Typically ignored.
 */
@Ignore
public class NonRegisteredUserPasswordGeneratorTest {

    @Test
    public void generatePasswords() {
        PHPassHasher hasher = new PHPassHasher();

        System.out.println("fordp: " + hasher.createHash("hoopyfrood"));
        System.out.println("apdent: " + hasher.createHash("towel"));
        System.out.println("zaphodb: " + hasher.createHash("heartofgold"));
        System.out.println("tmcmillian: " + hasher.createHash("whitemice"));
        System.out.println("babelf: " + hasher.createHash("inyourear"));
        System.out.println("sbfast: " + hasher.createHash("magrathea"));
        System.out.println("hotblackd: " + hasher.createHash("disasterarea"));
        System.out.println("cherryBo: " + hasher.createHash("whatitis"));
    }

}
