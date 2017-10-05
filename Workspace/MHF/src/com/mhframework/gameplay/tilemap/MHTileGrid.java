package com.mhframework.gameplay.tilemap;

import com.mhframework.MHRenderable;
import com.mhframework.gameplay.tilemap.io.MHMapFileInfo;

/********************************************************************
 * Handles the memory storage of a tile grid.
 * 
 * @author Michael Henson
 */
public class MHTileGrid
{
    private String filename;
    private MHITileMapContent[][] tileGrid;
    
    /****************************************************************
     */
    public MHTileGrid(int rows, int columns)
    {
    	tileGrid = new MHITileMapContent[rows][columns];
//    	for (int r = 0; r < rows; r++)
//    		tileGrid[r] = new MHITileMapContent[columns];
    }
    
    
    /****************************************************************
     */
    public int getNumColumns()
    {
        return tileGrid[0].length;
    }


    /****************************************************************
     */
    public int getNumRows()
    {
        return tileGrid.length;
    }
    
    
    /****************************************************************
     */
    public void putTile(MHTile tile, int row, int column)
    {
    	if (isValid(row, column))
    		tileGrid[row][column] = tile;
    }
    
    
    /****************************************************************
     */
    public MHITileMapContent getTile(int row, int column)
    {
    	if (isValid(row, column))
    		return tileGrid[row][column];
    	
    	return null;
    }
    
    
    /****************************************************************
     */
    public MHITileMapContent[][] getDataGrid()
    {
    	return tileGrid;
    }
    
    
    /****************************************************************
     */
    @Override
    public String toString()
    {
    	StringBuffer result = new StringBuffer();
    	result.append(getNumRows() + MHMapFileInfo.MAP_FILE_DELIMITER + getNumColumns());
    	
    	for (int r = 0; r < getNumRows(); r++)
    	{
    		for (int c = 0; c < getNumColumns(); c++)
    		{
    		    MHITileMapContent tile = tileGrid[r][c];
    		    int tileID;
    		    if (tile == null)
    		        tileID = MHTileSet.NULL_TILE_ID;
    		    else
    		        tileID = tile.getTileID();
    		    
        		result.append(MHMapFileInfo.MAP_FILE_DELIMITER + tileID);
    		}
    	}
    	
    	return result.toString();
    }
    
    
    /****************************************************************
     */
    private boolean isValid(int r, int c)
    {
    	return (r >= 0 && r < getNumRows() && 
    			c >= 0 && c < getNumColumns());
    }


    public String getFilename()
    {
        return filename;
    }


    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
