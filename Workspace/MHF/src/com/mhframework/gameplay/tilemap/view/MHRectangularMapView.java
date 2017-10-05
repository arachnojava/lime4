package com.mhframework.gameplay.tilemap.view;


import com.mhframework.MHRenderable;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHActorList;
import com.mhframework.gameplay.tilemap.MHITileMapContent;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.MHTileMapDirection;
import com.mhframework.gameplay.tilemap.MHTileSet;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

/********************************************************************
 * @author Michael Henson
 */
public class MHRectangularMapView extends MHTileMapView
{
    
    
	////////////////////////////
	////    Data Members    ////
	////////////////////////////

    


	////////////////////////////
	////      Methods       ////
	////////////////////////////

	/****************************************************************
	 * Constructor.  Creates the map data structure and sets up the
	 * virtual spaces (screen space, world space, and anchor space).
	 */
	public MHRectangularMapView()
	{
	    mapType = Type.RECTANGULAR;
	}


	public MHMapCellAddress tileWalk(int row, int column,
            MHTileMapDirection direction)
    {
        MHMapCellAddress a = new MHMapCellAddress(row, column);

        switch (direction)
        {
        case NORTH:     a.row--;             break;
        case NORTHEAST: a.row--; a.column++; break;
        case EAST:               a.column++; break;
        case SOUTHEAST: a.row++; a.column++; break;
        case SOUTH:     a.row++;             break;
        case SOUTHWEST: a.row++; a.column--; break;
        case WEST:               a.column--; break;
        case NORTHWEST: a.row--; a.column--; break;
        case CENTER:                         break;
        default:
            break;
        }

        return a;
    }
	
	

    
    /****************************************************************
     * Tile plotter.  Converts map coordinates to screen coordinates.
     * 
     * @param mapRow  The row of the map whose pixel position is
     *                 being calculated.
     * @param mapCol  The column of the map whose pixel position is
     *                 being calculated.
     *
     * @return The pixel position on screen where the base tile is
     *          to be rendered.
     */
    public MHVector plotTile(int row, int column)
    {
        MHVector plotPoint;

        // Calculate world coordinates
        final int x = column * getTileWidth();
        final int y = row * getTileHeight();

        // Convert world coordinates to screen coordinates
        plotPoint = camera.worldToScreen(new MHVector(x, y));
        plotPoint = camera.screenToLocal(plotPoint);

        return plotPoint;
    }

    private void renderVisibleTiles(MHGraphicsCanvas g, int r0, int c0, int horizontalTiles, int verticalTiles, MHActorList actors)
	{
        MHFont font = MHFont.getDefaultFont();

		for (int layerID = 0; layerID < mapData.getNumLayers(); layerID++)
		{
			MHTileGrid layer = mapData.getLayer(layerID);
			if (layer != null && layerVisible[layerID])
			{
//			    if (layerID == MHLayersEnum.WALL_DECALS.getID() && actors != null && layerVisible[MHLayersEnum.ACTORS.getID()])
//			    {
			        // For most regular tile maps, the most appropriate time
			        // to render the actors is after the walls and before the
			        // wall decals.  This is dramatically different from 
			        // isometric algorithms.
			        
			        // TODO:  Decide whether rectangular walls should be sorted.
			        // I think so because of the 2.5D side perspective.
//			        for (int actor = 0; actor < actors.size(); actor++)
//			        {
//			            MHVector screenAnchor = camera.getAnchor();
//			            MHActor a = actors.get(actor);
//			            if (camera.getScreenSpace().intersects(a.getBounds()))
//			                a.render(g, (int)screenAnchor.x, (int)screenAnchor.y);
//			        }
//			    }
			    
				for (int r = r0; r <= r0+verticalTiles; r++)
				{
					for (int c = c0; c <= c0+horizontalTiles; c++)
					{
						MHVector tilePos = plotTile(r, c);
						int tpx = (int)(tilePos.getX());
						int tpy = (int)(tilePos.getY());
						
						MHITileMapContent tile = layer.getTile(r, c);
						
						if (tile != null && tile.getTileID() != MHTileSet.NULL_TILE_ID)
						{
							g.drawImage(tile.getImage(), tpx, tpy);
		                    if (idOn)
		                    {
		                        g.setFont(font);
                                g.setColor(MHColor.WHITE);
		                        
		                        String ids = tile.getTileID()+"";
		                        int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
		                        int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
		                        g.drawString(ids, cx, cy);
		                    }
						}
						

						// Draw gridlines.
                        if (gridOn)
                        {
                            g.drawImage(getGridImage(), tpx, tpy);
                        }
                        

                        // Draw cursor.
                        if (cursorOn)
                        {
                            if (this.getCursorAddress().row == r && this.getCursorAddress().column == c)
                            {
                                g.drawImage(getCursorImage(), tpx, tpy);
                            }
                        }
						
					}
				}
			}
		}

	}


    @Override
    public void render(MHGraphicsCanvas g)
    {
        MHGraphicsCanvas bg = buffer.getGraphicsCanvas();
        bg.fill(MHColor.BLACK);
        MHRectangle visibleTiles = new MHRectangle();
        int tileWidth = getTileWidth();
        int tileHeight = getTileHeight();
        int viewWidth = getScreenSpace().width;
        int viewHeight = getScreenSpace().height;
        visibleTiles.x = (int)(camera.getAnchor().getX()/tileWidth);
        visibleTiles.y = (int)(camera.getAnchor().getY()/tileHeight);
        visibleTiles.width = Math.min(viewWidth/tileWidth, mapData.getWorldWidth()-1);
        visibleTiles.height = Math.min(viewHeight/tileHeight, mapData.getWorldHeight()-1);
        
        renderVisibleTiles(bg, visibleTiles.y, visibleTiles.x, visibleTiles.width, visibleTiles.height, null);
        
        g.drawImage(buffer, getScreenSpace().x, getScreenSpace().y);
    }

    
    
    

    @Override
    public MHBitmapImage renderCompleteImage()
    {
        MHBitmapImage img = MHPlatform.createImage(camera.getWorldSpace().width, camera.getWorldSpace().height);
        MHGraphicsCanvas bg = img.getGraphicsCanvas();
        bg.fill(MHColor.BLACK);
        MHRectangle visibleTiles = new MHRectangle();
        int tileWidth = getTileWidth();
        int tileHeight = getTileHeight();
        int viewWidth = img.getWidth();
        int viewHeight = img.getHeight();
        visibleTiles.x = (int)(camera.getAnchor().getX()/tileWidth);
        visibleTiles.y = (int)(camera.getAnchor().getY()/tileHeight);
        visibleTiles.width = Math.min(viewWidth/tileWidth, mapData.getWorldWidth()-1);
        visibleTiles.height = Math.min(viewHeight/tileHeight, mapData.getWorldHeight()-1);
        
        renderVisibleTiles(bg, visibleTiles.y, visibleTiles.x, visibleTiles.width, visibleTiles.height, null);
        
        return img;
    }


    @Override
    public void setMapData(MHGameWorldData data)
    {
        mapData = data;
    }


    @Override
    public MHBitmapImage createTileOutline(MHColor color)
    {
        MHBitmapImage img = MHPlatform.createImage(mapData.getTileWidth(), mapData.getTileHeight());
        
        MHGraphicsCanvas g = img.getGraphicsCanvas();
        
        g.fill(MHPlatform.createColor(0, 0, 0, 0));
        g.setColor(color);
        g.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
        
        return img;
    }


    @Override
    public MHVector plotActor(MHBitmapImage image, int r, int c)
    {
        MHVector v = plotTile(r, c);
        v.x += mapData.getTileWidth()/2 - image.getWidth()/2;
        v.y += mapData.getTileHeight()/2 - image.getHeight()/2;
        
        return v;
    }
}

