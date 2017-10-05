package com.mhframework.gameplay;

import com.mhframework.MHGlobalConstants;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.io.MHMapFileInfo;
import com.mhframework.resources.MHResourceManager;


public class MHGameWorldData 
{
	private String mapName;

	protected String[] layerFileNames;
	protected MHTileGrid[] layers;

	private String tileSetID;


	public MHGameWorldData() 
	{
		layers = new MHTileGrid[getNumLayers()];
        layerFileNames = new String[getNumLayers()];
	}


	
/*	
	public void loadFile(String filename)
	{
		mapFileName = filename;
	       info = retrieveMapFileInfo(filename);

	        mapGrid = new MHMapCell[info.height][info.width];

	        MHDataModel.getTileSetManager().loadTileSet(info.tileSetId);

	        for (int layer = 0; layer < MHMapCell.NUM_LAYERS; layer++)
	        {
	            int row = 0, col = 0;

	            // Select the data file for the current layer
	            final String layerFile = chooseLayerFile(layer);

	            try
	            {
	                final RandomAccessFile file = new RandomAccessFile(
	                                layerFile, "r");

	                for (row = 0; row < info.height; row++)
	                {
	                    // Read line of data from file
	                    final String line = file.readLine();

	                    // The data files should be tab-delimited.
	                    final String[] dataRow = line.trim().split("\t");

	                    // dataRow.length and info.width should be equal
	                    // only if every cell is accounted for in the
	                    // data file
	                    // NOTE: This will fail if any layer is larger
	                    // than the floor layer!
	                    // Possible solution: Examine ALL the data files
	                    // when getting the map's metrics.
	                    for (col = 0; col < dataRow.length; col++)
	                    {
	                        int tileID = MHTileSetManager.NULL_TILE_ID;
	                        MHMapCellAddress currentCell = new MHMapCellAddress();
	                        currentCell.row = row;
	                        currentCell.column = col;

	                        // Convert the input tile ID into an integer
	                        try
	                        {
	                            tileID = Integer.parseInt(dataRow[col]);
	                        }
	                        catch (final NumberFormatException nfe)
	                        {
	                            tileID = MHTileSetManager.NULL_TILE_ID;
	                        }

	                        // If there is no map cell at our current
	                        // position in the map, make one.
	                        if (mapGrid[row][col] == null)
	                            mapGrid[row][col] = new MHMapCell();

	                        // Create actor object for current tile
	                        MHActor tile = null;

	                        // 999 indicates a null tile
	                        if (tileID < MHTileSetManager.MHTileSet.MAX_TILES
	                                        && tileID != MHTileSetManager.NULL_TILE_ID)
	                        {
	                            // Instantiate special objects based on
	                            // the layer and tile ID.
	                            tile = objectVendor.getObject(layer, tileID, currentCell);

	                            // If the object vendor returned null,
	                            // we have to make the tile object
	                            // ourselves.
	                            if (tile == null)
	                            {
	                                tile = new MHActor();
	                                tile.setImageGroup(MHDataModel.getTileSetManager().getTileImageGroup(layer));
	                                // For a tile actor, the animation
	                                // sequence is the tile ID since
	                                // that's what determines
	                                // which image is displayed.
	                                tile.setAnimationSequence(tileID);
	                            }
	                        }

	                        // assign tile object to map cell field
	                        mapGrid[row][col].setLayer(layer, tile);

	                    } // for (col...
	                } // for (row...
	            } // try
	            catch (final EOFException eofe)
	            {
	            }
	            catch (final IOException ioe)
	            {
	            }
	        } // for (layer...)
	}
*/


	public boolean isCollidable(int row, int column) 
	{
		// Check collidable layers for obstructions.
		if (row < 0 || column < 0 || row >= getWorldHeight() || column >= getWorldWidth())
			return true;
		
//		if (layerCollidable.getTile(row, column) != null)
//			return true;
		
		return false;
	}

	
	public int getWorldHeight() 
	{
		return layers[0].getNumRows();
	}

	
	public int getWorldWidth() 
	{
		return layers[0].getNumColumns();
	}
	
	
	public int getNumLayers()
	{
		return MHLayersEnum.values().length;
	}
	

	public MHTileGrid getLayer(int layerID) 
	{
		// Validate parameter.
		if (layerID >= 0 && layerID < getNumLayers())
			return layers[layerID];
		
		return null;
	}
	
	
	public String getLayerFileName(MHLayersEnum layer) 
	{
		return getLayerFileName(layer.getID());
	}

	
	public String getLayerFileName(int layerID) 
	{
	    // Validate parameter.
	    if (layerID >= 0 && layerID < getNumLayers())
	    {
//	        if (layerFileNames[layerID] == null)
//	        {
//	            layerFileNames[layerID] = mapFileName+MHLayersEnum.values()[layerID]+MHMapFileInfo.TILE_MAP_FILE_EXTENSION;
//	        }
	        return layerFileNames[layerID];
	    }
	    
	    return null;
	}


	public MHTileGrid getLayer(MHLayersEnum layer) 
	{
	    return getLayer(layer.getID());
	}

	
	public void setTileGrid(int layerID, MHTileGrid tilemap, String name) 
	{
		layers[layerID] = tilemap;
		layerFileNames[layerID] = name;
	}

	
	public void setTileGrid(MHLayersEnum layer, MHTileGrid tileMap, String name) 
	{
		setTileGrid(layer.getID(), tileMap, name);
	}


	public void setTileSetID(String tileSetId) 
	{
		this.tileSetID = tileSetId;
	}
	
	
	public String getTileSetID()
	{
		return tileSetID;
	}


	public int getTileWidth() 
	{
	    return MHResourceManager.getInstance().getTileSet(tileSetID).getBaseTileWidth();
	}


	public int getTileHeight() 
	{
        return MHResourceManager.getInstance().getTileSet(tileSetID).getBaseTileHeight();
	}


	public void setMapName(String name) 
	{
		mapName = name;
	}


	public String getMapFileName() 
	{
	    String filename = mapName;
	    filename = validateFilePath(filename);
	    filename = validateMapFileExtension(filename);
		return filename;
	}

	
	private String validateFilePath(String filename)
	{
	    if (!filename.startsWith(MHGlobalConstants.DIR_DATA+"/"))
	        filename = MHGlobalConstants.DIR_DATA + "/" + filename;

	    return filename;
	}

	
    private String validateMapFileExtension(String filename)
    {
        if (!filename.endsWith(MHMapFileInfo.WORLD_FILE_EXTENSION))
            filename += MHMapFileInfo.WORLD_FILE_EXTENSION;
        
        return filename;
    }


	/**
	 * <ol>
     *   <li>Map name : String</li>
     *   <li>Map rows : int</li>
     *   <li>Map columns : int</li>
     *   <li>Tile width : int</li>
     *   <li>Tile height : int</li>
     *   <li>Tile set ID : String</li>
     *   <li>Floor map file name : String</li>
     *   <li>Floor details file name : String</li>
     *   <li>Wall map file name : String</li>
     *   <li>Wall details file name : String</li>
     *   <li>Actor list file name : String</li>
     * </ol>
     */
    @Override   
    public String toString()
    {
        StringBuffer data = new StringBuffer();
        data.append(mapName + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getWorldHeight() + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getWorldWidth() + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getTileWidth() + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getTileHeight() + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getTileSetID() + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getLayerFileName(MHLayersEnum.FLOOR) + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getLayerFileName(MHLayersEnum.FLOOR_DECALS) + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getLayerFileName(MHLayersEnum.WALLS) + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getLayerFileName(MHLayersEnum.WALL_DECALS) + MHMapFileInfo.MAP_FILE_DELIMITER);
        data.append(getLayerFileName(MHLayersEnum.ACTORS) + "\n");
        
        return data.toString();
    }


    public void setLayerFileName(MHLayersEnum layer, String filename)
    {
        layerFileNames[layer.getID()] = filename;
    }


    public String getMapName()
    {
        return mapName;
    }


}
