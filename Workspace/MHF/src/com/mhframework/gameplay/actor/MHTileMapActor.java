package com.mhframework.gameplay.actor;

import com.mhframework.gameplay.tilemap.MHITileMapContent;
import com.mhframework.platform.graphics.MHBitmapImage;

public class MHTileMapActor extends MHActor implements MHITileMapContent
{
    private int tileID;
    
    @Override
    public int getTileID()
    {
        return tileID;
    }

    @Override
    public MHBitmapImage getImage()
    {
        return super.getImage();
    }

    public void setTileID(int id)
    {
        // TODO Auto-generated method stub
        
    }

}
