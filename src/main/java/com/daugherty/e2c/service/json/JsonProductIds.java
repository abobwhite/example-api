package com.daugherty.e2c.service.json;

import java.util.ArrayList;
import java.util.List;

import com.daugherty.e2c.domain.Entity;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Represents and ordered list of Product Ids
 */
@SuppressWarnings("serial")
@JsonRootName("productIds")
public class JsonProductIds extends ArrayList<Long> {

    public JsonProductIds() {
        super();
    }

    public JsonProductIds(List<? extends Entity> entities) {
        for (Entity entity : entities) {
            add(entity.getId());
        }
    }

}
