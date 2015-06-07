package com.daugherty.e2c.business.accessor;

import java.util.Locale;

import javax.inject.Inject;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.domain.Entity;
import com.daugherty.e2c.persistence.data.Hashids;

public abstract class BaseAccessor<E extends Entity> implements Accessor<E> {

    @Inject
    protected Hashids hashids;

    public E load(String publicId, Locale locale) {
        return load(decrypt(publicId), locale);
    }

    protected String encrypt(Long id) {
        return hashids.encode(id);
    }

    protected Long decrypt(String publicId) {
        return hashids.decode(publicId)[0];
    }

    protected Long getPartyId(String publicPartyId) {
        return publicPartyId == null ? null : hashids.decode(publicPartyId)[0];
    }
}
