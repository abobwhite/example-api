package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Product domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("latestProducts")
public class JsonSupplierLatestProducts extends ArrayList<JsonLatestProduct> {

}
