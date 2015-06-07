package com.daugherty.e2c.domain;

import java.util.Comparator;

public class MembershipExpirationDateComparator implements Comparator<Membership> {

    @Override
    public int compare(Membership a, Membership b) {
        return a.getExpirationDate().compareTo(b.getExpirationDate());
    }
}
