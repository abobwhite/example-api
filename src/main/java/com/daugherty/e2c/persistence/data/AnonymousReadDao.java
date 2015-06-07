package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.Anonymous;

public interface AnonymousReadDao {

    Anonymous load(Long id);

}
