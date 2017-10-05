package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;

public class MHCamera2D
{
    public enum WrapMode
    {
        NONE, CLIP, WRAP;
    }

    private MHRectangle screenSpace;
    private MHRectangle worldSpace;
    private MHRectangle anchorSpace;
    private MHVector anchor = new MHVector();
    private WrapMode hWrapMode = WrapMode.CLIP, vWrapMode = WrapMode.CLIP;

    public MHRectangle getScreenSpace()
    {
        return screenSpace;
    }

    public void setScreenSpace(MHRectangle rect)
    {
        screenSpace = rect;
    }

    public void adjustScreenSpace(int leftAdjust, int topAdjust, int rightAdjust, int bottomAdjust)
    {
        screenSpace.x += leftAdjust;
        screenSpace.y += topAdjust;
        screenSpace.width += rightAdjust;
        screenSpace.height += bottomAdjust;
    }

    public MHRectangle getWorldSpace()
    {
        return worldSpace;
    }

    public void setWorldSpace(MHRectangle rect)
    {
        worldSpace = rect;
    }

    public void adjustWorldSpace(int leftAdjust, int topAdjust, int rightAdjust, int bottomAdjust)
    {
        worldSpace.x += leftAdjust;
        worldSpace.y += topAdjust;
        worldSpace.width += rightAdjust;
        worldSpace.height += bottomAdjust;
    }


    public void calculateWorldSpace(MHTilePlotter tilePlotter, int mapRows, int mapColumns)
    {
        //set worldspace rect to empty
        worldSpace = new MHRectangle();

        int left=0, right=0, top=0, bottom=0;

        //temporary rectangle
        MHRectangle temp = new MHRectangle(0, 0, tilePlotter.getTileWidth(), tilePlotter.getTileHeight());

        //point for plotting
        MHVector ptPlot = new MHVector();

        //map point
        MHMapCellAddress ptMap = new MHMapCellAddress();

        //loop through map positions
        for(ptMap.column=0;ptMap.column<mapColumns;ptMap.column++)
        {
            for(ptMap.row=0;ptMap.row<mapRows;ptMap.row++)
            {
                //plot the map point
                ptPlot=tilePlotter.plotTile(ptMap);

                //adjust the temp rect
                temp.x += (int)ptPlot.x;
                temp.y += (int)ptPlot.y;

                //expand the boundaries of worldspace
                left = Math.min(left, temp.x);
                top = Math.min(top, temp.y);
                right = Math.max(right, temp.right());
                bottom = Math.max(bottom, temp.bottom());

                //adjust the temp rect back
                temp.x -= (int)ptPlot.x;
                temp.y -= (int)ptPlot.y;
            }
        }

        worldSpace.setRect(left, top, right-left, bottom-top);
    }


    public MHRectangle getAnchorSpace()
    {
        return anchorSpace;
    }


    public void setAnchorSpace(MHRectangle rect)
    {
        anchorSpace = rect;
    }


    public void adjustAnchorSpace(int leftAdjust, int topAdjust, int rightAdjust, int bottomAdjust)
    {
        anchorSpace.x += leftAdjust;
        anchorSpace.y += topAdjust;
        anchorSpace.width += rightAdjust;
        anchorSpace.height += bottomAdjust;
    }


    public void calculateAnchorSpace()
    {
        //copy worldspace
        anchorSpace = worldSpace.clone();

        //subtract out screen space
        if (getHWrapMode() != WrapMode.WRAP) anchorSpace.width -= screenSpace.width;
        if (getVWrapMode() != WrapMode.WRAP) anchorSpace.height -= screenSpace.height;

        if (anchorSpace.width < 1) anchorSpace.width = 1;
        if (anchorSpace.height < 1) anchorSpace.height = 1;
    }



    public void setHWrapMode(WrapMode hWrapMode)
    {
        this.hWrapMode = hWrapMode;
    }

    public void setVWrapMode(WrapMode vWrapMode)
    {
        this.vWrapMode = vWrapMode;
    }

    public WrapMode getHWrapMode()
    {
        return hWrapMode;
    }

    public WrapMode getVWrapMode()
    {
        return vWrapMode;
    }

    public MHVector getAnchor()
    {
        return anchor;
    }

    public void setAnchor(MHVector anchor)
    {
        setAnchor(anchor.x, anchor.y);

    }

    public void setAnchor(double x, double y)
    {
        anchor.x = x;
        anchor.y = y;

        wrapAnchor();
    }


    public void moveAnchor(MHVector v)
    {
        moveAnchor(v.x, v.y);
    }


    public void moveAnchor(double x, double y)
    {
        anchor.x += x;
        anchor.y += y;
        wrapAnchor();
    }


    private void wrapAnchor()
    {
        //horizontal wrapping
        switch(hWrapMode)
        {
        case CLIP:
        {
            //clip to left
            if (anchor.x < anchorSpace.left()) anchor.x = anchorSpace.left();
            //clip to right
            if (anchor.x >= anchorSpace.right()) anchor.x = anchorSpace.right()-1;
        }break;
        case WRAP:
        {
            //left wrapping
            while (anchor.x < anchorSpace.left()) anchor.x += anchorSpace.width;
            //right wrapping
            while (anchor.x >= anchorSpace.right()) anchor.x -= anchorSpace.width;
        }break;
        default:
            break;
        }
        //vertical wrapping
        switch(vWrapMode)
        {
        case CLIP:
        {
            //clip to top
            if (anchor.y < anchorSpace.y) anchor.y = anchorSpace.y;
            //clip to bottom
            if (anchor.y >= anchorSpace.bottom()) anchor.y = anchorSpace.bottom()-1;
        }break;
        case WRAP:
        {
            //top wrapping
            while (anchor.y < anchorSpace.y) anchor.y += anchorSpace.height;
            //bottom wrapping
            while (anchor.y >= anchorSpace.bottom()) anchor.y -= anchorSpace.height;
        }break;
        default:
            break;
        }
    }

    //conversion
    //screen->world
    public MHVector screenToWorld(MHVector ptScreen)
    {
        ptScreen = screenToLocal(ptScreen);
        
        //translate into plotspace coordinates
        ptScreen.x -= screenSpace.x;
        ptScreen.y -= screenSpace.y;

        //translate into world coordinates
        ptScreen.x += anchor.x;
        ptScreen.y += anchor.y;

        //return coordinates
        return ptScreen;
    }

    //world->screen
    public MHVector worldToScreen(MHVector ptWorld)
    {
        //translate into plotspace coordinates
        ptWorld.x -= anchor.x;
        ptWorld.y -= anchor.y;

        //translate into screen coordinates
        ptWorld.x += screenSpace.x;
        ptWorld.y += screenSpace.y;

        //return cooridinates
        return ptWorld;
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
        return new MHVector(screen.x - getScreenSpace().x, screen.y - getScreenSpace().y);
    }

    //validation
    //world
    public boolean isWorldCoord(MHVector ptWorld)
    {
        //check for valid coordinate
        return worldSpace.contains(ptWorld);
    }

    //screen
    public boolean isScreenCoord(MHVector ptScreen)
    {
        //check for valid coordinate
        return screenSpace.contains(ptScreen);
    }

    //anchor
    public boolean isAnchorCoord(MHVector ptAnchor)
    {
        //check for valid coordinate
        return anchorSpace.contains(ptAnchor);
    }

    public boolean canScrollLeft()
    {
        return (anchor.x - 1 >= anchorSpace.left());
    }

    
    public boolean canScrollRight()
    {
        return (anchor.x + 1 < anchorSpace.right());
    }

    
    public boolean canScrollUp()
    {
        return (anchor.y - 1 >= anchorSpace.top());
    }

    
    public boolean canScrollDown()
    {
        return (anchor.y + 1 < anchorSpace.bottom());
    }

    public MHVector screenToWorld(double x, double y)
    {
        return screenToWorld(new MHVector(x, y));
    }

    public void setScreenSpace(int x, int y, int width, int height)
    {
        screenSpace.setRect(x, y, width, height);
    }
    
}
