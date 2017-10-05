package com.mhframework.platform.pc.graphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mhframework.MHGame;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHPCFont extends MHFont
{
    public Font font;
    private FontMetrics fontMetrics;
    
    public MHPCFont(String fontName)
    {
        super(fontName, MHFont.Type.TTF);
        font = MHPCFont.getFont(fontName);//new Font(fontName, Font.PLAIN, MHFont.DEFAULT_SIZE);
        this.setHeight(DEFAULT_SIZE);
    }

    @Override
    public void drawString(MHGraphicsCanvas g, String text, double x, double y)
    {
        Graphics gph = ((MHPCGraphics) g).getGraphics();
        gph.setFont(font);
        fontMetrics = gph.getFontMetrics(font);
        gph.drawString(text, (int)x, (int)y);
    }

    @Override
    public int stringWidth(String text)
    {
        if (fontMetrics == null)
        {
            MHGraphicsCanvas g = MHGame.getBackBuffer();
            Graphics gph = ((MHPCGraphics) g).getGraphics();
            fontMetrics = gph.getFontMetrics(font);
        }            
            
            return fontMetrics.stringWidth(text);
    }
    
    
    private static Font getFont(String name) 
    {
        Font font = null;

        String fileName = MHFont.FONT_DIR + name;
        
        try 
        {
            InputStream is = new BufferedInputStream(new FileInputStream(fileName));
            //URL url = MHPCFont.class.getResource(fileName);
            //File fontFile = new File(url.toURI());
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont((float)DEFAULT_SIZE);
        } 
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.err.println(fileName + " not loaded.  Using serif font.");
            font = new Font("serif", Font.PLAIN, 12);
        }
        catch (Exception ex) 
        {
          ex.printStackTrace();
          System.err.println(fileName + " not loaded.  Using serif font.");
          font = new Font("serif", Font.PLAIN, 12);
        }
        return font;
    }

    
    @Override
    public MHVector centerOn(MHRectangle r, MHGraphicsCanvas g, String text)
    {
        Graphics2D gph = (Graphics2D)((MHPCGraphics) g).getGraphics();

        fontMetrics = gph.getFontMetrics(font);
        
        // get the FontRenderContext for the Graphics2D context
        final FontRenderContext frc = gph.getFontRenderContext();

        final TextLayout layout = new TextLayout(text, font, frc);

        // get the bounds of the layout
        final Rectangle2D textBounds = layout.getBounds();

        // set the new position
        MHVector p = new MHVector();
        p.setX(r.x + (r.width/2) - (textBounds.getWidth()  / 2));
        p.setY(r.y + ((r.height  + textBounds.getHeight()) / 2));

        return p;
    }

    
    @Override
    public int getHeight()
    {
        return font.getSize();
    }

    
    @Override
    public void setHeight(int fontSize)
    {
        // deriveFont is overloaded to take an int for style and a float for 
        // point size.  Thus the cast.
        font = font.deriveFont((float)fontSize);
    }
    

    @Override
    public MHFont clone()
    {
        return new MHPCFont(this.getFontName());
    }
}
