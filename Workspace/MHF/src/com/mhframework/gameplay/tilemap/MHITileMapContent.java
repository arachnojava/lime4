package com.mhframework.gameplay.tilemap;

import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public interface MHITileMapContent
{
    public int getTileID();
    public MHBitmapImage getImage();
    public void render(MHGraphicsCanvas g, int x, int y);
}
