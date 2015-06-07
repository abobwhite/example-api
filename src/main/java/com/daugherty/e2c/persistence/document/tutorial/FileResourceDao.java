package com.daugherty.e2c.persistence.document.tutorial;

import java.util.Locale;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.daugherty.e2c.persistence.document.TutorialDao;

/**
 * Returns File System Resources
 */
@Repository
public class FileResourceDao implements ResourceLoaderAware, TutorialDao {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource getSupplierTutorial(Locale locale) {
        return getFile(Locale.ENGLISH.equals(locale) ? "export-tutorial.pdf" : "export-tutorial-es.pdf");

    }

    @Override
    public Resource getBuyerTutorial(Locale locale) {
        return getFile("import-tutorial.pdf");
    }

    private Resource getFile(String resourceFilePath) {
        return resourceLoader.getResource("classpath:tutorial/" + resourceFilePath);
    }

}
