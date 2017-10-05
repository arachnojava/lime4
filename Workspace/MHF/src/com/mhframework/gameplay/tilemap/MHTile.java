package com.mhframework.gameplay.tilemap;

import com.mhframework.MHRenderable;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHTile implements MHRenderable, MHITileMapContent
{
	public MHBitmapImage image;
	public int id;

	public MHTile(int tileID, MHBitmapImage tileImage) 
	{
		id = tileID;
		image = tileImage;
	}
	

    @Override
    public void render(MHGraphicsCanvas g)
    {
        render(g, 0, 0);
    }

    
    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
        g.drawImage(image, x, y);
    }


    @Override
    public int getTileID()
    {
        return id;
    }


    @Override
    public MHBitmapImage getImage()
    {
        return image;
    }
}
