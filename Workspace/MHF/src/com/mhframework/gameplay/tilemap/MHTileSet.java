package com.mhframework.gameplay.tilemap;

import java.io.File;
import java.util.HashMap;
import com.mhframework.MHGlobalConstants;
import com.mhframework.platform.MHPlatform;
import com.mhframework.resources.MHResourceManager;

public class MHTileSet 
{
	public static final int MAX_TILE_ID = MHGlobalConstants.MAX_TILES_PER_SET - 1;
	public static final int NULL_TILE_ID = MHGlobalConstants.MAX_TILES_PER_SET;

	private static MHTile NULL_TILE;
	private HashMap<Integer, MHTile> tiles; // TODO: Consider using a set for this, if performance allows.
    private HashMap<Integer, MHTile> actors;
	private String tileSetID;
	
	public MHTileSet(String id) 
	{		
		NULL_TILE = new MHTile(NULL_TILE_ID, MHPlatform.createImage(1, 1));
		tiles = new HashMap<Integer, MHTile>();
        actors = new HashMap<Integer, MHTile>();
		tileSetID = id;
		// Load the tile set specified by the ID.
		loadTileSet();
	}
	
	
	public String getTileSetID()
	{
		return tileSetID;
	}

	
	private void loadTileSet()
	{
		//for (int id = 0; id < MHGlobalConstants.MAX_TILES_PER_SET; id++)
			//getTile(id);
	}
	
	
	public MHTile getTile(int tileID)
	{
		if (tileID < 0 || tileID == NULL_TILE_ID)
			return NULL_TILE;
		
		if (tileID >= tiles.size() || tiles.get(tileID) == null)
		{
			String imageID = buildTileImageID(tileSetID, tileID);
			File file = new File(imageID);
			if (!file.exists())
				return NULL_TILE;
			
			MHTile t = new MHTile(tileID, MHResourceManager.getInstance().getImage(imageID));
			tiles.put(tileID, t);
		}
		
		return tiles.get(tileID);
	}
	
	
	public MHTile getActorTile(int actorID)
	{
        if (actorID < 0 || actorID == NULL_TILE_ID)
            return NULL_TILE;
        
        if (actorID >= actors.size() || actors.get(actorID) == null)
        {
            String imageID = buildActorImageID(tileSetID, actorID);
            File file = new File(imageID);
            if (!file.exists())
                return NULL_TILE;
            
            MHTile t = new MHTile(actorID, MHResourceManager.getInstance().getImage(imageID));
            actors.put(actorID, t);
        }
        
        return actors.get(actorID);
	}
	
	
	public static String buildTileImageID(String tileSetName, int id)
	{
		String tileID = MHGlobalConstants.DIR_TILE_SETS + "/" + tileSetName + "/tiles/" + 
				threeDigitFormat(id) + MHGlobalConstants.TILE_IMAGE_FILE_EXTENSION;
		
		return tileID;
	}

	
	public static String buildActorImageID(String tileSetName, int id)
	{
        String tileID = MHGlobalConstants.DIR_TILE_SETS + "/" + tileSetName + "/actors/" + 
                threeDigitFormat(id) + MHGlobalConstants.TILE_IMAGE_FILE_EXTENSION;
        
        return tileID;
	}
	
	private static String threeDigitFormat(int id)
	{
		String result = "";
		
		if (id < 100)
			result += "0";
		if (id < 10)
			result += "0";

		result += id;
		
		return result;
	}


	public int countTiles() 
	{
		int count = 0;
		
		count = tiles.size(); // TODO: Change this when you decide on a data structure.
		
		return count;
	}
	
	
	public int countActors()
	{
	    return actors.size();
	}


	public int getBaseTileWidth() 
	{
		int size = getTile(0).image.getWidth();
		return Math.max(size, 1);
	}
	

	public int getBaseTileHeight() 
	{
		int size = getTile(0).image.getHeight();
		return Math.max(size, 1);
	}
}
