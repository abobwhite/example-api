package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of JsonInteraction objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("interactions")
public class JsonInteractions extends ArrayList<JsonInteraction> {

}
