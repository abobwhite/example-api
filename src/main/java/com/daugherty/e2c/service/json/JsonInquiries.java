package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of JsonInquiry objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("inquiries")
public class JsonInquiries extends ArrayList<JsonInquiry> {

}
