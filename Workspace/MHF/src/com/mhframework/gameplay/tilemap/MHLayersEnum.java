package com.mhframework.gameplay.tilemap;

public enum MHLayersEnum 
{
	FLOOR(0),
	FLOOR_DECALS(1),
	WALLS(2),
	WALL_DECALS(3),
	ACTORS(4);
	
	private int id;
	
	private MHLayersEnum(int layerID)
	{
		id = layerID; 
	}
	
	
	public int getID()
	{
		return id;
	}
}
