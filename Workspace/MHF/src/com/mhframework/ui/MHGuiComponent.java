package com.mhframework.ui;

import com.mhframework.MHRenderable;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


/******************************************************************************
 * 
 * @author Michael
 *
 */
public abstract class MHGuiComponent implements MHRenderable, MHKeyListener, MHMouseTouchListener
{
    protected MHGuiComponentColorSet normalColors;
    protected MHGuiComponentColorSet focusedColors;
    protected MHGuiComponentColorSet disabledColors;
    
    protected MHGuiComponentColorSet currentColors;
    private String tooltip;
    private MHRectangle bounds;
    private boolean enabled = true;
    private boolean focusable = false;
    private boolean visible = true;
    private boolean hasFocus = false;

    
    protected MHGuiComponent()
    {
        normalColors = new MHGuiComponentColorSet();
        normalColors.foregroundColor = MHColor.BLACK;
        normalColors.backgroundColor = MHColor.LIGHT_GRAY;
        normalColors.borderColor = MHColor.BLACK;

        focusedColors = new MHGuiComponentColorSet();
        focusedColors.foregroundColor = MHColor.LIGHT_GRAY;
        focusedColors.backgroundColor = MHColor.BLACK;
        focusedColors.borderColor = MHColor.BLACK;

        disabledColors = new MHGuiComponentColorSet();
        disabledColors.foregroundColor = MHColor.GRAY;
        disabledColors.backgroundColor = MHColor.LIGHT_GRAY;
        disabledColors.borderColor = MHColor.BLACK;

        currentColors = normalColors;
    }
    
    
    public void setColorSet(MHGuiComponentColorSet colorSet)
    {
        currentColors = colorSet;
    }
    
    
    public void setForegroundColor(MHColor color)
    {
        getNormalColors().foregroundColor = color;
    }

    
    public void setBackgroundColor(MHColor color)
    {
        getNormalColors().backgroundColor = color;
    }
    
    
    public void setBorderColor(MHColor color)
    {
        getNormalColors().borderColor = color;
    }
    
    /**************************************************************************
     * Updates the color set of this component depending on the values of its
     * properties.  Subclasses should call this implementation and then
     * proceed to do their own specific updates.
     * 
     * @see com.mhframework.MHRenderable#update(long)
     * 
     * @param elapsedTime Time elapsed in milliseconds since the last
     *                    iteration of the game loop.
     */
    public void update(long elapsedTime)
    {
        if (!enabled)
            currentColors = disabledColors;
        else if (hasFocus)
            currentColors = focusedColors;
        else
            currentColors = normalColors;
    }

    
    public void setNormalColors(MHColor foreground, MHColor background, MHColor border)
    {
        normalColors.foregroundColor = foreground;
        normalColors.backgroundColor = background;
        normalColors.borderColor = border;
    }
    

    /**************************************************************************
     * Render this component onto the sent graphics canvas.  Subclasses
     * should override this.
     * 
     * @see com.mhframework.MHRenderable#render(com.mhframework.platform.graphics.MHGraphicsCanvas)
     */
    @Override
    public void render(MHGraphicsCanvas g)
    {
        drawBackground(g);
        drawForeground(g);
    }

    
    /**************************************************************************
     * Render this component onto the sent graphics canvas.  Subclasses
     * should override this.
     */
    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
    	render(g);
    }


    
    public void drawBackground(MHGraphicsCanvas g)
    {
        g.setColor(currentColors.backgroundColor);
        g.fillRect(getBounds());
    }
    

    public void drawForeground(MHGraphicsCanvas g)
    {
        g.setColor(currentColors.borderColor);
        g.drawRect(getBounds());
        g.setColor(currentColors.foregroundColor);
    }
    

    /**
     * @return the tooltip
     */
    public String getTooltip()
    {
        return tooltip;
    }


    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }


    /**************************************************************************
     * 
     * @return the focusable
     */
    public boolean isFocusable()
    {
        if (!enabled || !visible)
            focusable = false;
        
        return focusable;
    }


    /**************************************************************************
     * @param focusable the focusable to set
     */
    public void setFocusable(boolean focusable)
    {
        if (!enabled || !visible)
            focusable = false;
        
        this.focusable = focusable;
    }


    /**************************************************************************
     * @return the visible
     */
    public boolean isVisible()
    {
        return visible;
    }


    /**************************************************************************
     * @param visible the visible to set
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    /**************************************************************************
     * 
     * @return
     */
    public boolean isEnabled()
    {
        return enabled;
    }


    /**************************************************************************
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }



    /**************************************************************************
     * @return the hasFocus
     */
    public boolean hasFocus()
    {
        if (!enabled)
            hasFocus = false;
        
        return hasFocus;
    }


    /**************************************************************************
     * @param hasFocus the hasFocus to set
     */
    public void setFocused(boolean hasFocus)
    {
        this.hasFocus = hasFocus;
    }


    /**************************************************************************
     * 
     * @return
     */
    public MHRectangle getBounds()
    {
        if (bounds == null)
            bounds = new MHRectangle();
        
        return bounds;
    }

    
    /**************************************************************************
     * 
     * @param bounds
     */
    public void setBounds(MHRectangle bounds)
    {
        this.bounds = bounds;
    }

    
    /**************************************************************************
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setBounds(int x, int y, int width, int height)
    {
        getBounds().setRect(x, y, width, height);
    }
    
    
    public void setPosition(int x, int y)
    {
        getBounds().setRect(x, y, bounds.width, bounds.height);
    }

    
    public void setSize(int width, int height)
    {
        getBounds().setRect(bounds.x, bounds.y, width, height);
    }

    
    public int getX()
    {
        return getBounds().x;
    }

    
    public int getY()
    {
        return getBounds().y;
    }
    
    
    public int getWidth()
    {
        return getBounds().width;
    }
    
    
    public int getHeight()
    {
        return getBounds().height;
    }
    

    /**************************************************************************
     * Empty hook to support optional Command Pattern implementations in
     * subclasses.
     */
    public void executeCommand()
    {
    }


    /**************************************************************************
     * 
     * @param x
     * @param y
     * @return
     */
    public boolean contains(double x, double y)
    {
        return getBounds().contains(x, y);
    }
    
    
    /**************************************************************************
     * 
     * @author Michael
     *
     */
    public static class MHGuiComponentColorSet
    {
        public MHColor backgroundColor;
        public MHColor foregroundColor;
        public MHColor borderColor;
    }


    public MHGuiComponentColorSet getNormalColors()
    {
        return normalColors;
    }


    public MHGuiComponentColorSet getFocusedColors()
    {
        return focusedColors;
    }


    public MHGuiComponentColorSet getDisabledColors()
    {
        return disabledColors;
    }


    public MHGuiComponentColorSet getCurrentColors()
    {
        return currentColors;
    }


}
