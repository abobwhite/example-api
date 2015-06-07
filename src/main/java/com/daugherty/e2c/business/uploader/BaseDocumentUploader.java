package com.daugherty.e2c.business.uploader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sanselan.ImageReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.ImageCreator;
import com.daugherty.e2c.business.Uploader;
import com.daugherty.e2c.business.UploaderException;
import com.daugherty.e2c.business.ValidationException;
import com.daugherty.e2c.domain.ValidationError;
import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.document.DocumentDao;
import com.daugherty.e2c.persistence.document.DocumentType;

public abstract class BaseDocumentUploader<Entity> implements Uploader {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private DocumentDao documentDao;
    @Inject
    private ImageCreator thumbnailImage;
    @Inject
    private Hashids hashids;

    @Override
    public String uploadDocument(String publicId, String contentType, InputStream inputStream) throws IOException {
        return uploadDocument(publicId, null, contentType, inputStream);
    }

    @Override
    public String uploadDocument(String publicId, String documentName, String contentType, InputStream inputStream)
            throws IOException {
        return uploadDocument(hashids.decode(publicId)[0], documentName, contentType, inputStream);
    }

    @Override
    public String uploadDocument(Long id, String contentType, InputStream inputStream) throws IOException {
        return uploadDocument(id, null, contentType, inputStream);
    }

    @Override
    public String uploadDocument(Long id, String documentName, String contentType, InputStream inputStream)
            throws IOException {
        ValidationError validationError = new ValidationError();

        try {

            Entity entity = loadEntityById(id);

            if (isValid(entity, validationError)) {
                String documentKey = getDocumentType().toString() + id + "-" + UUID.randomUUID().toString();

                ByteArrayOutputStream baos = getByteArrayOutputStream(inputStream);
                InputStream image = new ByteArrayInputStream(baos.toByteArray());

                String newDocumentKey = documentDao.create(documentKey, contentType, image);

                if (isImage(contentType)) {
                    if (DocumentType.SUPPLIER_LOGO.equals(getDocumentType())
                            || DocumentType.BUYER_LOGO.equals(getDocumentType())) {
                        createLogoImages(contentType, baos, newDocumentKey);

                    } else if (DocumentType.PRODUCT_IMAGE.equals(getDocumentType())) {
                        createProductImages(contentType, baos, newDocumentKey);
                    }

                }

                persistNewDocumentKey(entity, documentName, newDocumentKey);

                return newDocumentKey;
            }
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw new UploaderException(e);
        } catch (ImageReadException e) {
            LOG.error(e.getMessage());
            throw new UploaderException(e);
        }

        if (validationError.hasErrors()) {
            throw new ValidationException(validationError);
        }

        return null;
    }

    /**
     * For Supplier & Buyer Logos we create Thumbnails
     */
    private void createLogoImages(String contentType, ByteArrayOutputStream baos, String newDocumentKey)
            throws IOException, ImageReadException {
        InputStream e2cThumbnail = new ByteArrayInputStream(baos.toByteArray());
        createThumbnailImage(contentType, e2cThumbnail, newDocumentKey + DocumentUrlFactory.THUMBNAIL, null);
    }

    /**
     * For Products we create thumbnails with E2C & Huijinkou watermarks
     */
    private void createProductImages(String contentType, ByteArrayOutputStream baos, String newDocumentKey)
            throws IOException, ImageReadException {
        InputStream e2cThumbnail = new ByteArrayInputStream(baos.toByteArray());
        InputStream e2cWatermark = new ByteArrayInputStream(baos.toByteArray());
        InputStream huijinkouThumbnail = new ByteArrayInputStream(baos.toByteArray());
        InputStream huijinkouWatermark = new ByteArrayInputStream(baos.toByteArray());

        createThumbnailImage(contentType, e2cThumbnail, newDocumentKey + DocumentUrlFactory.THUMBNAIL,
                ImageCreator.EXPORT_TO_CHINA_WATERMARK);
        createWatermarkImage(contentType, e2cWatermark, newDocumentKey + DocumentUrlFactory.WATERMARK,
                ImageCreator.EXPORT_TO_CHINA_WATERMARK);

        createThumbnailImage(contentType, huijinkouThumbnail, newDocumentKey + DocumentUrlFactory.HUIJINKOU_THUMBNAIL,
                ImageCreator.HUIJINKOU_WATERMARK);
        createWatermarkImage(contentType, huijinkouWatermark, newDocumentKey + DocumentUrlFactory.HUIJINKOU_WATERMARK,
                ImageCreator.HUIJINKOU_WATERMARK);
    }

    /**
     * Generates a Thumbnail Image. If image type is gif we convert to PNG before resizing
     */
    private void createThumbnailImage(String contentType, InputStream thumbnail, String newDocumentKey,
            String watermarkText) throws IOException, ImageReadException {
        String type = MediaType.IMAGE_GIF_VALUE.equalsIgnoreCase(contentType) ? MediaType.IMAGE_PNG_VALUE : contentType;

        if (MediaType.IMAGE_GIF_VALUE.equalsIgnoreCase(contentType)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(thumbnail), "png", baos);
            thumbnail = new ByteArrayInputStream(baos.toByteArray());
        }

        InputStream createdThumbnail = thumbnailImage.createThumbnail(thumbnail, getImageEncoding(type), watermarkText);

        documentDao.create(newDocumentKey, type, createdThumbnail);
    }

    /**
     * Generates a Watermark Image. Add a watermark to original image and save.
     */
    private void createWatermarkImage(String contentType, InputStream watermark, String newDocumentKey,
            String watermarkText) throws IOException, ImageReadException {
        InputStream createdWatermark = thumbnailImage.createWatermark(watermark, getImageEncoding(contentType),
                watermarkText);
        documentDao.create(newDocumentKey, contentType, createdWatermark);
    }

    private boolean isImage(String contentType) {
        if (StringUtils.isNotBlank(contentType) && contentType.length() > 5
                && contentType.substring(0, 5).equalsIgnoreCase("image")) {

            return true;
        }
        return false;
    }

    private String getImageEncoding(String contentType) {
        String substring = contentType.substring(6);

        return substring;
    }

    private ByteArrayOutputStream getByteArrayOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos;
    }

    protected abstract Entity loadEntityById(Long id);

    protected abstract boolean isValid(Entity entity, ValidationError validationError);

    protected abstract DocumentType getDocumentType();

    protected abstract void persistNewDocumentKey(Entity entity, String docmentName, String documentKey);
}
