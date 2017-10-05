package com.mhframework.platform.pc.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHPCGraphics extends MHGraphicsCanvas
{
    private Image buffer;
    private Graphics g;
    private Color color; // TODO: Do we need Color and Font references here?
    private MHPCFont font;

    
    private MHPCGraphics(int width, int height)
    {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = buffer.getGraphics();
        font = (MHPCFont) MHFont.getDefaultFont();
//        x0 = (int)(MHPlatform.getDisplayOrigin().x);
//        y0 = (int)(MHPlatform.getDisplayOrigin().y);
    }
    
    
    MHPCGraphics(Image image)
    {
        buffer = image;
        g = buffer.getGraphics();
        font = (MHPCFont) MHFont.getDefaultFont();
//        x0 = (int)(MHPlatform.getDisplayOrigin().x);
//        y0 = (int)(MHPlatform.getDisplayOrigin().y);
    }


    public static MHGraphicsCanvas create(int w, int h)
    {
        return new MHPCGraphics(w, h);
    }

    
    @Override
    public void drawString(String text, int x, int y)
    {
//    	x += x0;
//    	y += y0;
        font.drawString(this, text, x, y);
    }
    
    
    public Graphics getGraphics()
    {
        return g;
    }


    @Override
    public void fill(MHColor bgColor)
    {
        setColor(bgColor);
        int w = buffer.getWidth(null);
        int h = buffer.getHeight(null);
        g.fillRect(0, 0, w, h);
    }
    
    
    public void setColor(MHColor color)
    {
        this.color = ((MHPCColor)color).getColor();
        g.setColor(this.color);
    }


    @Override
    public void drawImage(MHBitmapImage image, int x, int y)
    {
        Image img = ((MHPCImage)image).getImage();
//    	x += x0;
//    	y += y0;
        g.drawImage(img, x, y, null);
    }


    public Image getImage()
    {
        return buffer;
    }


    @Override
    public void fillRect(MHRectangle rect)
    {
        fillRect(rect.x, rect.y, rect.width, rect.height);
    }


    @Override
    public void fillRect(int x, int y, int width, int height)
    {
//    	x += x0;
//    	y += y0;
        g.fillRect(x, y, width, height);
    }


    @Override
    public void drawRect(MHRectangle rect)
    {
        drawRect(rect.x, rect.y, rect.width, rect.height);
    }


    @Override
    public void drawRect(int x, int y, int width, int height)
    {
//    	x += x0;
//    	y += y0;
        g.drawRect(x, y, width, height);
    }


    @Override
    public void drawImage(MHBitmapImage image, int x, int y, int w, int h)
    {
        Image img = ((MHPCImage)image).getImage();
//    	x += x0;
//    	y += y0;
        g.drawImage(img, x, y, w, h, null);
    }


    @Override
    public void setFont(MHFont font)
    {
        this.font = (MHPCFont)font;
        g.setFont(this.font.font);
    }
    

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
//    	x1 += x0;
//    	x2 += x0;
//    	y1 += y0;
//    	y2 += y0;
        g.drawLine(x1, y1, x2, y2);
    }
}
