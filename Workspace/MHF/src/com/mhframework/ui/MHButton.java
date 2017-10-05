package com.mhframework.ui;

import java.util.ArrayList;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.resources.MHResourceManager;
import com.mhframework.ui.event.MHButtonListener;

/******************************************************************************
 * 
 * @author Michael
 *
 */
public abstract class MHButton extends MHLabel
{
    
    public static enum Type
    {
        TEXT, IMAGE, ICON, ANIMATION;
    }
    
    protected MHCommand command;
    private ArrayList<MHButtonListener> listeners;
	private boolean borderVisible = false;

	
	protected MHButton()
    {
        super("");
    }

	
	protected MHButton(String caption)
	{
		super(caption);
	}

	public boolean isBorderVisible() 
    {
		return borderVisible;
	}


	public void setBorderVisible(boolean b) 
	{
		borderVisible = b;
	}
	
	
	public static MHButton create(String caption)
	{
        return new MHTextButton(caption);
	}
	
	
    /**************************************************************************
     * 
     * @param buttonType
     * @param data
     * @return
     */
    public static MHButton create(Type buttonType, String data)
    {
        switch(buttonType)
        {
            case TEXT:
                return new MHTextButton(data);
            case IMAGE:
                //return new MHImageButton(data);
                break;
            case ICON:
                return new MHIconButton(data);
            case ANIMATION:
                break;
            default:
                break;
        }
        
        return new MHTextButton(data);
    }
    
    
    public void addButtonListener(MHButtonListener listener)
    {
        if (listeners == null)
            listeners = new ArrayList<MHButtonListener>();
        
        listeners.add(listener);
    }
    
    
    /**************************************************************************
     * 
     * @param command
     */
    public void setCommand(MHCommand command)
    {
        this.command = command;
    }
    
    
    /**************************************************************************
     * 
     */
    public void executeCommand()
    {
        if (command != null)
            command.execute();
    }
    
    


    private void dispatchEvent(MHMouseTouchEvent e)
    {
        if (listeners == null)
            return;
        
        for (MHButtonListener l : listeners)
        {
            l.onButtonPressed(this, e);
        }
        
        e.setHandled(true);
    }
    
    
    @Override
    public void onMouseDown(MHMouseTouchEvent e)
    {
        if (!isEnabled()) return;
        
        if (contains(e.getX(), e.getY()))
        {
            e.setHandled(true);
            this.setFocused(true);
        }
        else
        {
            this.setFocused(false);
        }
    }


    @Override
    public void onMouseUp(MHMouseTouchEvent e)
    {
        if (!isEnabled()) return;
        
        if (contains(e.getX(), e.getY()))
        {
            e.setHandled(true);
            this.setFocused(true);
            dispatchEvent(e);
        
            if (command != null)
                command.execute();
        }
        else
        {
            this.setFocused(false);
        }
    }


    @Override
    public void onMouseMoved(MHMouseTouchEvent e)
    {
        if (!isEnabled()) return;
        
        if (contains(e.getX(), e.getY()))
        {
            setFocused(true);
        }
        else
        {
            setFocused(false);
        }
    }

    @Override
    public void onKeyDown(MHKeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onKeyUp(MHKeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    
    /**************************************************************************
     * 
     */
    private static class MHTextButton extends MHButton
    {
        private MHColor highlight, shadow;
        
        public MHTextButton(String caption)
        {
            this.setEnabled(true);
            this.setText(caption);
            this.setAlignment(MHLabel.ALIGN_CENTER);
            this.setBorderVisible(true);
            //MHPlatform.addMouseTouchListener(this);
        }
        
        
        private MHColor getHighlight()
        {
            if (highlight == null)
            {
                highlight = MHPlatform.createColor(255, 255, 255, 140);
                normalColors.borderColor = normalColors.backgroundColor;
            }
            
            return highlight;
        }
        
        
        private MHColor getShadow()
        {
            if (shadow == null)
            {
                shadow = MHPlatform.createColor(0, 0, 0, 140);
                normalColors.borderColor = normalColors.backgroundColor;
            }
            
            return shadow;
        }
        
        
        public void render(MHGraphicsCanvas g)
        {
            super.render(g);
            
            
            if (isBorderVisible())
                drawBorder(g);
            
        }
        
        
        protected void drawBorder(MHGraphicsCanvas g)
        {
            // Draw 3D border.
            int left = (int)(getX());
            int right = left + getWidth();
            int top = (int)(getY());
            int bottom = top + getHeight();
            
            // Draw the shadowed edges.
            g.setColor(getShadow());
            g.drawLine(left, bottom, right, bottom);
            g.drawLine(right, top, right, bottom);
            
            // Draw the highlighted edges.
            g.setColor(getHighlight());
            g.drawLine(left, top, right, top);
            g.drawLine(left, top, left, bottom);
        }

        
    }

    
    
    /**************************************************************************
     * 
     */
    private static class MHIconButton extends MHTextButton
    {
        private MHBitmapImage icon;
        public MHIconButton(String iconFileName)
        {
            super("");
            icon = MHResourceManager.getInstance().getImage(iconFileName);//MHPlatform.createImage(iconFileName);
        }
        
        
        @Override
        public void render(MHGraphicsCanvas g)
        {
            super.drawBackground(g);
            super.drawBorder(g);
            int iconX = getX() + getWidth()/2 - icon.getWidth()/2;
            int iconY = getY() + getHeight()/2 - icon.getHeight()/2;
            g.drawImage(icon, iconX, iconY);
        }
        
        
        
        
    }

}
