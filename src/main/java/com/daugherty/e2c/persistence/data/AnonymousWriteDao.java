package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Anonymous;

public interface AnonymousWriteDao {
    Anonymous insert(Anonymous anonymous);

    Anonymous update(Anonymous anonymous);

}
