package com.daugherty.e2c.business;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA. User: SHK0723 Date: 10/21/13 Time: 11:02 AM To change this template use File | Settings |
 * File Templates.
 */

/* This is used more as an acceptance test for the ImageCreator */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/image-test-context.xml" })
public class ImageCreatorTest {
    @Autowired
    private ApplicationContext applicationContext;
    List<String> fileNames = new ArrayList<String>();

    ImageCreator creator = new ImageCreator();

    @Before
    public void readFiles() {
        URL resource = applicationContext.getClassLoader().getResource("pics");
        File rootDir = new File(resource.getFile());
        String[] list = rootDir.list(FileFileFilter.FILE);

        fileNames = Arrays.asList(list);
    }

    @Test
    @Ignore
    public void generateThumbNails() throws Exception {
        for (String fileName : fileNames) {
            String newName = "TM-" + fileName;
            Resource resource = applicationContext.getResource("pics///" + fileName);
            assertThat(resource.exists(), equalTo(true));
            InputStream inputStream = creator.createThumbnail(resource.getInputStream(), getImageEncoding(fileName),
                    ImageCreator.EXPORT_TO_CHINA_WATERMARK);
            File newFile = writeToFile(newName, inputStream);
            newFile.deleteOnExit();
        }
    }

    @Test
    @Ignore
    public void generateWatermarks() throws Exception {
        for (String fileName : fileNames) {
            String newName = "WM-" + fileName;
            Resource resource = applicationContext.getResource("pics///" + fileName);
            assertThat(resource.exists(), equalTo(true));
            InputStream inputStream = creator.createWatermark(resource.getInputStream(), getImageEncoding(fileName),
                    ImageCreator.HUIJINKOU_WATERMARK);
            File newFile = writeToFile(newName, inputStream);
            newFile.deleteOnExit();
        }
    }

    private File writeToFile(String name, InputStream inputStream) throws IOException {
        URL resource = applicationContext.getClassLoader().getResource("");
        File newFile = new File(resource.getPath(), name);
        OutputStream os = new FileOutputStream(newFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        os.close();
        return newFile;
    }

    private String getImageEncoding(String fileName) {
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        String substring = contentType.substring(6);
        return substring;
    }

}
