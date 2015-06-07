package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;

import com.daugherty.e2c.domain.Discount;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of Discount domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("discounts")
public class JsonDiscounts extends ArrayList<JsonDiscount> {

    public JsonDiscounts(List<Discount> discounts) {
        for (Discount discount : discounts) {
            add(new JsonDiscount(discount));
        }
    }

}
