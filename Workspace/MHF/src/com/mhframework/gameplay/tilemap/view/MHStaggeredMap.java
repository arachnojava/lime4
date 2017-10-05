package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTileMapDirection;



/********************************************************************
 * Handles the presentation of a staggered (layered) isometric map.
 */
public class MHStaggeredMap implements MHTileMapInterface
{
    private MHTileMapView view;
	private final boolean flatEdges;

    /****************************************************************
	 */
	public MHStaggeredMap(MHTileMapView v)
    {
	    this.view = v;
        flatEdges = true;

        calculateAnchorSpace();
    }


    /****************************************************************
     */
    @Override
    public MHMapCellAddress tileWalk(final int row, final int column, final MHTileMapDirection direction)
    {
		final MHMapCellAddress destination = new MHMapCellAddress();

		destination.row = row;
	    destination.column = column;

		switch(direction)
		{
			case NORTH:
		        destination.row -= 2;
		        break;

			case NORTHEAST:
			    destination.column += (destination.row%2);
			    destination.row--;
				break;

			case EAST:
			    destination.column++;
				break;

			case SOUTHEAST:
			    destination.column += (destination.row%2);
				destination.row++;
				break;

			case SOUTH:
				destination.row += 2;
				break;

			case SOUTHWEST:
				destination.column += ((destination.row%2) - 1);
				destination.row++;
				break;

			case WEST:
				destination.column--;
				break;

			case NORTHWEST:
				destination.column += ((destination.row%2) - 1);
				destination.row--;
				break;

			default:
				break;
		}

        return destination;
    }


    /****************************************************************
     * Plots the upper-left anchor MHVector of a base tile image.
     */
    @Override
    public MHVector plotTile(final int mapRow, final int mapCol)
    {
		int plotX, plotY;

		final int width = view.getTileWidth();
		final int height = view.getTileHeight();

		// calculate pixel position for the map position given
		plotX = mapCol * width + (mapRow & 1) * (width / 2);
		plotY = mapRow * (height / 2);

		return new MHVector(plotX, plotY);
    }


	/****************************************************************
	 */
    public void calculateAnchorSpace()
	{
//        
//
//        int width = view.worldSpace.width - view.screenSpace.width;
//        int height = view.worldSpace.height - view.screenSpace.height;
//
//            if(width <= 0)
//                width = 1;
//
//            if(height <= 0)
//            height = 1;
//
//            view.anchorSpace = new MHRectangle(view.worldSpace.x, view.worldSpace.y, width, height);
//
//	    
//		// If edges should be flat, adjust anchor space to eliminate
//		// jaggies.
//	    // TODO: Use tile sizes here.
//		if (flatEdges)
//    		view.anchorSpace = new MHRectangle(view.worldSpace.x + 32,
//    		        view.worldSpace.y + 16, width - 32, height - 16);
//        else
//            view.anchorSpace = new MHRectangle(view.worldSpace.x,
//                    view.worldSpace.y, width, height);
	}


    @Override
    public void calculateWorldSpace()
    {
        // TODO Auto-generated method stub
        
    }

}

