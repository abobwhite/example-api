package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BuyerApprovalTest {

    @Test
    public void getEmailWithPersonalNameCombinesFirstAndLastNameWithEmailAddress() throws Exception {
        BuyerApproval party = new BuyerApproval(null, null, null, "first", "last", "person@company.com", null, null, null,
                null, null);
        assertThat(party.getEmailWithPersonalName(), is("first last <person@company.com>"));
    }

}
