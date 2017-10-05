package com.mhframework.ui;

import java.util.ArrayList;

import com.mhframework.MHRenderable;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHGuiComponentList implements MHRenderable, MHKeyListener, MHMouseTouchListener
{
    private ArrayList<MHGuiComponent> components;
    private int selectedIndex = 0;
    
    public MHGuiComponentList()
    {
        components = new ArrayList<MHGuiComponent>();
    }


    public synchronized void update(long elapsedTime)
    {
    	for (int i = 0; i < components.size(); i++)
        {
            components.get(i).update(elapsedTime);
        }
    }

    

    public synchronized void render(MHGraphicsCanvas g)
    {
    	for (int i = 0; i < components.size(); i++)
        {
    		MHGuiComponent c = components.get(i);
            if (c.isVisible())
                c.render(g);
        }
    }

    
    public void add(MHGuiComponent c)
    {
        components.add(c);
    }

    
    public int size()
    {
    	return components.size();
    }
    
    
    public MHGuiComponent nextFocusableComponent()
    {
        if (components.size() == 0)
            return null;
        
        do
        {
            selectedIndex = (selectedIndex + 1) % components.size();
        } while (!components.get(selectedIndex).isFocusable());
        
        return components.get(selectedIndex);
    }
    
    
    public MHGuiComponent previousFocusableComponent()
    {
    	if (components.size() <= 0)
    		return null;
    	
        do
        {
            selectedIndex--;
            if (selectedIndex < 0)
                selectedIndex = components.size()-1;
        } while (!components.get(selectedIndex).isFocusable());
        
        return components.get(selectedIndex);
    }

    
    public MHGuiComponent getSelectedComponent()
    {
        return components.get(selectedIndex);
    }

    
    public void onKeyDown(MHKeyEvent e)
    {
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyDownArrow())
            nextFocusableComponent();
        else if (e.getKeyCode() == MHPlatform.getKeyCodes().keyUpArrow())
            previousFocusableComponent();
    }


    public void onKeyUp(MHKeyEvent e)
    {
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyEnter())
            getSelectedComponent().executeCommand();
    }


    public void onMouseDown(MHMouseTouchEvent e)
    {
        MHGuiComponent c = getComponentAt(e.getX(), e.getY());
        if (c != null)
        {
        	c.onMouseDown(e);
            selectedIndex = getComponentIndex(c);
        }
    }


    public void onMouseUp(MHMouseTouchEvent e)
    {
        MHGuiComponent c = getComponentAt(e.getX(), e.getY());
        if (c != null)
        {
        	c.onMouseUp(e);
            selectedIndex = getComponentIndex(c);
        }
    }


    public void onMouseMoved(MHMouseTouchEvent e)
    {
        for (MHGuiComponent c : components)
        {
        	c.onMouseMoved(e);
            selectedIndex = getComponentIndex(c);
        }
    }


    public void render(MHGraphicsCanvas g, int x, int y)
    {
        for (MHGuiComponent c : components)
            c.render(g, x, y);
    }
    
    
    private int getComponentIndex(MHGuiComponent c)
    {
        for (int i = 0; i < components.size(); i++)
        {
            if (components.get(i) == c)
                return i;
        }
        
        return -1;
    }
    
    
    private MHGuiComponent getComponentAt(int x, int y)
    {
        for (MHGuiComponent c : components)
            if (c.contains(x, y))
                return c;
        
        return null;
    }


	public MHGuiComponent get(int index) 
	{
		return components.get(index);
	}


    public void showAll()
    {
        for (MHGuiComponent c : components)
            c.setVisible(true);
    }


    public void hideAll()
    {
        for (MHGuiComponent c : components)
            c.setVisible(false);
    }


    public void clear()
    {
        components.clear();
    }
}
