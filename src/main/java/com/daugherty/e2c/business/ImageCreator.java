package com.daugherty.e2c.business;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.sanselan.ImageReadException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

/**
 * Takes an Input Stream and Creates a Thumbnail Input Stream with a watermark
 */
@Component
public class ImageCreator {

    @Inject
    private ImageReader imageReader;

    public static final String EXPORT_TO_CHINA_WATERMARK = "\u00a9 ExportToChina.com";
    public static final String HUIJINKOU_WATERMARK = "\u00a9 Huijinkou.com";

    public InputStream createThumbnail(InputStream inputStream, String imageEncoding, String watermarkText)
            throws IOException, ImageReadException {
        BufferedImage thumbnail = imageReader.readImage(inputStream, imageEncoding);

        if (thumbnail.getWidth() > 300) {
            thumbnail = Scalr.resize(thumbnail, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, 300, 300);
        }

        thumbnail = addWatermark(thumbnail, watermarkText);

        return getInputStream(thumbnail, imageEncoding);
    }

    public InputStream createWatermark(InputStream inputStream, String imageEncoding, String watermarkText)
            throws IOException, ImageReadException {
        BufferedImage watermark = imageReader.readImage(inputStream, imageEncoding);

        addWatermark(watermark, watermarkText);

        return getInputStream(watermark, imageEncoding);
    }

    private BufferedImage addWatermark(BufferedImage bufferedImage, String watermarkText) throws IOException {
        if (watermarkText != null) {
            Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(alpha);
            g2d.setColor(Color.WHITE);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(watermarkText, g2d);
            g2d.drawString(watermarkText, (bufferedImage.getWidth() - (int) rect.getWidth()) / 2,
                    bufferedImage.getHeight() - (int) rect.getHeight() * 2);
            g2d.dispose();
        }

        return bufferedImage;
    }

    private InputStream getInputStream(BufferedImage bufferedImage, String imageEncoding) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imageEncoding, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
