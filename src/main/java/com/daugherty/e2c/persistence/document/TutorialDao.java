package com.daugherty.e2c.persistence.document;

import java.util.Locale;

import org.springframework.core.io.Resource;

/**
 * Load buyers or suppliers tutorials based on language
 */
public interface TutorialDao {

    public Resource getSupplierTutorial(Locale locale);

    public Resource getBuyerTutorial(Locale locale);
}
