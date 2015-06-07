package com.daugherty.e2c.business;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daugherty.e2c.persistence.data.ProductCategoryReadDao;
import com.daugherty.e2c.persistence.data.ProductReadDao;
import com.daugherty.e2c.persistence.data.SupplierReadDao;

/**
 * Maps legacy IDs for categories, products and suppliers to their counterparts in the new site.
 */
@Service
public class LegacyIdMapper {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private ProductCategoryReadDao categoryReadDao;
    @Inject
    private ProductReadDao productReadDao;
    @Inject
    private SupplierReadDao supplierReadDao;

    public Long lookupCategoryId(Long legacyId) {
        return categoryReadDao.loadCategoryIdMatchingLegacyId(legacyId);
    }

    public Long lookupProductId(Long legacyId) {
        return productReadDao.loadProductIdMatchingLegacyId(legacyId);
    }

    public String lookupSupplierId(Long legacyId) {
        try {
            return supplierReadDao.loadSupplierIdMatchingLegacyId(legacyId);
        } catch(EmptyResultDataAccessException e){
            LOGGER.info("Failed to find supplier with legacy id " + legacyId);
            
            try{
                return supplierReadDao.loadApproved(legacyId, Locale.ENGLISH).getPublicId();
            } catch(EmptyResultDataAccessException e2){
                throw new LegacyIdNotFoundException("Failed to find supplier with private id " + legacyId);
            }
        }
    }

}
