package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.Certification;

@Transactional
public abstract class CertificationAccessor extends BaseAccessor<Certification> {

    @Override
    public Certification load(Long certificationId, Locale locale) {
        return doLoad(certificationId, locale);
    }

    @Override
    public List<Certification> find(Filter<Certification> filter) {
        throw new UnsupportedOperationException();
    }
    
    public abstract Certification doLoad(Long certificationId, Locale locale);

}
