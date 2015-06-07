package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.persistence.data.ProfileTranslationReadDao;
import com.daugherty.e2c.persistence.data.ProfileTranslationWriteDao;

/**
 * Mutator for ProfileTranslation domain objects.
 */
@Service("profileTranslationMutator")
@Transactional
public class ProfileTranslationMutator extends BaseMutator<ProfileTranslation> {

    @Inject
    private ProfileTranslationReadDao profileTranslationReadDao;
    @Inject
    private ProfileTranslationWriteDao profileTranslationWriteDao;

    @Override
    public ProfileTranslation create(ProfileTranslation translation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProfileTranslation update(ProfileTranslation translation) {
        Long updateId = profileTranslationReadDao.getUpdateIdForLatestSupplier(translation.getId());
        if (updateId == null) {
            return profileTranslationWriteDao.insert(translation);
        } else {
            return profileTranslationWriteDao.update(updateId, translation);
        }
    }

    @Override
    public void delete(Long entityId) {
        throw new UnsupportedOperationException();
    }
}
