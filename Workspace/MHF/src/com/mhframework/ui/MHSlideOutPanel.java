package com.mhframework.ui;

import com.mhframework.MHRenderable;
import com.mhframework.MHUpdatable;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;


public class MHSlideOutPanel implements MHRenderable, MHUpdatable
{
    public static final int TAB_TOP = 0;
    public static final int TAB_BOTTOM = 1;
    public static final int TAB_LEFT = 2;
    public static final int TAB_RIGHT = 3;
    
    public static final int TAB_THICKNESS = 30;
    
    /** The number of frames it takes for the panel to move between positions. */
    private static final float SPEED_DIVISOR = 10.0f;
   
    private SlideOutState state;
    private int width, height;
    private float x, y;
    private int visibleX, visibleY;
    private int invisibleX, invisibleY;
    private MHGuiComponentList components;
    private MHButton btnTab;
    int tabPosition;
    
    public MHSlideOutPanel(int width, int height)
    {
        components = new MHGuiComponentList();
        state = OnState.getInstance();
        this.width = width;
        this.height = height;
    }

    
    public MHButton createTab(String caption, MHFont font, int position)
    {
        int captionWidth = font.stringWidth(caption)+2;
        int captionHeight = font.getHeight()+2;
        MHBitmapImage icon = MHPlatform.createImage(captionWidth, captionHeight);
        MHGraphicsCanvas g = icon.getGraphicsCanvas();
        
        g.setColor(MHColor.WHITE);
        g.drawString(caption, 0, captionHeight-2);
        g.drawString(caption, 2, captionHeight-2);
        g.drawString(caption, 0, captionHeight);
        g.drawString(caption, 2, captionHeight);
        g.setColor(MHColor.BLACK);
        g.drawString(caption, 1, captionHeight-1);
        if (position == TAB_LEFT)
            icon = icon.rotate(-90.0);
        else if (position == TAB_RIGHT)
            icon = icon.rotate(90.0);
            
        MHResourceManager.getInstance().putImage(caption, icon);
        
        btnTab = MHButton.create(MHButton.Type.ICON, caption);
        tabPosition = position;
        
        return btnTab;
    }
    
    
    public void addComponent(MHGuiComponent c)
    {
        components.add(c);
    }
    
    
    public void showComponents(boolean v)
    {
        if (v)
            components.showAll();
        else
            components.hideAll();
    }
    
    
    public float getHSpeed()
    {
        return Math.abs((visibleX - invisibleX)/SPEED_DIVISOR);
    }


    public float getVSpeed()
    {
        return Math.abs((visibleY - invisibleY)/SPEED_DIVISOR);
    }
    

    public final void setState(SlideOutState state)
    {
        this.state = state;
    }

    public final void setVisibleLocation(int x, int y)
    {
        visibleX = x;
        visibleY = y;
    }
    
    public final void setHiddenLocation(int x, int y)
    {
        invisibleX = x;
        invisibleY = y;
    }

    public final void setVisible(boolean v)
    {
        if (v)
        {
            if (state != OnState.getInstance())
                state = ComingState.getInstance();
        }
        else
        {
            if (state != OffState.getInstance())
                state = GoingState.getInstance();
        }
    }

    
    public boolean isVisible()
    {
        return state.isVisible();
    }

    public float getX()
    {
        return x;
    }


    public void setX(float x)
    {
        this.x = x;
    }


    public float getY()
    {
        return y;
    }


    public void setY(float y)
    {
        this.y = y;
    }


    public int getVisibleX()
    {
        return visibleX;
    }


    public int getVisibleY()
    {
        return visibleY;
    }


    public int getInvisibleX()
    {
        return invisibleX;
    }


    public int getInvisibleY()
    {
        return invisibleY;
    }


    public boolean isInOnPosition()
    {
        float dx = Math.abs(visibleX - x);
        float dy = Math.abs(visibleY - y);
        
        return (dx <= getHSpeed() && dy <= getVSpeed());
    }

    
    public boolean isInOffPosition()
    {
        float dx = Math.abs(invisibleX - x);
        float dy = Math.abs(invisibleY - y);
        
        return (dx <= getHSpeed() && dy <= getVSpeed());
    }


    public int getWidth()
    {
        return width;
    }


    public int getHeight()
    {
        return height;
    }


    public void setHeight(int height)
    {
        this.height = height;
    }


    public void setWidth(int width)
    {
        this.width = width;
    }


    @Override
    public void update(long elapsedTime)
    {
        state.update(this);
        
        int x = (int) (getX()+5);
        int y = (int) (getY()+5);
        int bw = getWidth()-10;
        int bh = (getHeight()-10)/components.size();
        for (int i = 0; i < components.size(); i++)
        {
            components.get(i).setBounds(x, y, bw, bh);
            y += bh+1;
        }
        
        switch (tabPosition)
        {
        case TAB_TOP:
            btnTab.setPosition((int)getX(), (int)getY()-TAB_THICKNESS);
            btnTab.setSize(getWidth(), TAB_THICKNESS);
            break;
        case TAB_BOTTOM:
            btnTab.setPosition((int)getX(), (int)getY()+getHeight());
            btnTab.setSize(getWidth(), TAB_THICKNESS);
            break;
        case TAB_LEFT:
            btnTab.setPosition((int)getX()-TAB_THICKNESS, (int)getY());
            btnTab.setSize(TAB_THICKNESS, getHeight());
            break;
        case TAB_RIGHT:
            btnTab.setPosition((int)getX()+getWidth(), (int)getY());
            btnTab.setSize(TAB_THICKNESS, getHeight());
            break;
        }

    }


    @Override
    public void render(MHGraphicsCanvas g)
    {
        render(g, (int)x, (int)y);
    }


    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
        g.setColor(MHColor.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
    }
}


/******************************************************************************
 * 
 *
 */
interface SlideOutState
{
    public void update(MHSlideOutPanel panel);

    public boolean isVisible();
}


/******************************************************************************
 * 
 *
 */
class OnState implements SlideOutState
{
 private static final SlideOutState instance = new OnState();
    
    private OnState()
    {
    }
    
    
    public static SlideOutState getInstance()
    {
        return instance;
    }


    @Override
    public boolean isVisible()
    {
        return true;
    }

    @Override
    public void update(MHSlideOutPanel panel)
    {
        panel.setX(panel.getVisibleX());
        panel.setY(panel.getVisibleY());
    }
}


/******************************************************************************
 * 
 *
 */
class OffState implements SlideOutState
{
 private static final SlideOutState instance = new OffState();
    
    private OffState()
    {
    }
    
    
    public static SlideOutState getInstance()
    {
        return instance;
    }

    
    @Override
    public void update(MHSlideOutPanel panel)
    {
        panel.setX(panel.getInvisibleX());
        panel.setY(panel.getInvisibleY());
    }
    
    
    @Override
    public boolean isVisible()
    {
        return false;
    }

}


/******************************************************************************
 * 
 *
 */
class ComingState implements SlideOutState
{
    private static final SlideOutState instance = new ComingState();
    
    private ComingState()
    {
    }
    
    
    public static SlideOutState getInstance()
    {
        return instance;
    }

    
    @Override
    public void update(MHSlideOutPanel panel)
    {
        if (panel.isInOnPosition())
            panel.setState(OnState.getInstance());
        else
        {
            float dx = 0; 
            float dy = 0;
            
            if (panel.getVisibleX() > panel.getInvisibleX())
            {
                dx = panel.getHSpeed();
            }
            else if (panel.getVisibleX() < panel.getInvisibleX())
            {
                dx = panel.getHSpeed() * -1;
            }
            else if (panel.getVisibleY() < panel.getInvisibleY())
            {
                dy = panel.getVSpeed() * -1;
            }
            else if (panel.getVisibleY() > panel.getInvisibleY())
            {
                dy = panel.getVSpeed();
            }
            
            panel.setX(panel.getX() + dx);
            panel.setY(panel.getY() + dy);
        }
    }
    @Override
    public boolean isVisible()
    {
        return true;
    }
}


/******************************************************************************
 * 
 *
 */
class GoingState implements SlideOutState
{
    private static final SlideOutState instance = new GoingState();
    
    private GoingState()
    {
    }
    
    
    public static SlideOutState getInstance()
    {
        return instance;
    }

    
    @Override
    public void update(MHSlideOutPanel panel)
    {
        if (panel.isInOffPosition())
            panel.setState(OffState.getInstance());
        else
        {
            float dx = 0; 
            float dy = 0;
            
            if (panel.getVisibleX() > panel.getInvisibleX())
            {
                // Exit to the left.
                dx = panel.getHSpeed() * -1;
            }
            else if (panel.getVisibleX() < panel.getInvisibleX())
            {
                // Exit to the right.
                dx = panel.getHSpeed();
            }
            else if (panel.getVisibleY() < panel.getInvisibleY())
            {
                // Exit to the bottom.
                dy = panel.getVSpeed();
            }
            else if (panel.getVisibleY() > panel.getInvisibleY())
            {
                // Exit to the top.
                dy = panel.getVSpeed() * -1;
            }
            
            panel.setX(panel.getX() + dx);
            panel.setY(panel.getY() + dy);
        }
    }
    @Override
    public boolean isVisible()
    {
        return false;
    }
}


