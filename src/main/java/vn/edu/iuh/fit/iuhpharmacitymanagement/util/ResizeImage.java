package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ResizeImage {

    public static ImageIcon resizeImage(ImageIcon inputIcon, int scaledWidth, int scaledHeight) {
        Image inputImage = inputIcon.getImage();
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return new ImageIcon(outputImage);
    }
}

