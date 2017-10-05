package com.mhframework.gameplay.tilemap.view;

import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.tilemap.MHMapCellAddress;
import com.mhframework.gameplay.tilemap.view.MHRectangularMapView;
import com.mhframework.platform.graphics.MHBitmapImage;


/********************************************************************
 * Based on IsoTilePlotter.cpp by Ernest Pazera.
 * 
 */
public class MHTilePlotter
{
    private MHRectangularMapView.Type mapType = MHRectangularMapView.Type.RECTANGULAR;
    private int tileWidth=1, tileHeight=1;
    private ITilePlotter plotter = new RectangularPlotter();
    
    private MHTilePlotter()
    {
        
    }

    public static MHTilePlotter create(MHRectangularMapView.Type mapType)
    {
        MHTilePlotter p = new MHTilePlotter();
        p.mapType = mapType;
        
        if (mapType == MHRectangularMapView.Type.RECTANGULAR)
            p.plotter = p.new RectangularPlotter();
        else if (mapType == MHRectangularMapView.Type.DIAMOND)
            p.plotter = p.new DiamondPlotter();
        else if (mapType == MHRectangularMapView.Type.STAGGERED)
            p.plotter = p.new StaggeredPlotter();
        
        return p;
    }
    
    
    public MHRectangularMapView.Type getMapType()
    {
        return mapType;
    }
    
    
    public void setTileSize(int w, int h)
    {
        tileWidth = w;
        tileHeight = h;
    }
    
    
    public int getTileWidth()
    {
        return tileWidth;
    }

    
    public int getTileHeight()
    {
        return tileHeight;
    }

    /****************************************************************
     * Returns world space coordinates where a tile at the input
     * location would be rendered.
     * 
     * @param row
     * @param column
     * @return
     */
    public MHVector plotTile(int row, int column)
    {
        return plotter.plotTile(row, column, tileWidth, tileHeight);
    }

    
    /****************************************************************
     * Returns world space coordinates where a tile at the input
     * location would be rendered.
     * 
     * @param gridLocation
     * @return
     */
    public MHVector plotTile(MHMapCellAddress gridLocation)
    {
        return plotter.plotTile(gridLocation.row, gridLocation.column, tileWidth, tileHeight);
    }


    /****************************************************************
     * Plots the coarse position in world coordinates where the input
     * image should be rendered.
     * 
     * @param image
     * @param row
     * @param column
     * @return
     */
    public MHVector plotActor(MHBitmapImage image, int row, int column)
    {
        MHVector screenPoint = plotTile(row, column);

        screenPoint.x += (getTileWidth() / 2) - (image.getWidth() / 2);
        screenPoint.y += getTileHeight() - image.getHeight();
        
        return screenPoint;
    }

    
    /****************************************************************
     * 
     */
    private interface ITilePlotter
    {
        public MHVector plotTile(int row, int column, int tileWidth, int tileHeight);
    }
    
    
    /****************************************************************
     * 
     */
    private class RectangularPlotter implements ITilePlotter
    {
        @Override
        public MHVector plotTile(int row, int column, int tileWidth,
                int tileHeight)
        {
            //point plotted
            MHVector ptPlot = new MHVector();

            //plot x
            ptPlot.x = column * tileWidth;

            //plot y
            ptPlot.y = row * tileHeight;

            //return plotted coordinate
            return ptPlot;
        }
    }
    
    
    /****************************************************************
     * 
     */
    private class DiamondPlotter implements ITilePlotter
    {
        @Override
        public MHVector plotTile(int row, int column, int tileWidth,
                int tileHeight)
        {
            //point plotted
            MHVector ptPlot = new MHVector();

            //plot x
            ptPlot.x = (column - row) * (tileWidth>>1);

            //plot y
            ptPlot.y= (column+row) * (tileHeight>>1);

            //return plotted coordinate
            return ptPlot;
        }
    }
    
    
    /****************************************************************
     * 
     */
    private class StaggeredPlotter implements ITilePlotter
    {
        @Override
        public MHVector plotTile(int row, int column, int tileWidth,
                int tileHeight)
        {
            //point plotted
            MHVector ptPlot = new MHVector();

            //plot x
            ptPlot.x = column * tileWidth + (row & 1) * (tileWidth/2);

            //plot y
            ptPlot.y = row * (tileHeight>>1);

            //return plotted coordinate
            return ptPlot;
        }
    }

    
    /****************************************************************
     * 
     */
    private class SlidePlotter implements ITilePlotter
    {

        @Override
        public MHVector plotTile(int row, int column, int tileWidth,
                int tileHeight)
        {
            //point plotted
            MHVector ptPlot = new MHVector();

            //plot x
            ptPlot.x = column * tileWidth + row * (tileWidth/2);

            //plot y
            ptPlot.y = row * (tileHeight/2);

            //return plotted coordinate
            return ptPlot;
        }
        
    }


    /****************************************************************
     * 
     */
    private class HexPlotter implements ITilePlotter
    {

        @Override
        public MHVector plotTile(int row, int column, int tileWidth,
                int tileHeight)
        {
            return null;
        }
        
    }

}
