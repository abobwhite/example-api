package com.daugherty.e2c.business;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.Membership;

/**
 * Helps compare active vs provisional memberships
 */
@Component
public class MembershipRenewalTypeHelper {
	public boolean hasProvisionalRenewalOrDowngrade(Membership active, Membership provisional) {
		if(active != null && provisional != null) {
			return provisional.getLevel().getValue() <= active.getLevel().getValue();
		}
		
		return false;
	}
	
	public boolean hasProvisionalUpgrade(Membership active, Membership provisional) {
		if(active != null && provisional != null) {
			return provisional.getLevel().getValue() > active.getLevel().getValue();
		}
		
		return false;
	}
}
