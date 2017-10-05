package com.mhframework.ui;

import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHLabel extends MHGuiComponent
{
    public static final int ALIGN_LEFT   = -1;
    public static final int ALIGN_CENTER =  0;    
    public static final int ALIGN_RIGHT  =  1;
    
    private int textMargin = 5;
    private String text;
    private String[] lines;
    private MHFont font;
    private int alignment = ALIGN_LEFT;
    
    protected MHLabel(String caption)
    {
        text = caption;
        this.setVisible(true);
        this.setEnabled(true);
        this.setFocusable(false);
        this.setFont(MHFont.getDefaultFont());
    }
    
    
    public static MHLabel create(String caption)
    {
        return new MHLabel(caption);
    }

    
    public void render(MHGraphicsCanvas g)
    {
        this.render(g, getX(), getY());
    }

    
    public void resize()
    {
        int currentWidth = this.getWidth();
        int currentHeight = this.getHeight();
        
        int width = this.getWidth() - textMargin*2;

        lines = font.splitLines(text, width);
        int numLines = Math.max(1, lines.length);
        
        for (int i = 0; i < lines.length; i++)
            width = Math.max(width, font.stringWidth(lines[i]));

        width += textMargin*2;
        int height = font.getHeight() * numLines + textMargin*2;

        width = Math.max(width, currentWidth);
        height = Math.max(height, currentHeight);
        
        super.setBounds(getX(), getY(), width, height);
    }
    
    
    public void setFont(MHFont font)
    {
        this.font = font;
        resize();
    }
    
    
    public MHFont getFont()
    {
    	return font;
    }
    
    
    public void setText(String caption)
    {
        text = caption;
        resize();
    }
    
    
    public String getText()
    {
    	return text;
    }
    

    @Override
    public void render(MHGraphicsCanvas g, int x, int y)
    {
        g.setFont(font);
        resize();
        super.render(g);
        
        int x0 = 0;//(int) MHScreenManager.getDisplayOrigin().getX();
        int y0 = 0;//(int) MHScreenManager.getDisplayOrigin().getY();
        int ty = y+textMargin+font.getHeight();
        for (String s : lines)
        {
            // if left aligned...
            int tx = x+textMargin;
            
            switch (alignment)
            {
                case ALIGN_RIGHT:
                    tx = x + getWidth() - textMargin - font.stringWidth(s);
                    break;
                case ALIGN_CENTER:
                    tx = x + (getWidth()/2) - (font.stringWidth(s)/2);
                    break;
            }
            
            g.drawString(s, x0+tx, y0+ty);
            ty += font.getHeight();
        }
    }


    /**
     * @return the alignment
     */
    public int getAlignment()
    {
        return alignment;
    }


    /**
     * @param alignment the alignment to set
     */
    public void setAlignment(int alignment)
    {
        this.alignment = alignment;
    }


	@Override
	public void onKeyDown(MHKeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onKeyUp(MHKeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseDown(MHMouseTouchEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseMoved(MHMouseTouchEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void centerOn(MHRectangle bounds, MHGraphicsCanvas g) 
	{
        if (text.length() < 1)
            text = " ";

        MHVector p = font.centerOn(getBounds(), g, text);

        int width = Math.max(this.getWidth(), font.stringWidth(text));
        int height = Math.max(this.getHeight(), font.getHeight());
        
        setBounds((int)p.x, (int)p.y, width, height);
    }


	public void setHeight(int h) 
	{
		this.setBounds(getX(), getY(), getWidth(), h);
	}


	public void setWidth(int w) 
	{
		this.setBounds(getX(), getY(), w, getHeight());
	}
}
