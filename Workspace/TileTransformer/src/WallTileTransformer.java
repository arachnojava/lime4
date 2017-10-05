import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import mhframework.media.MHResourceManager;
import mhframework.tilemap.MHIsoMouseMap;


public class WallTileTransformer
{
    static final int UNTRANSFORMED_HEIGHT = MHIsoMouseMap.HEIGHT * 2;
    static final int TRANSFORMED_HEIGHT = MHIsoMouseMap.HEIGHT * 3;
    static final int WIDTH = MHIsoMouseMap.WIDTH / 2;
    
    public WallTileTransformer(String filename) throws IOException
    {
        Image originalImage = MHResourceManager.loadImage(filename);

        BufferedImage left = transformLeftWallTile(originalImage);
        File output = new File(filename + "_transformed_Left.png");
        if (!ImageIO.write(left, "png", output))
            System.out.println("Error writing left-facing wall image.");
        
        BufferedImage right = transformRightWallTile(originalImage);
        output = new File(filename + "_transformed_Right.png");
        if (!ImageIO.write(right, "png", output))
            System.out.println("Error writing right-facing wall image.");
            
        
        BufferedImage corner = createCornerWallTile(right, left);
        output = new File(filename + "_transformed_Corner.png");
        if (!ImageIO.write(corner, "png", output))
            System.out.println("Error writing corner wall image.");
        
    }
    
    
    private BufferedImage createCornerWallTile(BufferedImage r, BufferedImage l)
    {
        BufferedImage result = new BufferedImage(WIDTH*2, TRANSFORMED_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
        result.getGraphics().drawImage(l, 0, 0, null);
        result.getGraphics().drawImage(r, 0, 0, null);
        
        return result;
    }


    private BufferedImage transformLeftWallTile(Image originalImage)
    {
        BufferedImage result = new BufferedImage(2*WIDTH, TRANSFORMED_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
        BufferedImage scaledImage = new BufferedImage(WIDTH, UNTRANSFORMED_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
        scaledImage.getGraphics().drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
        scaledImage = applyShadow(scaledImage);
        int y = MHIsoMouseMap.HEIGHT/2 + 1;
        for (int x = 0; x < scaledImage.getWidth(); x += 2)
        {
            result.getGraphics().drawImage(scaledImage, x, y, x+2, y+scaledImage.getHeight(null), x, 0, x+2, scaledImage.getHeight(null), null);
            y++;
        }
        
        return result;
    }


    private BufferedImage applyShadow(BufferedImage image)
    {
        Color c = Color.BLACK;
        Color shadowColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
        BufferedImage shadowImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = shadowImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.setColor(shadowColor);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        return shadowImage;
    }


    private BufferedImage transformRightWallTile(Image originalImage)
    {
            BufferedImage result = new BufferedImage(2*WIDTH, TRANSFORMED_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
            BufferedImage scaledImage = new BufferedImage(WIDTH, UNTRANSFORMED_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);
            scaledImage.getGraphics().drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
            int y = result.getHeight() - scaledImage.getHeight();// - 1;
            for (int x = result.getWidth()/2; x < result.getWidth(); x += 2)
            {
                int sx1 = x-result.getWidth()/2;
                int sx2 = sx1 + 2;
                result.getGraphics().drawImage(scaledImage, x, y, x+2, y+scaledImage.getHeight(), sx1, 0, sx2, scaledImage.getHeight(), null);
                y--;
            }
            
            return result;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.err.println("ERROR:  Please specify input filename.");
            System.exit(0);
        }
        
        String filename = args[0];
        
        try
        {
            new WallTileTransformer(filename);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
