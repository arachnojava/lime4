package com.test;

import com.mhframework.MHGame;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.MHTile;
import com.mhframework.gameplay.tilemap.MHTileGrid;
import com.mhframework.gameplay.tilemap.MHTileMapDirection;
import com.mhframework.gameplay.tilemap.view.MHCamera2D;
import com.mhframework.gameplay.tilemap.view.MHCamera2D.WrapMode;
import com.mhframework.gameplay.tilemap.view.MHIsoMouseMap;
import com.mhframework.gameplay.tilemap.view.MHRectangularMapView;
import com.mhframework.gameplay.tilemap.view.MHTilePlotter;
import com.mhframework.gameplay.tilemap.view.MHTileWalker;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class TileMapTestScreen extends MHScreen
{

    MHTilePlotter plotter;
    MHTileWalker walker;
    MHCamera2D camera;
    MHIsoMouseMap mouseMap = MHIsoMouseMap.getInstance();
    MHTileGrid map;
    MHRectangularMapView.Type type = MHRectangularMapView.Type.DIAMOND;
    
    MHVector cameraPan = new MHVector();
    String debugMsg = "";
    
    public TileMapTestScreen()
    {
    }
    
    public void load()
    {
        MHBitmapImage tileImage = MHPlatform.createImage(64, 32);
        tileImage.getGraphicsCanvas().fill(MHPlatform.createColor(0, 0, 0, 0));
        tileImage.getGraphicsCanvas().setColor(MHColor.WHITE);
        tileImage.getGraphicsCanvas().drawLine(32, 0, 63, 15);
        tileImage.getGraphicsCanvas().drawLine(63, 16, 32, 31);
        tileImage.getGraphicsCanvas().drawLine(31, 31, 0, 16);
        tileImage.getGraphicsCanvas().drawLine(0, 15, 31, 0);
        
        MHTile tile = new MHTile(0, tileImage);
        
        map = new MHTileGrid(100, 100);
        for (int r = 0; r < map.getNumRows(); r++)
            for (int c = 0; c < map.getNumColumns(); c++)
                map.putTile(tile, r, c);
        
        plotter = MHTilePlotter.create(type);
        plotter.setTileSize(tile.image.getWidth(), tile.image.getHeight());
        walker = MHTileWalker.create(type);

        mouseMap.setTileSize(64);
        mouseMap.calculateReferencePoint(plotter);
        mouseMap.setCamera(camera);
        mouseMap.setPlotter(plotter);
        mouseMap.setWalker(walker);
        
        MHRectangle screenSpace = new MHRectangle(0, 0, MHScreenManager.getDisplayWidth(), MHScreenManager.getDisplayHeight());
        camera = new MHCamera2D();
        camera.setHWrapMode(WrapMode.CLIP);
        camera.setVWrapMode(WrapMode.CLIP);
        camera.setScreenSpace(screenSpace);
        camera.calculateWorldSpace(plotter, map.getNumRows(), map.getNumColumns());
        camera.calculateAnchorSpace();
    }


    @Override
    public void update(long elapsedTime)
    {
        camera.moveAnchor(cameraPan.multiply(elapsedTime));
        super.update(elapsedTime);
    }


    @Override
    public void render(MHGraphicsCanvas g)
    {
        g.fill(MHColor.BLACK);
        
        //working variables for calculating corners
        MHVector ptScreen = new MHVector(camera.getScreenSpace().x, camera.getScreenSpace().y);
        MHVector ptWorld = camera.screenToWorld(ptScreen);
        MHMapCellAddress ptCoarse = new MHMapCellAddress((int)(ptWorld.y/plotter.getTileHeight()),
                                                         (int)(ptWorld.x/plotter.getTileWidth()));
        MHMapCellAddress ptMap = new MHMapCellAddress();
        MHMapCellAddress upperLeft = new MHMapCellAddress(), 
                upperRight = new MHMapCellAddress(), 
                lowerLeft = new MHMapCellAddress(), 
                lowerRight = new MHMapCellAddress();


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
        ptScreen.x = camera.getScreenSpace().right();
        ptScreen.y = camera.getScreenSpace().top();
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
        ptScreen.x = camera.getScreenSpace().left();
        ptScreen.y = camera.getScreenSpace().bottom();
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
        ptScreen.x = camera.getScreenSpace().right();
        ptScreen.y = camera.getScreenSpace().bottom();
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

        //main rendering loop
        //vars for rendering loop
        MHMapCellAddress ptCurrent;
        MHMapCellAddress ptRowStart;
        MHMapCellAddress ptRowEnd;
        int dwRowCount=0;

        //set up rows
        ptRowStart = upperLeft;
        ptRowEnd = upperRight;

        //start rendering loops
        for(;;)//"infinite" loop
        {
            //set current point to rowstart
            ptCurrent=ptRowStart;

            //render a row of tiles
            for(;;)//'infinite' loop
            {

                //check for valid point. if valid, render
                if(ptCurrent.column>=0 && ptCurrent.row>=0 && ptCurrent.column<map.getNumColumns() && ptCurrent.row<map.getNumRows())
                {
                    //valid, so render
                    ptScreen=plotter.plotTile(ptCurrent);//plot tile
                    ptScreen=camera.worldToScreen(ptScreen);//world->screen

                    g.drawImage(map.getTile(ptCurrent.row, ptCurrent.column).image, (int)ptScreen.x, (int)ptScreen.y);
//                    tsBack.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put background tile
//                    if(iMap[ptCurrent.x][ptCurrent.y])//check for tree
//                    {
//                        tsShadow.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put shadow
//                        tsTree.PutTile(lpddsBack,ptScreen.x,ptScreen.y,0);//put tree
//                    }
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

        //flip to show the back buffer
//        lpddsMain->Flip(0,DDFLIP_WAIT);
//    }
        
        g.setColor(MHColor.GREEN);
        g.drawString("World Space: " + camera.getWorldSpace().toString(), 10, 40);
        g.drawString("Screen Space: " + camera.getScreenSpace().toString(), 10, 60);
        g.drawString("Anchor Space: " + camera.getAnchorSpace().toString(), 10, 80);
        g.drawString("Anchor: " + camera.getAnchor().toString(), 10, 100);
        g.drawString(debugMsg, 10, 120);
        super.render(g);
    }


    @Override
    public void onKeyDown(MHKeyEvent e)
    {
        double speed = 0.5;  // pixels/millisecond
        debugMsg = "Key Down: " + e.getKeyCode();
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyUpArrow())
            cameraPan.y = -speed;
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyDownArrow())
            cameraPan.y = speed;
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyLeftArrow())
            cameraPan.x = -speed;
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyRightArrow())
            cameraPan.x = speed;
    }


    @Override
    public void onKeyUp(MHKeyEvent e)
    {
        debugMsg = "";
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyUpArrow() ||
            e.getKeyCode() == MHPlatform.getKeyCodes().keyDownArrow())
            cameraPan.y = 0;
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyLeftArrow() || 
            e.getKeyCode() == MHPlatform.getKeyCodes().keyRightArrow())
            cameraPan.x = 0;
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyEscape())
            MHGame.setProgramOver(true);
    }
    
    
}
