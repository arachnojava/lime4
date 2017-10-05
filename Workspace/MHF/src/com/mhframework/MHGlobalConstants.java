package com.mhframework;

import com.mhframework.platform.MHPlatform;

public abstract class MHGlobalConstants 
{
	public static final String DIR_ASSETS = "assets";
	public static final String DIR_IMAGES = DIR_ASSETS + "/images";
	public static final String DIR_TILE_SETS = DIR_IMAGES + "/tilesets";
	public static final String DIR_AUDIO = DIR_ASSETS + "/audio";
    public static final String DIR_DATA = DIR_ASSETS + "/data";
	
	public static final short MAX_TILES_PER_SET = 1000;
	public static final String TILE_IMAGE_FILE_EXTENSION = ".png";

	public static final int KEY_DEBUG()
	{
	    return MHPlatform.getKeyCodes().keyF12();
	}

	public static final int KEY_SCREENSHOT()
    {
        return MHPlatform.getKeyCodes().keyF11();
    }
}
