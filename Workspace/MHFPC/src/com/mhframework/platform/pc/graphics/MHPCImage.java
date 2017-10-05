package com.mhframework.platform.pc.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHPCImage extends MHBitmapImage
{
    private Image image;
    private MHPCGraphics graphics;

    
    private MHPCImage(String filename)
    {
        image = loadImage(filename);
        graphics = new MHPCGraphics(image);
        setSize(image.getWidth(null), image.getHeight(null));
        id = filename;
    }
    
    
    public MHPCImage(int width, int height)
    {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = new MHPCGraphics(image);
        setSize(width, height);
        id = "" + autoID++;
    }


    public static MHBitmapImage create(int width, int height)
    {
        return new MHPCImage(width, height);
    }

    
    public static MHBitmapImage create(String filename)
    {
        return new MHPCImage(filename);
    }

    
    public Image getImage()
    {
        return image;
    }

    
    @Override
    public MHGraphicsCanvas getGraphicsCanvas()
    {
        return graphics;
    }


	@Override
	public void savePNG(String filename) 
	{
		if (!filename.toLowerCase().endsWith(".png"))
		{
			filename += ".png";
		}
		
		BufferedImage im = (BufferedImage)image;
        File output = new File(filename);
        try 
        {
			ImageIO.write(im, "png", output);
		} 
        catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    @Override
    public MHBitmapImage rotate(double degrees)
    {
        int size = Math.max(getWidth(), getHeight());
        int center = size/2;
        
        MHBitmapImage result = MHPlatform.createImage(size, size);
        Graphics2D g = (Graphics2D)((MHPCGraphics)result.getGraphicsCanvas()).getGraphics();
        g.rotate(degrees * (Math.PI / 180.0), center, center);
        g.drawImage(image, center-getWidth()/2, center-getHeight()/2, null);
        return result;
    }


    @Override
    public void redimension(int width, int height)
    {
        Image newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(image, 0, 0, null);
        image = newImage;
        graphics = new MHPCGraphics(image);
        setSize(width, height);
    }


    public static Image loadImage(String filename)
    {
        Image img = null;
        try
        {
            // The following line only works for images not in a JAR file.
            img = ImageIO.read(new File(filename));
        }
        catch (final IOException ioe)
        {
            System.err.println("ERROR:  File " + filename + " not found.");
            // The following line only works for images in MHFramework.
//            URL url = this.getClass().getResource(filename);
//            image = Toolkit.getDefaultToolkit().createImage(url);
        }
        catch (final Exception e)
        {
        }
        
        return img;
    }
}
