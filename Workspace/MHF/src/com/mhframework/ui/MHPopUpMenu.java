package com.mhframework.ui;

import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHPopUpMenu extends MHScreen
{
	private MHScreen parent;
	private boolean visible = false;
	
	public boolean isVisible() {
		return visible;
	}



	@Override
	public void add(MHGuiComponent c) 
	{
		super.add(c);
		
		if (c instanceof MHButton)
		{
			((MHButton)c).setBorderVisible(false);
		}
	}



	public synchronized void show(MHScreen parentScreen, int x, int y, int w, int h)
	{
		this.parent = parentScreen;
		visible = true;
		
		// Calculate button positions based on requested display location (x, y).

		for (int i = 0; i < getGuiComponents().size(); i++)
		{
			getGuiComponents().get(i).setEnabled(true);
			getGuiComponents().get(i).setPosition(x, y);
			getGuiComponents().get(i).setSize(w, h);
			y += h+1;
		}
		
		MHScreenManager.getInstance().changeScreen(this);
	}
	
	
	public void hide()
	{
		MHGuiComponentList c = getGuiComponents();
		for (int i = 0; i < c.size(); i++)
			c.get(i).setEnabled(false);

		if (visible)
		{
			visible = false;
			MHScreenManager.getInstance().changeScreen(parent);
		}
	}
	

	@Override
	public void render(MHGraphicsCanvas g, int x, int y) 
	{
		this.render(g);
	}
	
	
	@Override
	public void render(MHGraphicsCanvas g)
	{
		parent.render(g);
		super.render(g);
	}

	
	
	@Override
	public void onKeyDown(MHKeyEvent e) 
	{
	}


	@Override
	public void onKeyUp(MHKeyEvent e) 
	{
		// If ESCAPE was pressed, close menu.
		if (e.getKeyCode() == MHPlatform.getKeyCodes().keyEscape())
			hide();
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
		// If mouse is outside menu bounds, close menu.
//		for (int i = 0; i < getGuiComponents().size(); i++)
//			if (getGuiComponents().get(i).contains(e.getX(), e.getY()))
//					return;
			
		super.onMouseUp(e);
		e.setHandled(true);
		hide();
	}

	
	@Override
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		super.onMouseDown(e);
		e.setHandled(true);
	}


//	@Override
//	public void onMouseMoved(MHMouseTouchEvent e) 
//	{
//	}
	
	public void unload()
	{
		hide();
	}
}
