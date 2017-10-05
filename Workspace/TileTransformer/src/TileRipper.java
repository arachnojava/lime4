import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mhframework.platform.pc.graphics.MHPCImage;


public class TileRipper
{
    public TileRipper(String filename, int divisionsX, int divisionsY, int startID) throws IOException
    {
        Image originalImage = MHPCImage.loadImage(filename);
        int w = originalImage.getWidth(null)/divisionsX;
        int h = originalImage.getHeight(null)/divisionsY;

        int tileID = startID;
        for (int row = 0; row < divisionsY; row++)
            for (int col = 0; col < divisionsX; col++)
            {
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
                bi.getGraphics().drawImage(originalImage, 0, 0, w, h, col*w, row*h, col*w+w, row*h+h, null);
                
                File output = new File("output/"+threeDigits(tileID) + ".png");
                ImageIO.write(bi, "png", output);
                
                tileID++;
            }
    }
    
    
    private String threeDigits(int i)
    {
        String d = "" + i;
        if (i < 100)
            d = "0" + d;
        if (i < 10)
            d = "0" + d;
        
        return d;
    }

    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("ERROR:  Please specify input filename and the number of horizontal and vertical divisions.");
            System.exit(0);
        }
        
        String filename = args[0];
        int divisionsX = Integer.parseInt(args[1]);
        int divisionsY = Integer.parseInt(args[2]);
        int tileID = Integer.parseInt(args[3]);
        
        try
        {
            TileRipper t = new TileRipper(filename, divisionsX, divisionsY, tileID);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
