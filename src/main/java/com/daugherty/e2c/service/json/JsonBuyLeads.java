package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.BuyLead;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Buy Lead domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("buyLeads")
public class JsonBuyLeads extends ArrayList<JsonBuyLead> {

	public JsonBuyLeads(List<BuyLead> buyLeads, DocumentUrlFactory documentUrlFactory, Locale locale) {
		for (BuyLead buyLead : buyLeads) {
			add(new JsonBuyLead(buyLead, documentUrlFactory, locale));
		}
	}

}
