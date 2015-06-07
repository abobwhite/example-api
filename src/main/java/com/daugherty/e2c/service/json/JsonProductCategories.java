package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of ProductCategory domain objects to/from JSON..
 */
@SuppressWarnings("serial")
@JsonRootName("productCategories")
public class JsonProductCategories extends ArrayList<JsonProductCategory> {

}
