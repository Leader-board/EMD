import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class get_grayscale {
    /**
     * Function to import an images and get their grayscale values
     * to be passed into the function at image_data
     * some credits to https://www.tutorialspoint.com/java_dip/grayscale_conversion.htm
     */
    public static int[][] getGrayscale(String imageloc) throws IOException {
        String image1 = imageloc;
        File input = new File(image1);
        BufferedImage image = ImageIO.read(input);
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] grayscalearr = new int[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Color c = new Color(image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() *0.114);
                grayscalearr[i][j] = red + green + blue;
            }
        }
        return grayscalearr;
    }
}
