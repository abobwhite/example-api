package com.daugherty.e2c.persistence.data;

import com.daugherty.e2c.domain.ProfileTranslation;

/**
 * Defines database change operations for ProfileTranslation objects.
 */
public interface ProfileTranslationWriteDao {

    ProfileTranslation insert(ProfileTranslation translation);

    ProfileTranslation update(Long updateId, ProfileTranslation translation);

}
