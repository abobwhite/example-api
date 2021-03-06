package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of JsonMessage objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("productMessages")
public class JsonProductMessages extends ArrayList<JsonProductMessage> {

}
