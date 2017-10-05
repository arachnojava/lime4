package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHTileMapActor;
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
 * Provides a view for isometric tile maps.
 * 
 * @author Michael Henson
 */
public class MHIsometricMapView extends MHTileMapView
{
    protected MHVector cursorAnchor = new MHVector();
    protected MHIsoMouseMap mouseMap = MHIsoMouseMap.getInstance();


    public MHIsometricMapView(Type renderType) 
    {
        super();
        mapType = renderType;
    }
    
    
    
    

    public MHVector calculateBasePoint(MHActor actor)
    {
        MHVector p = new MHVector();
        p.setX(actor.getX() + (actor.getWidth() / 2));
        p.setY(actor.getY() + actor.getHeight() - (getTileHeight()/2));
        return p;
    }


    /****************************************************************
     * Returns the vertical height of the standard base tile image in
     * this tile map.
     *
     * @return The height of the standard base tile in this map.
     */
    @Override
    public int getTileHeight()
    {
        return getTileWidth() / 2;
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
    @Override
    public MHMapCellAddress mapMouse(final MHVector mousePoint)
    {

        // ///////////////////////////////////////////////////////////
        // Step #1: Convert Screen Coordinates to World Coordinates
        // ///////////////////////////////////////////////////////////

        MHVector worldPoint = screenToWorld(mousePoint);

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
        mouseMapCoarse.column = (int) (worldPoint.getX() / getTileWidth());
        mouseMapCoarse.row = (int) (worldPoint.getY() / getTileHeight());

        // fine coordinates -- Where are we relative to the coarse
        // cell?
        final MHVector mouseMapFine = new MHVector();
        mouseMapFine.setX(worldPoint.getX() % getTileWidth());
        mouseMapFine.setY(worldPoint.getY() % getTileHeight());

        // adjust for negative fine coordinates
        if (mouseMapFine.getX() < 0)
        {
            mouseMapFine.setX(mouseMapFine.getX() + getTileWidth());
            mouseMapCoarse.column--;
        }

        if (mouseMapFine.getY() < 0)
        {
            mouseMapFine.setY(mouseMapFine.getY() + getTileHeight());
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
        final MHTileMapDirection mouseMapDirection = mouseMap.getDirection(
                mouseMapFine.getX(), mouseMapFine.getY());

        // Walk in the direction specified above
        mapAddress = walker.tileWalk(mapAddress, mouseMapDirection);

        // return map coordinate
        return mapAddress;
    }

    
    @Override
    public void setMapData(MHGameWorldData data)
    {
        mouseMap.setTileSize(data.getTileWidth());
        initView(this, data);
    }

    @Override
    public MHBitmapImage renderCompleteImage()
    {
        MHBitmapImage img = MHPlatform.createImage(camera.getWorldSpace().width, camera.getWorldSpace().height);
        MHGraphicsCanvas bg = img.getGraphicsCanvas();

        MHVector world = new MHVector(camera.getWorldSpace().x, camera.getWorldSpace().y);
        
        renderCompleteLayer(bg, mapData.getLayer(MHLayersEnum.FLOOR), world);
        renderCompleteLayer(bg, mapData.getLayer(MHLayersEnum.FLOOR_DECALS), world);
        
        for (int r = 0; r < mapData.getWorldHeight(); r++)
            for (int c = 0; c < mapData.getWorldWidth(); c++)
            {
                MHITileMapContent obj = mapData.getLayer(MHLayersEnum.WALLS).getTile(r, c);
                renderActor(bg, obj, r, c, world);
                
                obj = mapData.getLayer(MHLayersEnum.ACTORS).getTile(r, c);
                renderActor(bg, obj, r, c, world);

                obj = mapData.getLayer(MHLayersEnum.WALL_DECALS).getTile(r, c);
                renderActor(bg, obj, r, c, world);
            }
        
        return img;
    }

    
    private void renderActor(MHGraphicsCanvas g, MHITileMapContent obj, int row, int column, MHVector world)
    {
        if (obj != null && obj.getTileID() != MHTileSet.NULL_TILE_ID)
        {
            MHVector p = plotter.plotActor(obj.getImage(), row, column).subtract(world);
            g.drawImage(obj.getImage(), (int)p.x, (int)p.y);
        }
    }
    
    private void renderCompleteLayer(MHGraphicsCanvas g, MHTileGrid layer, MHVector world)
    {
        for (int r = 0; r < mapData.getWorldHeight(); r++)
            for (int c = 0; c < mapData.getWorldWidth(); c++)
            {
                MHVector p = plotter.plotTile(r, c).subtract(world);
                g.drawImage(layer.getTile(r, c).getImage(), (int)p.x, (int)p.y);
            }
    }
    
    
    private void renderVisibleTiles(MHGraphicsCanvas g, MHRectangle screenSpace)
    {
        MHFont font = MHFont.getDefaultFont();

        MHGraphicsCanvas bg = buffer.getGraphicsCanvas();
        bg.fill(MHColor.BLACK);
        
        //working variables for calculating corners
        MHVector ptScreen = new MHVector(screenSpace.x, screenSpace.y);
        MHVector ptWorld = camera.screenToWorld(ptScreen);
        
        MHMapCellAddress ptCoarse = new MHMapCellAddress((int)(ptWorld.y/plotter.getTileHeight()),
                                                         (int)(ptWorld.x/plotter.getTileWidth()));
        MHMapCellAddress ptMap = new MHMapCellAddress();
        MHMapCellAddress upperLeft = new MHMapCellAddress(), 
                upperRight = new MHMapCellAddress(), 
                lowerLeft = new MHMapCellAddress(), 
                lowerRight = new MHMapCellAddress();
        
        MHIsoMouseMap mouseMap = MHIsoMouseMap.getInstance();
        mouseMap.setPlotter(plotter);


        //////////////////////
        // UPPER LEFT CORNER
        //////////////////////
        
        //adjust by mousemap reference point    
        ptWorld.x -= mouseMap.getReferencePoint().x;
        ptWorld.y -= mouseMap.getReferencePoint().y;

        //adjust for negative remainders
        if(ptWorld.x%plotter.getTileWidth()<0) ptCoarse.column--;
        if(ptWorld.y%plotter.getTileHeight()<0) ptCoarse.row--;

        //set map point to 0,0
        ptMap.row = 0;
        ptMap.column = 0;
        //do eastward tilewalk
        ptMap= walker.tileWalk(ptMap, MHTileMapDirection.EAST);
        ptMap.column *= ptCoarse.column;
        ptMap.row *= ptCoarse.column;
        //assign ptmap to corner point
        upperLeft.column = ptMap.column;
        upperLeft.row = ptMap.row;
        //reset ptmap to 0,0
        ptMap.row=0;
        ptMap.column=0;
        //do southward tilewalk
        ptMap=walker.tileWalk(ptMap, MHTileMapDirection.SOUTH);
        ptMap.column *= ptCoarse.row;
        ptMap.row *= ptCoarse.row;
        //add ptmap to corner point
        upperLeft.column += ptMap.column;
        upperLeft.row += ptMap.row;

        //////////////////////
        // UPPER RIGHT CORNER
        //////////////////////
        
        //screen point
        ptScreen.x = screenSpace.right();
        ptScreen.y = screenSpace.top();
        //change into world coordinate
        ptWorld=camera.screenToWorld(ptScreen);
        //adjust by mousemap reference point    
        ptWorld.x-=mouseMap.getReferencePoint().x;
        ptWorld.y-=mouseMap.getReferencePoint().y;
        //calculate coarse coordinates
        ptCoarse.column = (int) (ptWorld.x/plotter.getTileWidth());
        ptCoarse.row = (int) (ptWorld.y/plotter.getTileHeight());
        //adjust for negative remainders
        if(ptWorld.x%plotter.getTileWidth()<0) ptCoarse.column--;
        if(ptWorld.y%plotter.getTileHeight()<0) ptCoarse.row--;
        //set map point to 0,0
        ptMap.row=0;
        ptMap.column=0;
        //do eastward tilewalk
        ptMap=walker.tileWalk(ptMap, MHTileMapDirection.EAST);
        ptMap.column*=ptCoarse.column;
        ptMap.row*=ptCoarse.column;
        //assign ptmap to corner point
        upperRight.column=ptMap.column;
        upperRight.row=ptMap.row;
        //reset ptmap to 0,0
        ptMap.column=0;
        ptMap.row=0;
        //do southward tilewalk
        ptMap=walker.tileWalk(ptMap,MHTileMapDirection.SOUTH);
        ptMap.column *= ptCoarse.row;
        ptMap.row*=ptCoarse.row;
        //add ptmap to corner point
        upperRight.column += ptMap.column;
        upperRight.row += ptMap.row;

        
        //////////////////////
        // LOWER LEFT CORNER
        //////////////////////
        
        //screen point
        ptScreen.x = screenSpace.left();
        ptScreen.y = screenSpace.bottom();
        //change into world coordinate
        ptWorld = camera.screenToWorld(ptScreen);
        //adjust by mousemap reference point    
        ptWorld.x-=mouseMap.getReferencePoint().x;
        ptWorld.y-=mouseMap.getReferencePoint().y;
        //calculate coarse coordinates
        ptCoarse.column = (int) (ptWorld.x/plotter.getTileWidth());
        ptCoarse.row = (int) (ptWorld.y/plotter.getTileHeight());
        //adjust for negative remainders
        if(ptWorld.x%plotter.getTileWidth()<0) ptCoarse.column--;
        if(ptWorld.y%plotter.getTileHeight()<0) ptCoarse.row--;
        //set map point to 0,0
        ptMap.column=0;
        ptMap.row=0;
        //do eastward tilewalk
        ptMap=walker.tileWalk(ptMap, MHTileMapDirection.EAST);
        ptMap.column*=ptCoarse.column;
        ptMap.row*=ptCoarse.column;
        //assign ptmap to corner point
        lowerLeft.column=ptMap.column;
        lowerLeft.row=ptMap.row;
        //reset ptmap to 0,0
        ptMap.column=0;
        ptMap.row=0;
        //do southward tilewalk
        ptMap=walker.tileWalk(ptMap, MHTileMapDirection.SOUTH);
        ptMap.column *= ptCoarse.row;
        ptMap.row*=ptCoarse.row;
        //add ptmap to corner point
        lowerLeft.column+=ptMap.column;
        lowerLeft.row+=ptMap.row;

        //////////////////////
        // LOWER RIGHT CORNER
        //////////////////////
        
        //screen point
        ptScreen.x = screenSpace.right();
        ptScreen.y = screenSpace.bottom();
        //change into world coordinate
        ptWorld=camera.screenToWorld(ptScreen);
        //adjust by mousemap reference point    
        ptWorld.x-=mouseMap.getReferencePoint().x;
        ptWorld.y-=mouseMap.getReferencePoint().y;
        //calculate coarse coordinates
        ptCoarse.column = (int) (ptWorld.x/plotter.getTileWidth());
        ptCoarse.row = (int) (ptWorld.y/plotter.getTileHeight());
        //adjust for negative remainders
        if(ptWorld.x%plotter.getTileWidth()<0) ptCoarse.column--;
        if(ptWorld.y%plotter.getTileHeight()<0) ptCoarse.row--;
        //set map point to 0,0
        ptMap.column=0;
        ptMap.row=0;
        //do eastward tilewalk
        ptMap = walker.tileWalk(ptMap, MHTileMapDirection.EAST);
        ptMap.column*=ptCoarse.column;
        ptMap.row*=ptCoarse.column;
        //assign ptmap to corner point
        lowerRight.column=ptMap.column;
        lowerRight.row=ptMap.row;
        //reset ptmap to 0,0
        ptMap.column=0;
        ptMap.row=0;
        //do southward tilewalk
        ptMap = walker.tileWalk(ptMap, MHTileMapDirection.SOUTH);
        ptMap.column*=ptCoarse.row;
        ptMap.row*=ptCoarse.row;
        //add ptmap to corner point
        lowerRight.column+=ptMap.column;
        lowerRight.row+=ptMap.row;

        //tilewalk from corners
        upperLeft = walker.tileWalk(upperLeft, MHTileMapDirection.NORTHWEST);
        upperRight = walker.tileWalk(upperRight, MHTileMapDirection.NORTHEAST);
        lowerLeft = walker.tileWalk(lowerLeft, MHTileMapDirection.SOUTHWEST);
        lowerRight = walker.tileWalk(lowerRight, MHTileMapDirection.SOUTHEAST);

        // The rendering algorithm works in two passes:
        // 1.  Floor and floor details if visible.
        // 2.  Actors, walls, and wall details
        
        for (int pass = 0; pass < 2; pass++)
        {
        //main rendering loop
        //vars for rendering loop
        MHMapCellAddress ptCurrent;
        MHMapCellAddress ptRowStart;
        MHMapCellAddress ptRowEnd;
        int dwRowCount=0;

        //set up rows
        ptRowStart = upperLeft;
        ptRowEnd = upperRight;
        
        MHGameWorldData map = getMapData();

        //start rendering loops
        for(;;)//"infinite" loop
        {
            //set current point to rowstart
            ptCurrent=ptRowStart;

            //render a row of tiles
            for(;;)//'infinite' loop
            {
                //check for valid point. if valid, render
                if(ptCurrent.column>=0 && ptCurrent.row>=0 && ptCurrent.column<map.getWorldWidth() && ptCurrent.row<map.getWorldHeight())
                {
                    //valid, so render
                    ptScreen=plotter.plotTile(ptCurrent);//plot tile
                    ptScreen=camera.screenToLocal(ptScreen);
                    ptScreen=camera.worldToScreen(ptScreen);//world->screen

                    // FIXME:  Interlace the layers correctly.
                    if (pass == 0)
                    {
                        if (isLayerVisible(MHLayersEnum.FLOOR))
                        {
                            MHITileMapContent tile = map.getLayer(MHLayersEnum.FLOOR).getTile(ptCurrent.row, ptCurrent.column);
                            bg.drawImage(tile.getImage(), (int)ptScreen.x, (int)ptScreen.y);
                            
                            
                            if (idOn && tile.getTileID() != MHTileSet.NULL_TILE_ID)
                            {
                                g.setFont(font);
                                g.setColor(MHColor.WHITE);
                                
                                String ids = tile.getTileID()+"";
                                int tpx = (int)ptScreen.x;
                                int tpy = (int)ptScreen.y;
                                int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
                                int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
                                g.drawString(ids, cx, cy);
                            }
                        }
                        if (isLayerVisible(MHLayersEnum.FLOOR_DECALS))
                        {
                            MHITileMapContent tile = map.getLayer(MHLayersEnum.FLOOR_DECALS).getTile(ptCurrent.row, ptCurrent.column);
                            bg.drawImage(tile.getImage(), (int)ptScreen.x, (int)ptScreen.y);
                            
                            if (idOn && tile.getTileID() != MHTileSet.NULL_TILE_ID)
                            {
                                g.setFont(font);
                                g.setColor(MHColor.WHITE);
                                
                                String ids = tile.getTileID()+"";
                                int tpx = (int)ptScreen.x;
                                int tpy = (int)ptScreen.y;
                                int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
                                int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
                                g.drawString(ids, cx, cy);
                            }
                        }
                    } // end pass 0
                    else
                    {
                        if (isLayerVisible(MHLayersEnum.WALLS))
                        {
                            MHITileMapContent actor = map.getLayer(MHLayersEnum.WALLS).getTile(ptCurrent.row, ptCurrent.column);
                            if (actor != null && actor.getTileID() != MHTileSet.NULL_TILE_ID)
                            {
                                MHVector p = plotter.plotActor(actor.getImage(), ptCurrent.row, ptCurrent.column);
                                p=camera.screenToLocal(p);
                                p=camera.worldToScreen(p);
                                bg.drawImage(actor.getImage(), (int)p.x, (int)p.y);
                                if (idOn)
                                {
                                    g.setFont(font);
                                    g.setColor(MHColor.WHITE);
                                    
                                    String ids = actor.getTileID()+"";
                                    int tpx = (int)ptScreen.x;
                                    int tpy = (int)ptScreen.y;
                                    int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
                                    int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
                                    g.drawString(ids, cx, cy);
                                }
                            }
                        }
                        if (isLayerVisible(MHLayersEnum.ACTORS))
                        {
                            MHITileMapContent actor = map.getLayer(MHLayersEnum.ACTORS).getTile(ptCurrent.row, ptCurrent.column);
                            if (actor != null && actor.getTileID() != MHTileSet.NULL_TILE_ID)
                            {
                                MHVector p = plotter.plotActor(actor.getImage(), ptCurrent.row, ptCurrent.column);
                                p=camera.screenToLocal(p);
                                p=camera.worldToScreen(p);
                                bg.drawImage(actor.getImage(), (int)p.x, (int)p.y);
                                
                                if (idOn)
                                {
                                    g.setFont(font);
                                    g.setColor(MHColor.WHITE);
                                    
                                    String ids = actor.getTileID()+"";
                                    int tpx = (int)ptScreen.x;
                                    int tpy = (int)ptScreen.y;
                                    int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
                                    int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
                                    g.drawString(ids, cx, cy);
                                }
                            }
                        }
                        if (isLayerVisible(MHLayersEnum.WALL_DECALS))
                        {
                            MHITileMapContent actor = map.getLayer(MHLayersEnum.WALL_DECALS).getTile(ptCurrent.row, ptCurrent.column);
                            if (actor != null && actor.getTileID() != MHTileSet.NULL_TILE_ID)
                            {
                                MHVector p = plotter.plotActor(actor.getImage(), ptCurrent.row, ptCurrent.column);
                                p=camera.screenToLocal(p);
                                p=camera.worldToScreen(p);
                                bg.drawImage(actor.getImage(), (int)p.x, (int)p.y);
                                
                                if (idOn)
                                {
                                    g.setFont(font);
                                    g.setColor(MHColor.WHITE);
                                    
                                    String ids = actor.getTileID()+"";
                                    int tpx = (int)ptScreen.x;
                                    int tpy = (int)ptScreen.y;
                                    int cx = tpx + getTileWidth()/2 - font.stringWidth(ids)/2;
                                    int cy = tpy + getTileHeight()/2 + font.getHeight()/2;
                                    g.drawString(ids, cx, cy);
                                }
                            }
                        }
                    } // end pass 1
                    // Draw gridlines.
                    if (gridOn)
                    {
                        bg.drawImage(getGridImage(), (int)ptScreen.x, (int)ptScreen.y);
                    }


                        // Draw cursor.
                        if (cursorOn)
                        {
                            if (this.getCursorAddress().row == ptCurrent.row && this.getCursorAddress().column == ptCurrent.column)
                            {
                                bg.drawImage(getCursorImage(), (int)ptScreen.x, (int)ptScreen.y);
                            }
                        }

                    }

                    //check if at end of row. if we are, break out of inner loop
                    if(ptCurrent.column==ptRowEnd.column && ptCurrent.row==ptRowEnd.row) break;

                    //walk east to next tile
                    ptCurrent=walker.tileWalk(ptCurrent, MHTileMapDirection.EAST);
                }

                //check to see if we are at the last row. if we are, break out of loop
                if (ptRowStart.column == lowerLeft.column && ptRowStart.row == lowerLeft.row) break;

                //move the row start and end points, based on the row number
                if(dwRowCount % 2 != 0)
                {
                    //odd
                    //start moves SW, end moves SE
                    ptRowStart = walker.tileWalk(ptRowStart, MHTileMapDirection.SOUTHWEST);
                    ptRowEnd = walker.tileWalk(ptRowEnd, MHTileMapDirection.SOUTHEAST);
                }
                else
                {
                    //even
                    //start moves SE, end moves SW
                    ptRowStart = walker.tileWalk(ptRowStart, MHTileMapDirection.SOUTHEAST);
                    ptRowEnd = walker.tileWalk(ptRowEnd, MHTileMapDirection.SOUTHWEST);
                }

                //increase the row number
                dwRowCount++;
            }
        }
    }

    
    public void render(MHGraphicsCanvas g)
    {
        renderVisibleTiles(buffer.getGraphicsCanvas(), camera.getScreenSpace());
        g.drawImage(buffer, camera.getScreenSpace().x, camera.getScreenSpace().y);
    }

    
//    public void renderFine(MHGraphicsCanvas g, MHBitmapImage image, double x, double y)
//    {        
//        // Adjust for screen anchor to enable scrolling
//        x -= camera.getAnchor().getX();
//        y -= camera.getAnchor().getY();
//
//        // Draw the image
//        g.drawImage(image, (int)x, (int)y);
//    }


    public void renderBaseTile(MHGraphicsCanvas g, MHBitmapImage image, int row, int column)
    {
        // Calculate plot coordinates.
        MHVector screenPoint = plotter.plotTile(row, column);

        // Adjust for screen anchor to enable scrolling.
        screenPoint = screenPoint.subtract(camera.getAnchor());

        // Draw the tile image.
        g.drawImage(image, (int)screenPoint.x, (int)screenPoint.y);
    }


    /****************************************************************
     * Centers the view space on the map cell specified by the row
     * and column parameters.
     * 
     * @param row
     * @param column
     */
    public void centerOnMapCell(int row, int column)
    {
        // TODO  Move this to camera class.
        MHVector p = plotter.plotTile(row, column);
        int width = getScreenSpace().width;
        int height = getScreenSpace().height;
        int x = (int) (p.getX() - width/2 + getTileWidth()/2);
        int y = (int) (p.getY() - height/2);
        camera.setAnchor(x, y);
    }


    /****************************************************************
     * Centers the view space on the pixel position specified by the
     * parameters.
     * 
     * @param x
     * @param y
     */
    public void centerOnPixel(int x, int y)
    {
        int width = super.getScreenSpace().width;
        int height = super.getScreenSpace().height;
        int ax = x - width/2;
        int ay = y - height/2;
        camera.setAnchor(ax, ay);
    }





    @Override
    public MHBitmapImage createTileOutline(MHColor color)
    {
        MHBitmapImage img = MHPlatform.createImage(mapData.getTileWidth(), mapData.getTileHeight());
        MHGraphicsCanvas g = img.getGraphicsCanvas();

        int right = img.getWidth()-1;
        int bottom = img.getHeight()-1;
        int cx = img.getWidth()/2;
        int cy = img.getHeight()/2;
        
        g.fill(MHPlatform.createColor(0, 0, 0, 0));
        g.setColor(color);
        g.drawLine(cx,     0,     right, cy-1);
        g.drawLine(right, cy,     cx,    bottom);
        g.drawLine(cx-1,  bottom, 0,     cy);
        g.drawLine(0,     cy-1,   cx-1,  0);
        
        return img;
    }
}
