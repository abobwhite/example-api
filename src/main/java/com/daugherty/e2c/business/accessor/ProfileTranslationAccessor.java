package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.persistence.data.ProfileTranslationReadDao;

/**
 * Accessor for supplier ProfileTranslations.
 */
@Service("profileTranslationAccessor")
public class ProfileTranslationAccessor extends BaseAccessor<ProfileTranslation> {

    @Inject
    private ProfileTranslationReadDao profileTranslationReadDao;

    @Override
    public List<ProfileTranslation> find(Filter<ProfileTranslation> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProfileTranslation load(Long supplierId, Locale locale) {
        return profileTranslationReadDao.loadByLatestSupplierId(supplierId);
    }
}
