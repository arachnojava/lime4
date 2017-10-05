package com.mhframework.gameplay.tilemap.view;

import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.MHGameWorldData;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHActorList;
import com.mhframework.gameplay.tilemap.MHLayersEnum;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.view.MHCamera2D.WrapMode;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public abstract class MHTileMapView
{

    public enum Type
    {
        RECTANGULAR,
        DIAMOND,
        STAGGERED;
    }
    
    protected MHTilePlotter plotter;
    protected MHTileWalker walker;
    protected MHCamera2D camera;
    

    protected MHTileMapView.Type mapType;
    /** Object containing the map data. */
    protected MHGameWorldData mapData;
    /** A rectangle containing the screen coordinates. */

    protected MHMapCellAddress cursorAddress = new MHMapCellAddress();
    protected MHVector cursorPoint = new MHVector();
    private MHRectangle scrollUpZone;
    private MHRectangle scrollDownZone;
    private MHRectangle scrollRightZone;
    private MHRectangle scrollLeftZone;
    private boolean mouseScroll = false;
    protected MHBitmapImage buffer;
    protected boolean gridOn = false;
    protected boolean cursorOn;
    protected boolean[] layerVisible;
    protected boolean idOn = false;
    
    protected MHBitmapImage gridImage, cursorImage;

    protected MHTileMapView()
    {
        layerVisible = new boolean[MHLayersEnum.values().length];
        for (int i = 0; i < layerVisible.length; i++)
            layerVisible[i] = true;
        
    }

    
    public static MHTileMapView create(Type mapType, MHGameWorldData data)
    {
        MHTileMapView view = null;
        
        switch (mapType)
        {
        case DIAMOND:
        case STAGGERED:
            view = new MHIsometricMapView(mapType);
            break;
        case RECTANGULAR:
            view = new MHRectangularMapView();
            break;
        default:
            break;
        
        }
        
        view = initView(view, data);
        
        return view;
    }
    
    
    protected MHBitmapImage getGridImage()
    {
        if (gridImage == null)
            gridImage = createTileOutline(MHColor.WHITE);
        
        return gridImage;
    }
    
    protected MHBitmapImage getCursorImage()
    {
        if (cursorImage == null)
            cursorImage = this.createTileOutline(MHColor.GREEN);
        
        return cursorImage;
    }
    
    
    protected static MHTileMapView initView(MHTileMapView view, MHGameWorldData data)
    {
        view.mapData = data;
        view.plotter = MHTilePlotter.create(view.mapType);
        view.plotter.setTileSize(data.getTileWidth(), data.getTileHeight());
        view.walker = MHTileWalker.create(view.mapType);
        MHIsoMouseMap.getInstance().setPlotter(view.plotter);
        MHRectangle screenSpace = new MHRectangle(0, 0, MHScreenManager.getDisplayWidth(), MHScreenManager.getDisplayHeight());
        if (view.camera == null)
        {
            view.camera = new MHCamera2D();
            view.camera.setHWrapMode(WrapMode.CLIP);
            view.camera.setVWrapMode(WrapMode.CLIP);
        }
        if (view.camera.getScreenSpace() == null)
            view.camera.setScreenSpace(screenSpace);
        view.camera.calculateWorldSpace(view.plotter, data.getWorldHeight(), data.getWorldWidth());
        view.camera.calculateAnchorSpace();
        view.cursorImage = view.createTileOutline(MHColor.GREEN);
        view.gridImage = view.createTileOutline(MHColor.WHITE);
        
        return view;
    }
    
    public abstract void setMapData(MHGameWorldData data);

    
    public abstract MHBitmapImage createTileOutline(MHColor color);

    public boolean isLayerVisible(MHLayersEnum layer)
    {
        return layerVisible[layer.getID()];
    }

    public void setLayerVisible(MHLayersEnum layer, boolean isVisible)
    {
        layerVisible[layer.getID()] = isVisible;
    }

    public MHVector calculateBasePoint(MHActor actor)
    {
    	return actor.getPosition().add(actor.getWidth()/2, actor.getHeight());
    }

    /****************************************************************
     * Scrolls the map in the distance specified by the input
     * parameters.
     *
     * @param scrollX  The horizontal distance in pixels to scroll
     *                 the map
     * @param scrollY  The vertical distance in pixels to scroll
     *                 the map
     */
    public void scrollMap(final double scrollX, final double scrollY)
    {
        camera.moveAnchor(scrollX, scrollY);
    	setCursorAddress(mapMouse(MHInputEventHandler.getInstance().getMousePosition()));
    }

    public void setCursorAddress(MHMapCellAddress addr)
    {
    	cursorAddress = addr;
    	clipCursorAddress();
    }



    /****************************************************************
     * Convert screen coordinates to world coordinates.
     *
     * @param screen  A point in screen coordinates.
     *
     * @return  The input screen point translated into world
     *           coordinates.
     */
    public MHVector screenToWorld(final MHVector screen)
    {
    	// translate into plotspace coordinates
    	MHVector world = screenToLocal(screen);
    	
    	// translate into world coordinates
    	world = world.add(camera.getAnchor());
    
    	return world;
    }

//    /****************************************************************
//     * Convert world coordinates to screen coordinates.
//     *
//     * @param location  A point in world space.
//     *
//     * @return A point in screen space.
//     */
//    public MHVector worldToScreen(final MHVector location)
//    {
//    	int screenX, screenY;
//    
//    	// translate into plotspace coordinates
//    	screenX = (int)(location.getX() - screenAnchor.getX());
//    	screenY = (int)(location.getY() - screenAnchor.getY());
//    
//    	// translate into screen coordinates
//    	// NOTE:  This is unnecessary if we just render straight to a
//    	//        buffer instead of to the screen.
//    	//screenX += screenSpace.x;
//    	//screenY += screenSpace.y;
//    
//    	return new MHVector(screenX, screenY);
//    }

    /****************************************************************
     * Returns the width of a base tile in this tile map.
     */
    public int getTileWidth()
    {
    	return mapData.getTileWidth();
    }
    

    /****************************************************************
     * Returns the height of a base tile in this tile map.  For
     * normal rectangular maps, the tile height is usually the same
     * as the tile width because the tiles are square.  Override
     * this method in subclasses requiring something different, such
     * as isometric maps.
     */
    public int getTileHeight()
    {
    	return mapData.getTileHeight();
    }

    /****************************************************************
     * Determines if the given point is a valid world coordinate.
     *
     * @return  True if the given point is a valid point in world
     *          space; false otherwise.
     */
    public boolean isWorldCoordinate(final MHVector p)
    {
    	return camera.isWorldCoord(p);
    }


    /****************************************************************
     * Defines the bounds of the screen space.  Screen space is the
     * area on the screen where the game world is visible.
     */
    public void setScreenSpace(int x, int y, final int width,
            final int height)
    {
    	setScreenSpace(new MHRectangle(x, y, width, height));
    }

    /****************************************************************
     * Defines the bounds of the screen space.  Screen space is the
     * area on the screen where the game world is visible.
     */
    public void setScreenSpace(MHRectangle viewRect)
    {
    		camera.setScreenSpace(viewRect);
    
            //set world space
    		if (plotter != null && mapData != null)
    		{
    		    camera.calculateWorldSpace(plotter, mapData.getWorldHeight(), mapData.getWorldWidth());
    		    camera.calculateAnchorSpace();
    		}
    		
            // Create the mouse scroll zones.
            //createScrollZones();
    
            buffer = MHPlatform.createImage(getScreenSpace().width, getScreenSpace().height);
    	}

    private void createScrollZones()
    {
    	int x = getScreenSpace().x;
    	int y = getScreenSpace().y;
    	int w = getScreenSpace().width;
    	int h = getScreenSpace().height;
    	int zoneSize = 32;
    	
    	scrollUpZone = new MHRectangle(x, y, w, zoneSize);
    	scrollDownZone = new MHRectangle(x, y+h-zoneSize, w, zoneSize);
    	scrollLeftZone = new MHRectangle(x, y, zoneSize, h);
    	scrollRightZone = new MHRectangle(x+w-zoneSize, y, zoneSize, h);
    }

    /****************************************************************
     * Converts mouse coordinates into a map cell address.
     *
     * @param screenPoint  A point indicating the current physical
     *                    location of the mouse cursor on screen.
     *
     * @return  The address of the map cell containing the mouse
     *           cursor.
     */
    public MHMapCellAddress mapMouse(MHVector screenPoint)
    {
    	final MHMapCellAddress mapCell = new MHMapCellAddress();
    	//MHVector refPoint = plotTile(0,0);
    
    	//screenPoint = screenToLocal(screenPoint);
    	
    	//convert from screen to world
    	MHVector worldPoint = screenToWorld(screenPoint);
    
    	// calculate reference point
    	//refPoint = refPoint.translate(screenSpace.x, screenSpace.y);
    
    	//subtract reference point
    	//worldPoint = worldPoint.subtract(refPoint);
    
    	mapCell.column=(int)(worldPoint.getX()/getTileWidth());
    	mapCell.row=(int)(worldPoint.getY()/getTileHeight());
    
    	//return map coordinate
    	return(mapCell);
    }

    /****************************************************************
     * Returns a reference to the MHMapGrid object serving as the map's
     * data structure.
     *
     * @return A reference to the map data object
     */
    public MHGameWorldData getMapData()
    {
    	return mapData;
    }

    public void setMouseScroll(boolean mouseScroll)
    {
    	this.mouseScroll  = mouseScroll;
    }

    public void update(long elapsedTime)
    {
    	//            for (int row = 0; row < mapData.getHeight(); row++)
    	//                for (int col = 0; col < mapData.getWidth(); col++)
    	//                    for (int layer = 0; layer < MHMapCell.NUM_LAYERS; layer++)
    	//                    {
    	//                        MHActor a = mapData.getMapCell(row, col).getLayer(layer);
    	//                        if (a != null)
    	//                        {
    	//                            a.advance();
    	//                        }
    	//                  
        MHRectangle screenSpace = camera.getScreenSpace();
        
        if (buffer.getWidth() != screenSpace.width || buffer.getHeight() != screenSpace.height)
            buffer.redimension(screenSpace.width, screenSpace.height);
    
        if (mouseScroll)
            checkForScroll();
    
    }


    public MHBitmapImage renderCompleteImage()
    {
        boolean gridMode = isGridOn();
        boolean cursorMode = isCursorOn();
        MHRectangle scrSpace = camera.getScreenSpace().clone();
        
        setGridOn(false);
        setCursorOn(false);
        
    	MHBitmapImage result = MHPlatform.createImage(camera.getWorldSpace().width, camera.getWorldSpace().height);
//    	int rows = this.getMapData().getWorldHeight();
//    	int columns = this.getMapData().getWorldWidth();
    	
    	camera.setScreenSpace(0, 0, camera.getWorldSpace().width, camera.getWorldSpace().height);
        camera.setAnchor(0, 0);
    	render(result.getGraphicsCanvas());
    	
    	setGridOn(gridMode);
    	setCursorOn(cursorMode);
    	camera.setScreenSpace(scrSpace);
    	
    	return result;
    }


    /****************************************************************
     * Performs validation on the cursor's map cell address to ensure
     * that it is within the bounds of the map.
     */
    private void clipCursorAddress()
    {
    	// clip cursor to tile map
    	if (cursorAddress.column < 0)
    		cursorAddress.column = 0;
    	else if (cursorAddress.column > mapData.getWorldWidth() - 1)
    		cursorAddress.column = mapData.getWorldWidth() - 1;
    
    	if (cursorAddress.row < 0)
    		cursorAddress.row = 0;
    	else if (cursorAddress.row > mapData.getWorldHeight() - 1)
    		cursorAddress.row = mapData.getWorldHeight() - 1;
    }

    public void onMouseMoved(final MHMouseTouchEvent e)
    {
    	if (!isScreenCoordinate(e.getPoint())) return;
    	
    	// Convert to local coordinates.
    	cursorPoint = screenToLocal(e.getPoint());
    	
    	// map the mouse coordinates
    	setCursorAddress(mapMouse(e.getPoint()));
    }

    private void checkForScroll()
    {
    	if (!mouseScroll) return;
    	
    	MHVector mouse = MHInputEventHandler.getInstance().getMousePosition();
    	
    	int vSpeed = 0;
    	int hSpeed = 0;
    	if (this.scrollDownZone.contains(mouse))
    		vSpeed = (int) (mouse.getY() - scrollDownZone.y);
    	else if (this.scrollUpZone.contains(mouse))
    		vSpeed = (int) (mouse.getY() - (scrollUpZone.y + scrollUpZone.height));
    	
    	if (this.scrollLeftZone.contains(mouse))
    		hSpeed = (int) (mouse.getX() - (scrollLeftZone.x + scrollLeftZone.width));
    	else if (this.scrollRightZone.contains(mouse))
    		hSpeed = (int) (mouse.getX() - scrollRightZone.x);
    
    	
    	scrollMap(hSpeed, vSpeed);
    }

    /****************************************************************
     * Converts from screen coordinates to a coordinate relative to
     * the region displaying this tile map view.  It is important to
     * note that if the tile map view is positioned at (0, 0) on the
     * screen, then the return value of this method will equal the
     * input since no conversion is necessary in that case.
     * 
     * @param screen A vector containing a coordinate on screen.
     * 
     * @return A vector containing the input convert to a coordinate
     *         relative to the tile map view.
     */
    public MHVector screenToLocal(MHVector screen)
    {
    	// Convert to local coordinates.
    	return camera.screenToLocal(screen);
    }

    
    /**
     * Returns the screenSpace.
     * @return Rectangle2D
     */
    public MHRectangle getScreenSpace()
    {
    	return camera.getScreenSpace();
    }

    public MHMapCellAddress getCursorAddress()
    {
    	return cursorAddress;
    }


    /**
     * Returns the cursorPoint.
     * @return Point
     */
    public MHVector getCursorPoint()
    {
    	return cursorPoint;
    }

    public boolean isGridOn()
    {
    	return gridOn;
    }

    public void setGridOn(boolean gridOn)
    {
    	this.gridOn = gridOn;
    }

    public void setCursorOn(boolean cursorOn)
    {
        this.cursorOn = cursorOn;
    }

    public boolean isCursorOn()
    {
        return cursorOn;
    }

    public boolean isIdOn()
    {
        return idOn;
    }

    public void setIdOn(boolean idOn)
    {
        this.idOn = idOn;
    }

    
    public abstract void render(MHGraphicsCanvas g);


    public boolean canScrollLeft()
    {
        return camera.canScrollLeft();
    }

    
    public boolean canScrollRight()
    {
        return camera.canScrollRight();
    }

    
    public boolean canScrollUp()
    {
        return camera.canScrollUp();
    }

    
    public boolean canScrollDown()
    {
        return camera.canScrollDown();
    }


    public MHVector screenToWorld(double x, double y)
    {
        return camera.screenToWorld(x, y);
    }


    public boolean isScreenCoordinate(MHVector point)
    {
        return camera.isScreenCoord(point);
    }


    public MHVector plotActor(MHBitmapImage image, int r, int c)
    {
        return plotter.plotActor(image, r, c);
    }
}
