package com.daugherty.e2c.service.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of SupplierApproval domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("supplierApprovals")
public class JsonSupplierApprovals extends ArrayList<JsonSupplierApproval> {

}
