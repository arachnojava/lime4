import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mhframework.platform.pc.graphics.MHPCImage;


public class FloorTileTransformer
{
    public FloorTileTransformer(String filename, int divisionsX, int divisionsY, int destWidth, int destHeight) throws IOException
    {
        Image originalImage = MHPCImage.loadImage(filename);
        int w = originalImage.getWidth(null)/divisionsX;
        int h = originalImage.getHeight(null)/divisionsY;

        for (int row = 0; row < divisionsY; row++)
            for (int col = 0; col < divisionsX; col++)
            {
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
                bi.getGraphics().drawImage(originalImage, 0, 0, w, h, row*h, col*w, row*h+h, col*w+w, null);
                
                BufferedImage im = transformFloorTile(bi, destWidth, destHeight);
                File output = new File(filename + "_transformed_" + row + "_" + col + ".png");
                ImageIO.write(im, "png", output);
            }
    }
    
    /****************************************************************
    * Implements Ernest Pazera's tile slanting algorithm.
    * 
    * @param image
    * @return
    */
    private BufferedImage transformFloorTile(final BufferedImage image, final int w, final int h)
    {
        final int originalSize = image.getWidth(null);
        //final int bufSize = (int) Math.sqrt(2 * Math.pow(originalSize, 2));  // Pythagorean theorem.
        final BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
        //final Graphics2D g = (Graphics2D) buffer.getGraphics();

        // loop through x texture coords
        for (int tx = 0; tx < originalSize; tx++)
        {
            // loop through y texture coords
            for (int ty = 0; ty < originalSize; ty++)
            {
                int c = image.getRGB(tx, ty);
                int x = w/2 - 2 + tx*2 - ty*2;
                int y = tx + ty;
                System.out.println("----  " + tx +", "+ty + "  ----");
                for (int tempX = x; tempX < (x+4); tempX++)
                {
                    System.out.println("\tWriting pixel " + tempX + ", " + y);
                    buffer.setRGB(tempX, y, c);
                }
            }
            System.out.println();
        }
        
        return buffer;
    }
    
    // Michael Henson's orginal solution:
//   private BufferedImage transformFloorTile(final Image image, final int w, final int h)
//   {
//       final int originalSize = image.getWidth(null);
//       final int bufSize = (int) Math.sqrt(2 * Math.pow(originalSize, 2));  // Pythagorean theorem.
//       final Image buffer = new BufferedImage(bufSize, bufSize, BufferedImage.TYPE_INT_ARGB_PRE);
//       final Graphics2D g = (Graphics2D) buffer.getGraphics();
//
//       // Rotate image.
//       g.rotate(45 * (Math.PI / 180.0), bufSize/2, bufSize/2);
//       final int offset = bufSize/2 - originalSize/2;
//       g.drawImage(image, offset, offset, originalSize, originalSize, null);
//
//       // Scale image.
//       final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
//       final Graphics2D rg = (Graphics2D) result.getGraphics();
//       rg.drawImage(buffer, 0, 0, w, h, null);
//
//       return result;
//   }
   
   
   
   

    public static void main(String[] args)
    {
        if (args.length < 5)
        {
            System.err.println("ERROR:  Please specify input filename, number of horizontal and vertical divisions, and the width and height of the resulting tiles.");
            System.exit(0);
        }
        
        String filename = args[0];
        int divisionsX = Integer.parseInt(args[1]);
        int divisionsY = Integer.parseInt(args[2]);
        int destWidth = Integer.parseInt(args[3]);
        int destHeight = Integer.parseInt(args[4]);
        
        
        try
        {
            FloorTileTransformer t = new FloorTileTransformer(filename, divisionsX, divisionsY, destWidth, destHeight);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
