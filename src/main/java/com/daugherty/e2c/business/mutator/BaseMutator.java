package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.domain.Entity;
import com.daugherty.e2c.persistence.data.Hashids;

public abstract class BaseMutator<E extends Entity> implements Mutator<E> {
    @Inject
    protected Hashids hashids;

    @Override
    public void delete(String entityId) {
        delete(hashids.decode(entityId)[0]);
    }

    protected String encrypt(Long id) {
        return hashids.encode(id);
    }

    protected Long decrypt(String publicId) {
        return hashids.decode(publicId)[0];
    }

}
