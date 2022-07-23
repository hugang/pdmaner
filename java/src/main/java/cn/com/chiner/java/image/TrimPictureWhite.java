package cn.com.chiner.java.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 裁切图片空白
 */
public class TrimPictureWhite {
    private BufferedImage img;

    public TrimPictureWhite(InputStream input) {
        try {
            img = ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException("Problem reading image", e);
        }
    }

    public void trim() {
        int width = getTrimmedWidth();
        int height = getTrimmedHeight();

        BufferedImage newImg = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.createGraphics();
        g.drawImage(img, 0, 0, null);
        img = newImg;
    }

    public void write(String ext,OutputStream out) {
        try {
            ImageIO.write(img, ext, out);
        } catch (IOException e) {
            throw new RuntimeException("Problem writing image", e);
        }
    }

    private int getTrimmedWidth() {
        int height = this.img.getHeight();
        int width = this.img.getWidth();
        int trimmedWidth = 0;

        for (int i = 0; i < height; i++) {
            for (int j = width - 1; j >= 0; j--) {
                if (img.getRGB(j, i) != Color.WHITE.getRGB() && j > trimmedWidth) {
                    trimmedWidth = j;
                    break;
                }
            }
        }
        return trimmedWidth;
    }

    private int getTrimmedHeight() {
        int width = this.img.getWidth();
        int height = this.img.getHeight();
        int trimmedHeight = 0;

        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {
                if (img.getRGB(i, j) != Color.WHITE.getRGB()
                        && j > trimmedHeight) {
                    trimmedHeight = j;
                    break;
                }
            }
        }

        return trimmedHeight;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TrimPictureWhite trimPicture = new TrimPictureWhite(new FileInputStream("/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/images/smis/TableRelations.png"));
        trimPicture.trim();
        trimPicture.write("png",new FileOutputStream("/Users/asher/workspace/ws-vekai/siner-java/src/test/resources/images/smis/TableRelations-1.png"));
    }
}