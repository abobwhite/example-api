package com.daugherty.e2c.service.json;

import java.util.Locale;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.domain.Anonymous;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a Anonymous domain object to/from JSON.
 */
@JsonRootName(value = "anonymous")
public class JsonAnonymous extends JsonParty {
	
    public JsonAnonymous() {
    }

    public JsonAnonymous(Anonymous anonymous, DocumentUrlFactory documentUrlFactory, Locale locale) {
        super(anonymous, documentUrlFactory, locale);
    }
}
