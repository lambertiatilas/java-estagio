package br.edu.ucv.estagio.util.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

public class ImageResizer {

    public static byte[] resize(byte[] imageData, Integer width, Integer height) {
    	ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
    	
        try {
        	BufferedImage originalImage = ImageIO.read(bais);   
        	Mode mode = (double) width / (double) height >= (double) originalImage.getWidth() / (double) originalImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
			originalImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, mode, width, height);
			int x = 0;
		    int y = 0;

		    if (mode == Scalr.Mode.FIT_TO_WIDTH) {
		        y = (originalImage.getHeight() - height) / 2;
		    } else if (mode == Scalr.Mode.FIT_TO_HEIGHT) {
		        x = (originalImage.getWidth() - width) / 2;
		    }

		    originalImage = Scalr.crop(originalImage, x, y, width, height);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
        } catch (Exception e) {
            return null;
        }
    }  
}