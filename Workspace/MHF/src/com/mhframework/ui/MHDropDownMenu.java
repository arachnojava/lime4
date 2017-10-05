package com.mhframework.ui;

import com.mhframework.MHScreen;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.event.MHButtonListener;


public class MHDropDownMenu extends MHGuiComponent implements MHButtonListener
{
	private MHButton button;
	private MHPopUpMenu menu; // The menu that pops up when this menu is invoked.
	private MHScreen screen;  // The parent screen that the menu is on.
	private boolean menuShown = false;
	
	
	/****************************************************************
	 * 
	 * @param caption
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param parentScreen
	 */
	public MHDropDownMenu(String caption, int x, int y, int w, int h, MHScreen parentScreen)
	{
		button = MHButton.create(MHButton.Type.TEXT, caption);
		screen = parentScreen;
		setBounds(x, y, w, h);
		button.setBounds(this.getBounds());
		button.addButtonListener(this);
	}
	
	
	
	
	@Override
	public void render(MHGraphicsCanvas g) 
	{
		button.render(g);
	}




	public void hide()
	{
		menuShown = false;
		menu.hide();
	}
	
	
    /****************************************************************
     * 
     * @param elapsedTime
     */
 	@Override
	public void update(long elapsedTime) 
	{
		if (menuShown)
		{
			menuShown = false;
			int w = getWidth();
			int h = getHeight();
			int x = getX();
			int y = getY() + h + 2;

			menu.show(screen, x, y, w, h);
		}
		super.update(elapsedTime);
	}


 	/****************************************************************
 	 * 
 	 */
	public void addMenuItem(MHButton item)
	{
		if (menu == null)
			menu = new MHPopUpMenu();
		
		menu.add(item);
	}


 	/****************************************************************
 	 * 
 	 */
	@Override
	public void onButtonPressed(MHButton button, MHMouseTouchEvent e) 
	{
		if (button != this.button)
			return;

		if (!menuShown)
			menuShown = true;
	}


	@Override
	public void onKeyDown(MHKeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onKeyUp(MHKeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		button.onMouseDown(e);
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
		button.onMouseUp(e);
	}


	@Override
	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		button.onMouseMoved(e);
	}
}
