package com.daugherty.e2c.persistence.data;

/**
 * Defines database change operations for Product publication state.
 */
public interface ProductPublicationWriteDao {

    void insertPublished(Long id, boolean published);

    void insertHot(Long id, boolean hot);

}
