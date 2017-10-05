package com.mhframework.ui;

import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHImageScreen extends MHScreen 
{
	private static MHImageScreen instance = new MHImageScreen();
	private MHBitmapImage image;
	
	private MHImageScreen()
	{		
	}
	
	
	public static MHImageScreen getInstance(MHScreen prev, MHBitmapImage img)
	{
		instance.setPreviousScreen(prev);
		instance.image = img;
		
		return instance;
	}
	
	
	@Override
	public void render(MHGraphicsCanvas g) 
	{
		g.fill(MHColor.BLACK);
		
		if (image == null)
		{
			g.setColor(MHColor.WHITE);
			g.drawString("Image not specified.", 10, 20);
		}
		else
		{
			double sw = MHScreenManager.getDisplayWidth();
			double sh = MHScreenManager.getDisplayHeight();
			double iw = image.getWidth();
			double ih = image.getHeight();
			double hpct = sh/ih;
			double wpct = sw/iw;
			double pct = Math.min(hpct, wpct);

			iw *= pct;
			ih *= pct;
			
			int cx = (int)(sw/2 - iw/2);
			int cy = (int)(sh/2 - ih/2);
			
			g.drawImage(image, cx, cy, (int)iw, (int)ih);
		}
	}

	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
		MHScreenManager.getInstance().changeScreen(getPreviousScreen());
	}

	@Override
	public void onKeyUp(MHKeyEvent e) 
	{
		MHScreenManager.getInstance().changeScreen(getPreviousScreen());
	}

}
