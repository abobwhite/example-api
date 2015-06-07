package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of User domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("users")
public class JsonUsers extends ArrayList<JsonUser> {
}
