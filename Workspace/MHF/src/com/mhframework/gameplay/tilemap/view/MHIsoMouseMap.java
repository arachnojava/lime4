package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTileMapDirection;

/**
 * Mouse map for isometric tile maps.  The <tt>getDirection()</tt>
 * method in this class accepts an (x, y) pair representing a fine
 * mouse map coordinate, and then returns a constant indicating the
 * direction to perform a tile walk in order to identify the proper
 * map coordinate containing the mouse point.
 * 
 * @author Michael Henson
 */
public class MHIsoMouseMap
{
    /////////////////////////////////////////////////////////////////
	////    Constants                                            ////
    /////////////////////////////////////////////////////////////////

	/**
	 * Constant indicating center of mouse map.
	 */
	//public static final int CENTER = -1;

    /////////////////////////////////////////////////////////////////
	////    Data Members                                         ////
    /////////////////////////////////////////////////////////////////

	private static MHIsoMouseMap instance;
	
	/** The mouse map matrix. */
	private MHTileMapDirection[][] regions;

	private int tileWidth = 0;
	
	private MHVector refPoint;
	
	private MHCamera2D camera;
	private MHTilePlotter plotter;
	private MHTileWalker walker;
	


    /////////////////////////////////////////////////////////////////
	////    Methods                                              ////
    /////////////////////////////////////////////////////////////////

	/****************************************************************
	 * Constructor.
	 */
	private MHIsoMouseMap()
	{
	}
	
	
	public static MHIsoMouseMap getInstance()
	{
		if (instance == null)
			instance = new MHIsoMouseMap();
		
		return instance;
	}

	
	public MHVector getReferencePoint()
	{
	    return refPoint;
	}
	
	
	public void calculateReferencePoint(MHTilePlotter plotter)
	{
	    this.plotter = plotter;
	    refPoint = plotter.plotTile(0, 0);
	}
	
	
	public void setTileSize(int width)
	{
		tileWidth = width;
		populateRegions();
	}
	
	
	public int getWidth()
	{
		return tileWidth;
	}
	
	
	public int getHeight()
	{
		return getWidth()/2;
	}
	

	/****************************************************************
	 * Populates the mouse map matrix with the constant values
	 * defined in this class.  The resulting matrix is precisely
	 * aligned with the positions of the pixels in a standard base
	 * tile as defined in my design document.
	 */
	private void populateRegions()
	{
		int row, col;          // indices of mouse map array
		int sideRegions = getWidth()/2-2;  // distance from the top left corner of the
		                              // mouse map to the center region
		
		regions = new MHTileMapDirection[getHeight()][getWidth()];

		for (row = 0; row < getHeight(); row++)
		{
			for (col = 0; col < getWidth(); col++)
			{
				if (row < getHeight()/2)  // top half of mouse map
				{
					if (col < sideRegions)
						regions[row][col] = MHTileMapDirection.NORTHWEST;
					else if (col < getWidth() - sideRegions)
						regions[row][col] = MHTileMapDirection.CENTER;
					else
						regions[row][col] = MHTileMapDirection.NORTHEAST;
				}
				else // bottom half of mouse map
				{
					if (col < sideRegions)
						regions[row][col] = MHTileMapDirection.SOUTHWEST;
					else if (col < getWidth() - sideRegions)
						regions[row][col] = MHTileMapDirection.CENTER;
					else
						regions[row][col] = MHTileMapDirection.SOUTHEAST;
				}
			}


			if (row < getHeight() / 2)
			{
				if (sideRegions > 0)
					sideRegions -= 2;
			}
			else
				sideRegions += 2;
		}
	}


	/****************************************************************
	 * Returns the direction to be "walked" in order for the mouse
	 * point to be accurately interpreted as a map position.
	 *
	 * @param mouseX  The x coordinate of a fine mouse map point.
	 * @param mouseY  The y coordinate of a fine mouse map point.
	 *
	 * @return  A constant indicating the point's position relative
	 *           to a map cell's center.
	 */
	public MHTileMapDirection getDirection(final int mouseX, final int mouseY)
	{
	    if (plotter.getMapType() == MHRectangularMapView.Type.RECTANGULAR)
	        return MHTileMapDirection.CENTER;
	    
		if (tileWidth <= 0)
		{
			System.err.println("ERROR: Tile size not initialized in iso mouse map.");
			return MHTileMapDirection.CENTER;
		}
		
	    MHTileMapDirection direction = MHTileMapDirection.CENTER;

		if (mouseX >= 0 && mouseX < getWidth() &&
		    mouseY >= 0 && mouseY < getHeight())
		{
			direction = regions[mouseY][mouseX];
		}

		return direction;
	}
	
	
    /****************************************************************
     * Converts a screen (mouse) coordinate into a map coordinate.
     * Uses the five-step mouse mapping algorithm presented in the
     * book <i>Isometric Game Programming with DirectX 7.0</i> by
     * Ernest Pazera.
     *
     * @param mousePoint
     *            A point representing a mouse coordinate.
     *
     * @return The address of the map cell containing the input mouse
     *         coordinate.
     */
    public MHMapCellAddress mapMouse(final MHVector mousePoint)
    {

        // ///////////////////////////////////////////////////////////
        // Step #1: Convert Screen Coordinates to World Coordinates
        // ///////////////////////////////////////////////////////////

        MHVector worldPoint = camera.screenToWorld(mousePoint);

        // ///////////////////////////////////////////////////////////
        // Step #2: Subtract World Coordinates for the Upper Left of
        // the Map Position (0, 0)
        // ///////////////////////////////////////////////////////////

        // calculate reference point -- Point relative to mouse map
        MHVector refPoint = plotter.plotTile(0, 0);

        refPoint = refPoint.add(refPoint);

        // subtract reference point
        worldPoint = worldPoint.subtract(refPoint);

        // ///////////////////////////////////////////////////////////
        // Step #3: Determine Mouse Map Coordinates
        // ///////////////////////////////////////////////////////////

        // coarse coordinates -- Estimate which cell we're near
        final MHMapCellAddress mouseMapCoarse = new MHMapCellAddress();
        mouseMapCoarse.column = (int) (worldPoint.getX() / plotter.getTileWidth());
        mouseMapCoarse.row = (int) (worldPoint.getY() / plotter.getTileHeight());

        // fine coordinates -- Where are we relative to the coarse
        // cell?
        final MHVector mouseMapFine = new MHVector();
        mouseMapFine.setX(worldPoint.getX() % plotter.getTileWidth());
        mouseMapFine.setY(worldPoint.getY() % plotter.getTileHeight());

        // adjust for negative fine coordinates
        if (mouseMapFine.getX() < 0)
        {
            mouseMapFine.setX(mouseMapFine.getX() + plotter.getTileWidth());
            mouseMapCoarse.column--;
        }

        if (mouseMapFine.getY() < 0)
        {
            mouseMapFine.setY(mouseMapFine.getY() + plotter.getTileHeight());
            mouseMapCoarse.row--;
        }

        MHMapCellAddress mapAddress = new MHMapCellAddress();
        mapAddress.row = 0;
        mapAddress.column = 0;

        // ///////////////////////////////////////////////////////////
        // Step #4: Perform a Coarse Tile Walk
        // ///////////////////////////////////////////////////////////

        // North
        while (mouseMapCoarse.row < 0)
        {
            mapAddress = walker.tileWalk(mapAddress, MHTileMapDirection.NORTH);
            mouseMapCoarse.row++;
        }

        // South
        while (mouseMapCoarse.row > 0)
        {
            mapAddress = walker.tileWalk(mapAddress, MHTileMapDirection.SOUTH);
            mouseMapCoarse.row--;
        }

        // West
        while (mouseMapCoarse.column < 0)
        {
            mapAddress = walker.tileWalk(mapAddress, MHTileMapDirection.WEST);
            mouseMapCoarse.column++;
        }

        // East
        while (mouseMapCoarse.column > 0)
        {
            mapAddress = walker.tileWalk(mapAddress, MHTileMapDirection.EAST);
            mouseMapCoarse.column--;
        }

        // ///////////////////////////////////////////////////////////
        // Step #5: Use the Mouse Map Lookup Table
        // ///////////////////////////////////////////////////////////

        // Figure out which direction the fine coordinates indicate
        final MHTileMapDirection mouseMapDirection = getDirection(
                mouseMapFine.getX(), mouseMapFine.getY());

        // Walk in the direction specified above
        mapAddress = walker.tileWalk(mapAddress, mouseMapDirection);

        // return map coordinate
        return mapAddress;
    }




	/****************************************************************
	 * For testing only.  Do not use.
	 */
	public static void main(final String args[])
	{
		final MHIsoMouseMap mouseMap = new MHIsoMouseMap();

		for (int r = 0; r < mouseMap.getHeight(); r++)
		{
			for (int c = 0; c < mouseMap.getWidth(); c++)
			{
				System.err.print(mouseMap.getDirection(c, r));
			}

			System.err.println();
		}
	}


	public MHTileMapDirection getDirection(double x, double y) 
	{
		return getDirection((int)x, (int)y);
	}


//    public static void setInstance(MHIsoMouseMap instance)
//    {
//        MHIsoMouseMap.instance = instance;
//    }


    public void setCamera(MHCamera2D camera)
    {
        this.camera = camera;
    }


    public void setPlotter(MHTilePlotter plotter)
    {
        this.plotter = plotter;
        this.calculateReferencePoint(plotter);
    }


    public void setWalker(MHTileWalker walker)
    {
        this.walker = walker;
    }
}
