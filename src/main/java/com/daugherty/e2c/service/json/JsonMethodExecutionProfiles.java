package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of MostExecutionProfile objects to/from JSON..
 */
@SuppressWarnings("serial")
@JsonRootName("methodExecutionProfiles")
public class JsonMethodExecutionProfiles extends ArrayList<JsonMethodExecutionProfile> {

}
