package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Party;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Products domain objects to/from JSON..
 */
@SuppressWarnings("serial")
@JsonRootName("parties")
public class JsonParties extends ArrayList<JsonParty> {

    public JsonParties(List<Party> parties, DocumentUrlFactory documentUrlFactory, Locale locale) {
        for (Party party : parties) {
            add(new JsonParty(party, documentUrlFactory, locale));
        }
    }
}
