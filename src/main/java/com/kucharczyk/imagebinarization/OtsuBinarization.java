package com.kucharczyk.imagebinarization;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class OtsuBinarization {
    public static void writeImage(String imageName, BufferedImage img) throws IOException {
        File imgFile = new File(imageName + ".png");
        ImageIO.write(img, "png", imgFile);
    }
    
    public static BufferedImage readImage(String imagePath, BufferedImage img) throws IOException {
        File imgFile = new File(imagePath);
        img = ImageIO.read(imgFile);
        return img;
    }

    public static void toGrayscale(BufferedImage img) {    
          for (int x = 0; x < img.getWidth() ; x++) {
            for (int y = 0; y < img.getHeight(); y++) {

                int rgb = img.getRGB(x,y);
                int alpha = (rgb>>24) & 0xff; // int alpha = new Color(originalImage.getRGB(x, y)).getAlpha();
                int red = (rgb>>16) & 0xff;
                int green = (rgb>>8) & 0xff;
                int blue = rgb & 0xff;
                int avg = (red + green + blue) / 3;

                rgb = (alpha<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x, y, rgb);
            }
        }
    }
  
    public static int[] createHistogram(BufferedImage img) {    
        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
        
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgb = img.getRGB(x,y) & 0xff;
                histogram[rgb]++;
            }
        }

        return histogram;
    }
    
    public static int getOtsuThreshold(BufferedImage img) {    
        int[] histogram = createHistogram(img);
        int pixelsNumber = img.getWidth() * img.getHeight();
        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }
        
        float backgroundSum = 0;
        int backgroundWeight = 0;
        int foregroundWeight = 0;
        float maximumVariance = 0;
        int threshold = 0;
         
        for (int i = 0; i < 256; i++) {
            
            backgroundWeight += histogram[i];
            if (backgroundWeight == 0) continue;
            foregroundWeight = pixelsNumber - backgroundWeight;
            
            if (foregroundWeight == 0) break;
            
            backgroundSum += (float) (i * histogram[i]);
            float backgroundMean = backgroundSum / backgroundWeight;
            float foregroundMean = (sum - backgroundSum) / foregroundWeight;
            
            float betweenClassVariance = (float) backgroundWeight * (float) foregroundWeight * (backgroundMean - foregroundMean) * (backgroundMean - foregroundMean);
        
            if (betweenClassVariance > maximumVariance) {
                maximumVariance = betweenClassVariance;
                threshold = i;
            }
         }
        return threshold;
    }
    
    public static void toBinary(BufferedImage img) {     
        int threshold = getOtsuThreshold(img);
        Color white = new Color(255,255,255);
        Color black = new Color(0,0,0);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {        
                int rgb = img.getRGB(x,y)&0xff;
                if (rgb < threshold) {
                    img.setRGB(x, y, black.getRGB());
                }
                else
                    img.setRGB(x, y, white.getRGB());
            }
        }
    }
}
