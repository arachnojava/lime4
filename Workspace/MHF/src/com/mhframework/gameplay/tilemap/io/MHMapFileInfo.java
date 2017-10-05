package com.mhframework.gameplay.tilemap.io;


/********************************************************************
 * This is the information that gets written to and read from a world
 * file by LIME.
 * 
 * @author Michael Henson
 *
 */
public class MHMapFileInfo
{
	// Constants specifying the ordering elements in a world file.
    public static final int ELEM_MAP_NAME          =  0;
    public static final int ELEM_MAP_ROWS          =  1;
	public static final int ELEM_MAP_COLUMNS       =  2;
	public static final int ELEM_TILE_WIDTH        =  3;
	public static final int ELEM_TILE_HEIGHT       =  4;
	public static final int ELEM_TILE_SET_ID       =  5;
	public static final int ELEM_FLOOR_FILE        =  6;
	public static final int ELEM_FLOOR_DETAIL_FILE =  7;
	public static final int ELEM_WALL_FILE         =  8;
	public static final int ELEM_WALL_DETAIL_FILE  =  9;
    public static final int ELEM_ACTOR_FILE        = 10;

	
	/** Delimiter for separating the values in a map data file. */
	public static final String MAP_FILE_DELIMITER = "\t";
	
	/** Extension on a world data file which contains summary 
	 *  metadata for a game world. */
    public static final String WORLD_FILE_EXTENSION = ".lime";
    
    /** Extension on tile map files. */
    public static final String TILE_MAP_FILE_EXTENSION = ".ltm"; // ltm = LIME Tile Map

    /** The width of the map in tiles. */
    public int width;
    
    /** The height of the map in tiles. */
    public int height;

    /** The width of a base tile in pixels. */
    public int tileWidth;
    
    /** The height of a base tile in pixels. */
    public int tileHeight;
    
    /** The name (ID) of the tile set to use for this world. */
    public String tileSetId;
    
    /** The name of world metadata file. */
    public String fileName;
    
    /** The name of the floor tile map file. */
    public String floorFile;
    
    /** The name of the floor details file. */
    public String floorDetailFile;
    
    /** The name of the wall tile map file. */
    public String wallFile;
    
    /** The name of the wall details file. */
    public String wallDetailFile;

    /** The name of the actor file. */
    public String actorFile;
}
