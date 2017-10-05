package com.mhframework.ui;

import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHDialogBox extends MHScreen 
{
    private static final MHColor BACKGROUND_COLOR = MHPlatform.createColor(0, 0, 0, 180);
    public static final MHColor SHADOW_COLOR = MHPlatform.createColor(0, 0, 0, 128);
    protected int boxWidth, boxHeight;
    protected int preferredWidth;
    private int lineSpacing; 
    private MHBitmapImage backgroundImage;
    private boolean tileBackground = false;
    private String text, title;
    protected MHFont textFont, titleFont;
    private MHRectangle titleBounds;

    /*
     * Border images are assumed to be in this order:
     * 0 1 2
     * 3 4 5
     * 6 7 8
     */
    private MHBitmapImage[] border = null;
    
       
    public MHDialogBox(MHScreen parentScreen, String text)
    {
    	this(parentScreen, text, MHFont.getDefaultFont());
    }
    
    
    public MHDialogBox(MHScreen parentScreen, String text, String titleText)
    {
    	this(parentScreen, text, titleText, MHFont.getDefaultFont(), MHFont.getDefaultFont());
    }
    
    
    public MHDialogBox(MHScreen parentScreen, String text, MHFont font)
    {
        setPreviousScreen(parentScreen);
        this.text = text;
        textFont = font;
        load();
        createBackgroundImage();
        calculateBoxDimensions();
    }
    
    
    public MHDialogBox(MHScreen parentScreen, String text, String title, MHFont font, MHFont titleFont)
    {
        setPreviousScreen(parentScreen);
        this.text = text;
        this.title = title;
        textFont = font;
        this.titleFont = titleFont;
        load();
        createBackgroundImage();
        calculateBoxDimensions();
    }
    
    
   
    public void setText(String text)
    {
        this.text = text;
        calculateBoxDimensions();
    }

    
    public void setTitle(String title)
    {
        this.title = title;
        calculateBoxDimensions();
    }

    
    public void setFont(MHFont font)
    {
        textFont = font;
        calculateBoxDimensions();
    }
    
    
    public void setTitleFont(MHFont font)
    {
        titleFont = font;
        calculateBoxDimensions();
    }
    
    
    /****************************************************************
     * Draw the dialog box.  This method is designed to support the
     * Template Method design pattern.  Subclasses implementing
     * specialized dialog boxes can override methods that are called
     * in the following order.  Alternatively, these methods can be
     * called independently as well.
     * <ol>
     *     <li>calculateBoxDimensions</li>
     *     <li>drawShadow</li>
     *     <li>drawBorder</li>
     *     <li>drawTitle</li>
     *     <li>drawText</li>
     * </ol>
     */
    public void render(MHGraphicsCanvas g)
    {
        calculateBoxDimensions();
        
        if (tileBackground)
            tileImage(g, backgroundImage, 0, 0);
        else
            getPreviousScreen().render(g);
        
        drawShadow(g);
        
        drawBorder(g);
        
        drawTitle(g);
        
        drawText(g);
        
        super.render(g);
    }

    
    
    private void drawShadow(MHGraphicsCanvas g)
    {
        final int SHADOW_DISTANCE = 20;
        g.setColor(SHADOW_COLOR);
        int x = getTitleBounds().x + SHADOW_DISTANCE;
        int y = getTitleBounds().y + SHADOW_DISTANCE;

        g.fillRect(x, y, boxWidth, boxHeight);
    }


    private void drawTitle(MHGraphicsCanvas g)
    {
//        int x = (int)getTitleBounds().getX();
//        int y = (int)getTitleBounds().getY();
//        int w = (int)getTitleBounds().getWidth();
//        int h = (int)getTitleBounds().getHeight();
//        g.setColor(Color.WHITE);
//        g.drawRect(x, y, w, h);

        if (title != null && title.trim().length() > 0)
        {
            MHVector p;
            if (titleFont != null)
            {
                p = titleFont.centerOn(getTitleBounds(), g, title);  //(g, title, x0, y0);
                titleFont.drawString(g, title, (int)p.getX(), (int)p.getY());
            }
            else
            {
                p = textFont.centerOn(getTitleBounds(), g, title);
                textFont.drawString(g, title, (int)p.getX(), (int)p.getY());
            }
        }
    }


    protected void calculateBoxDimensions()
    {
        lineSpacing = (int)(textFont.getHeight() * 1.1);
        boxHeight = calculateBoxHeight();
        boxWidth = calculateBoxWidth();

        while (boxHeight > MHScreenManager.getDisplayHeight() & boxWidth < MHScreenManager.getDisplayHeight())
            preferredWidth = (int)(preferredWidth * 1.1);
    }
    
    
    protected int calculateBoxHeight()
    {
        boxHeight = 5;
        if (border != null)
            boxHeight += border[1].getHeight() + border[7].getHeight();
        
        if (title != null && title.trim().length() > 0)
        {
            if (titleFont != null)
                boxHeight += titleFont.getHeight() + 5;
            else
                boxHeight += textFont.getHeight() + 5;
            
            int x = getTitleBounds().x;
            int y = getTitleBounds().y;
            int w = getTitleBounds().width;
            int h = boxHeight;
            getTitleBounds().setRect(x, y, w, h);
        }
            
        
        int numLines = Math.max(1, textFont.splitLines(text, preferredWidth).length);
        boxHeight += numLines * lineSpacing + 5;

        return boxHeight;
    }

    
    protected MHRectangle getTitleBounds()
    {
        if (titleBounds == null)
        {
            titleBounds = new MHRectangle();
            int x = MHScreenManager.getDisplayWidth()/2 - calculateBoxWidth()/2;
            int y = MHScreenManager.getDisplayHeight()/2 - calculateBoxHeight()/2;
            int w = calculateBoxWidth();
            int h = this.titleFont.getHeight();
            titleBounds.setRect(x, y, w, h);
        }        
        
        return titleBounds;
    }
    
    protected int calculateBoxWidth()
    {
        boxWidth = 5;
        if (border != null)
            boxWidth += border[3].getWidth() + border[5].getWidth();
        
        String[] lines = textFont.splitLines(text, preferredWidth);
        int longest = preferredWidth;
        for (int i = 0; i < lines.length; i++)
            longest = Math.max(longest, textFont.stringWidth(lines[i]));
        
        boxWidth += longest;
        
        int x = getTitleBounds().x;
        int y = getTitleBounds().y;
        int w = boxWidth;
        int h = getTitleBounds().height;
        getTitleBounds().setRect(x, y, w, h);

        
        return boxWidth;
    }
    
    
    public void setBackgroundImage(MHBitmapImage img, boolean tiled)
    {
        backgroundImage = img;
        tileBackground = tiled;
    }

    
    public void setBorderImages(MHBitmapImage[] images)
    {
        border = images;
        calculateBoxDimensions();
    }
    

    @Override
    public void load()
    {

        preferredWidth = MHScreenManager.getDisplayWidth() / 2;
    }
    

    @Override
    public void unload()
    {
    }
    
    
    private void drawText(MHGraphicsCanvas g)
    {
        int x0 = (int)getTitleBounds().x;
        int y0 = (int)getTitleBounds().y;
        
        if (titleBounds != null)
            y0 += titleBounds.height;
        
        if (border == null)
        {
            x0 += 5;
            y0 += textFont.getHeight();
        }
        else
        {
            x0 += border[0].getWidth() + 5;
            y0 += border[0].getHeight() + textFont.getHeight();
        }
        
        String[] lines = textFont.splitLines(text, preferredWidth);
        for (int line = 0; line < lines.length; line++)
        {
            int y = y0 + (line*lineSpacing);
            textFont.drawString(g, lines[line], x0, y);
        }
        
    }
    
    
    private void drawBorder(MHGraphicsCanvas g)
    {
        int x0 = getTitleBounds().x;
        int y0 = getTitleBounds().y;
        
        if (border == null)
        {
            g.setColor(MHColor.BLACK);
            g.fillRect(x0, y0, boxWidth, boxHeight);
            g.setColor(MHColor.LIGHT_GRAY);
            g.drawRect(x0, y0, boxWidth, boxHeight);
            return;
        }

        // draw center fill
        for (int x = x0; x < boxWidth-border[4].getWidth(); x+=border[4].getWidth())
            for (int y = y0; y < boxHeight-border[4].getHeight(); y+=border[4].getHeight())
                g.drawImage(border[4], x, y);
        
        // draw top and bottom edges
        for (int x = x0 + border[0].getWidth(); x < boxWidth - border[2].getWidth(); x+=border[1].getWidth())
        {
            g.drawImage(border[1], x, y0);
            g.drawImage(border[7], x, y0 + boxHeight-border[7].getHeight());
        }

        // draw left and right edges
        for (int y = y0 + border[0].getHeight(); y < boxHeight - border[6].getHeight(); y+=border[3].getHeight())
        {
            g.drawImage(border[3], x0, y);
            g.drawImage(border[5], x0 + boxWidth-border[5].getWidth(),  y);
        }

        // draw corners
        g.drawImage(border[0], x0, y0);
        g.drawImage(border[2], x0 + boxWidth-border[2].getWidth(), y0);
        g.drawImage(border[6], x0, y0 + boxHeight-border[6].getHeight());
        g.drawImage(border[8], x0 + boxWidth-border[8].getWidth(), y0 + boxHeight-border[8].getHeight());
    }
    
    
    private void createBackgroundImage()
    {
        backgroundImage = MHPlatform.createImage(MHScreenManager.getDisplayWidth(), MHScreenManager.getDisplayHeight());
        MHGraphicsCanvas bg = backgroundImage.getGraphicsCanvas();
        getPreviousScreen().render(bg);
        bg.setColor(BACKGROUND_COLOR);
        bg.fillRect(0, 0, MHScreenManager.getDisplayWidth(), MHScreenManager.getDisplayHeight());
    }


    @Override
    public void onKeyUp(MHKeyEvent e)
    {
        MHScreenManager.getInstance().changeScreen(getPreviousScreen());
    }


    @Override
    public void onMouseUp(MHMouseTouchEvent e)
    {
        MHScreenManager.getInstance().changeScreen(getPreviousScreen());
    }
   
}
