package com.mhframework.gameplay.tilemap.io;

import java.io.File;
import com.mhframework.MHGlobalConstants;
import com.mhframework.core.io.MHTextFile;
import com.mhframework.core.io.MHTextFile.Mode;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHActorList;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.MHTileSet;

public abstract class MHWorldIO 
{
	/****************************************************************
	 * Loads a basic tile map from a file using the given tile set.
	 * This method assumes that the tile map contains tiles only, and
	 * not actors to be spawned.
	 * 
	 * TODO:  Overload this with a version that uses MHActorFactory.
	 * 
	 * @param filename The name of the file to load.
	 * @param tileset The tile set from which to pull tiles.
	 * 
	 * @return A tile grid containing the instantiated data from the
	 *         file.
	 */
	public static MHTileGrid loadTileMap(String filename, MHTileSet tileset)
	{
	       if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
	            filename = MHGlobalConstants.DIR_DATA+"/"+filename;

		MHTextFile mapFile = new MHTextFile(filename, Mode.READ);
		String[] data = mapFile.readLine().split(MHMapFileInfo.MAP_FILE_DELIMITER);
		int rows = Integer.parseInt(data[0]);
		int columns = Integer.parseInt(data[1]);
		MHTileGrid grid = new MHTileGrid(rows, columns);
		//int averageTileSize = 0;

		int i = 2;
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < columns; c++)
			{
				int tileID = Integer.parseInt(data[i++]);
				MHTile tile = tileset.getTile(tileID);
				grid.putTile(tile, r, c);

				//averageTileSize += tile.image.getWidth();
			}
		}

		//averageTileSize /= data.length;
		//grid.setTileSize(grid.getTile(0, 0).image.getWidth());

		return grid;
	}

	
    public static void saveTileMap(String filename, MHTileGrid map)
	{
        if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
            filename = MHGlobalConstants.DIR_DATA+"/"+filename;

        MHTextFile file = new MHTextFile(filename, Mode.REWRITE);
        file.append(map.toString());
        file.close();
	}
    
    
    public static MHTileGrid loadActors(String filename, MHTileSet tileset)
    {
        if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
             filename = MHGlobalConstants.DIR_DATA+"/"+filename;

     MHTextFile mapFile = new MHTextFile(filename, Mode.READ);
     String[] data = mapFile.readLine().split(MHMapFileInfo.MAP_FILE_DELIMITER);
     int rows = Integer.parseInt(data[0]);
     int columns = Integer.parseInt(data[1]);
     MHTileGrid grid = new MHTileGrid(rows, columns);

     int i = 2;
     for (int r = 0; r < rows; r++)
     {
         for (int c = 0; c < columns; c++)
         {
             int tileID = Integer.parseInt(data[i++]);
             MHTile a = tileset.getActorTile(tileID);
             grid.putTile(a, r, c);

             //averageTileSize += tile.image.getWidth();
         }
     }

     //averageTileSize /= data.length;
     //grid.setTileSize(grid.getTile(0, 0).image.getWidth());

     return grid;
 }
//    {
//        MHTileGrid list = new MHTileGrid();
//
//        if (filename == null)
//            return list;
//        
//        if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
//            filename = MHGlobalConstants.DIR_DATA+"/"+filename;
//        
//        // If the file doesn't exist, just give back an empty list.
//        File f = new File(filename);
//        if (!f.exists())
//            return list;
//        
//        MHTextFile file = new MHTextFile(filename, Mode.READ);
//
//        // Read file contents
//        String raw = file.readLine();
//        if (raw == null || raw.length() == 0)
//            return list;
//        
//        String[] data = raw.split(MHMapFileInfo.MAP_FILE_DELIMITER);
//
//        // Close file
//        file.close();
//        
//        
//        for (int i = 0; i < data.length; i += 3)
//        {
//            MHActor actor = new MHActor(data[i]);
//            //actor.setImageID(data[i]);
//            int x = Integer.parseInt(data[i+1]);
//            int y = Integer.parseInt(data[i+2]);
//            actor.setPosition(x, y);
//            
//            list.add(actor);
//        }
//
//        return list;
//    }
    
    

	/****************************************************************
	 * Loads a game world from the metadata contained in a .LIME 
	 * file.  The expected type and order of each property is as
	 * follows:
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
     *   <li>Actor file name : String</li>
	 * </ol>
	 * 
	 * @param filename The name of the .LIME file whose contents to
	 *                 load.
	 * 
	 * @return A game world data object containing the game world
	 *         described by the metadata in the file.
	 */
	public static MHGameWorldData loadGameWorld(String filename)
	{
	       if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
	            filename = MHGlobalConstants.DIR_DATA+"/"+filename;

		// FIXME:  MHMapFileInfo might be completely useless now.  Investigate that.
		MHMapFileInfo info = new MHMapFileInfo();

		info.fileName = filename;

		// Open file
		MHTextFile file = new MHTextFile(filename, Mode.READ);

		// Read file contents
		String raw = file.readLine();
		String[] data = raw.split(MHMapFileInfo.MAP_FILE_DELIMITER);

		// Close file
		file.close();

		// Get map dimensions.
		// FIXME:  The map dimensions are determined by the base layer.  Not needed in the metadata.
		String mapName = data[MHMapFileInfo.ELEM_MAP_NAME];
        info.height = Integer.parseInt(data[MHMapFileInfo.ELEM_MAP_ROWS]);
		info.width = Integer.parseInt(data[MHMapFileInfo.ELEM_MAP_COLUMNS]);

		// Get tile dimensions.
		info.tileWidth = Integer.parseInt(data[MHMapFileInfo.ELEM_TILE_WIDTH]);
		info.tileHeight = Integer.parseInt(data[MHMapFileInfo.ELEM_TILE_HEIGHT]);
		
		// Get tile set ID.
		info.tileSetId = data[MHMapFileInfo.ELEM_TILE_SET_ID];

		// Get floorFile
		info.floorFile = data[MHMapFileInfo.ELEM_FLOOR_FILE];

		// Get floorDetailFile
		info.floorDetailFile = data[MHMapFileInfo.ELEM_FLOOR_DETAIL_FILE];

		// Get wallFile
		info.wallFile = data[MHMapFileInfo.ELEM_WALL_FILE];

		// Get wallDetailFile
		info.wallDetailFile = data[MHMapFileInfo.ELEM_WALL_DETAIL_FILE];
		
		// Get actorFile
		info.actorFile = data[MHMapFileInfo.ELEM_ACTOR_FILE];

		// Load the data from those file names we just read.
		MHTileSet tileset = new MHTileSet(info.tileSetId);
		MHTileGrid floor = loadTileMap(info.floorFile, tileset);
		MHTileGrid fdecals = loadTileMap(info.floorDetailFile, tileset);
		MHTileGrid walls = loadActors(info.wallFile, tileset);
		MHTileGrid wdecals = loadActors(info.wallDetailFile, tileset);
		MHTileGrid actors = loadActors(info.actorFile, tileset);
		
		// Store the tile grids in the world's layers.
		MHGameWorldData world = new MHGameWorldData();
		
		world.setMapName(mapName);
		world.setTileSetID(info.tileSetId);
		//world.setTileWidth(info.tileWidth);
		//world.setTileHeight(info.tileHeight);
		world.setTileGrid(MHLayersEnum.FLOOR,        floor,   info.floorFile);
		world.setTileGrid(MHLayersEnum.FLOOR_DECALS, fdecals, info.floorDetailFile);
		world.setTileGrid(MHLayersEnum.WALLS,        walls,   info.wallFile);
		world.setTileGrid(MHLayersEnum.WALL_DECALS,  wdecals, info.wallDetailFile);
        world.setTileGrid(MHLayersEnum.ACTORS,       actors,  info.actorFile);

		return world;
	}
	

	public static void saveActorFile(String filename, MHActorList actors)
	{
	    if (filename == null)
	        return;
	    
	    String data = actors.toString();
        if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
            filename = MHGlobalConstants.DIR_DATA+"/"+filename;
	    MHTextFile file = new MHTextFile(filename, Mode.REWRITE);
	    file.append(data);
	    file.close();
	}

	   
	public static void saveGameWorld(String filename, MHGameWorldData world)
	{
	    String data = world.toString();

	    if (!filename.startsWith(MHGlobalConstants.DIR_DATA))
	        filename = MHGlobalConstants.DIR_DATA+"/"+filename;

        MHTextFile file = new MHTextFile(filename, Mode.REWRITE);
        file.append(data);
        file.close();

        saveTileMap(world.getLayerFileName(MHLayersEnum.FLOOR), world.getLayer(MHLayersEnum.FLOOR));
        saveTileMap(world.getLayerFileName(MHLayersEnum.FLOOR_DECALS), world.getLayer(MHLayersEnum.FLOOR_DECALS));
        saveTileMap(world.getLayerFileName(MHLayersEnum.WALLS), world.getLayer(MHLayersEnum.WALLS));
        saveTileMap(world.getLayerFileName(MHLayersEnum.WALL_DECALS), world.getLayer(MHLayersEnum.WALL_DECALS));
        saveTileMap(world.getLayerFileName(MHLayersEnum.ACTORS), world.getLayer(MHLayersEnum.ACTORS));
	}
	
	
	private static String initTileMapData(int rows, int columns)
	{
        String d = MHMapFileInfo.MAP_FILE_DELIMITER;
        String tilemap = rows + d + columns;
        
        for (int i = 0; i < columns*rows; i++)
            tilemap += d + MHTileSet.NULL_TILE_ID;
        
        return tilemap;
	}
	
	
	public static void generateTestFiles(String tilesetID)
	{
		final int MAP_WIDTH = 100;
		final int MAP_HEIGHT = 100;
		
		String tilemap = initTileMapData(MAP_WIDTH, MAP_HEIGHT);

		MHTextFile floorfile = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\testfloor.ltm", Mode.REWRITE);
		floorfile.append(tilemap);
		floorfile.close();
		
		MHTextFile floordetailfile = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\testfloordetail.ltm", Mode.REWRITE);
		floordetailfile.append(tilemap);
		floordetailfile.close();
		
		MHTextFile wallfile = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\testwall.ltm", Mode.REWRITE);
		wallfile.append(tilemap);
		wallfile.close();
		
		MHTextFile walldetailfile = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\testwalldetail.ltm", Mode.REWRITE);
		walldetailfile.append(tilemap);
		walldetailfile.close();
		
        MHTextFile actorfile = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\testactors.ltm", Mode.REWRITE);
        actorfile.append(tilemap);
        actorfile.close();		
		
		MHTextFile world = new MHTextFile(MHGlobalConstants.DIR_DATA+"\\TestMap.lime", Mode.REWRITE);
		// FIXME:  Map width and height not needed in metadata.
		// map width, map height, tile width, tile height, tile set name
        String d = MHMapFileInfo.MAP_FILE_DELIMITER;
		String meta = "TestMap" + d + MAP_WIDTH + d + MAP_HEIGHT + d + "20" + d + "20" + d + "test" + d + 
				      floorfile.getName() + d + floordetailfile.getName() + d +
				      wallfile.getName() + d + walldetailfile.getName() + d +
				      actorfile.getName();
		world.append(meta);
		world.close();
	}
	
}
