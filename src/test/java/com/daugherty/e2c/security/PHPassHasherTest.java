package com.daugherty.e2c.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@Ignore("Re-enable this test if we ever mess with this class again")
public class PHPassHasherTest {

    private final PHPassHasher hasher = new PHPassHasher();

    @Test
    public void isMatchSucceedsWithProvidedExamplesFromLegacyData() {
        String invalidHash = "!@#NOT_A_HASH!@#";

        String first = "skiwtmi2c";
        assertFalse(hasher.isMatch(first, invalidHash));
        assertTrue(hasher.isMatch(first, "$S$D3X9WcNylPtOeXclUsyOdDl0z4G3PpxcX2vFlSxtFAX.VLc1/3AA"));
        hasher.createHash(first);

        String second = "siiwtmi2c8";
        assertFalse(hasher.isMatch(second, invalidHash));
        assertTrue(hasher.isMatch(second, "$S$DANuZRrFQYmdROxoM6Nv5MJvSXIiJjvO1thuyMt3FPUfLlUZQpNQ"));

        String third = "siiwtmi2";
        assertFalse(hasher.isMatch(third, invalidHash));
        assertTrue(hasher.isMatch(third, "$S$DpIM1uoESFrujxMgQY8sZWo8Dg0G08TUC8OaxomRkTDHTop9769O"));
    }

    @Test
    public void isMatchSucceedsWithKnownGoodHashes() {
        // Test multiple cases, just in case
        assertTrue(hasher.isMatch("huan", "$S$DXZrp425p2VN9LXpDWxiBeqSZSPexyZNNPUo80fTTN2e/W15a97G"));
        assertTrue(hasher.isMatch("Cookies1", "$S$Dr3Hm8Od9X0Ux/olZ.B8Uwh6./3E1l4nPfXebovKVvMB9zcmjkTn"));
    }

    @Test
    public void isMatchFailsWithWrongPassword() {
        assertFalse(hasher.isMatch("OHH NO! Not cookies!", "$S$Dr3Hm8Od9X0Ux/olZ.B8Uwh6./3E1l4nPfXebovKVvMB9zcmjkTn"));
    }

    @Test
    public void isMatchFailsWithoutPassword() {
        assertFalse(hasher.isMatch("", "$S$Dr3Hm8Od9X0Ux/olZ.B8Uwh6./3E1l4nPfXebovKVvMB9zcmjkTn"));
    }

    @Test
    public void createHashMatchesHasFromOriginalPassword() {
        String password = "ThisIs@C♥mplexPÃssword™123";
        String hash = hasher.createHash(password);
        assertTrue(hasher.isMatch(password, hash));
        assertFalse(hasher.isMatch("NotTheSamePassword", hash));
    }

}
