package com.daugherty.e2c.persistence.document.tutorial;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/spring/fileResourceDao-test-context.xml" })
public class FileResourceDaoTest {

    @Inject
    private FileResourceDao fileResourceDao;

    @Test
    public void englishSupplierTutorial() {
        Resource resource = fileResourceDao.getSupplierTutorial(Locale.ENGLISH);

        assertThat(resource.getFilename(), is("export-tutorial.pdf"));
    }

    @Test
    public void spanishSupplierTutorial() {
        Resource resource = fileResourceDao.getSupplierTutorial(new Locale("es"));

        assertThat(resource.getFilename(), is("export-tutorial-es.pdf"));
    }

    @Test
    public void buyerTutorial() {
        Resource resource = fileResourceDao.getBuyerTutorial(Locale.CHINESE);

        assertThat(resource.getFilename(), is("import-tutorial.pdf"));
    }
}
