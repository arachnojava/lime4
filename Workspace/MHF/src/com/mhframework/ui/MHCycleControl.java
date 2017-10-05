package com.mhframework.ui;

import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.event.MHButtonListener;

public class MHCycleControl extends MHGuiComponent implements MHButtonListener 
{
    private MHButton btnPrevious;
    private MHButton btnNext;
    private MHLabel lblValue;
    private Object[] values;
    private int index = 0;

    protected MHCycleControl()
    {
        setSize(100, 25);

        btnPrevious = getPreviousButton();
        btnNext = getNextButton();
    }


    public void setValues(final Object[] values)
    {
        this.values = values;
        if (index >= values.length)
            index = values.length-1;
        getLabel().setText(values[index].toString());
    }


    public void setLabelColor(final MHColor color)
    {
        getLabel().normalColors.foregroundColor = color;
    }


    protected MHButton getPreviousButton()
    {
        if (btnPrevious == null)
        {
            btnPrevious = MHButton.create("<");
            btnPrevious.addButtonListener(this);
        }

        return btnPrevious;
    }


    protected MHButton getNextButton()
    {
        if (btnNext == null)
        {
            btnNext = MHButton.create(">");
            btnNext.addButtonListener(this);
        }

        return btnNext;
    }


    protected MHLabel getLabel()
    {
        if (lblValue == null)
        {
            lblValue = new MHLabel("");
            lblValue.setAlignment(MHLabel.ALIGN_CENTER);
            lblValue.setColorSet(focusedColors);
        }
        return lblValue;
    }


    @Override
    public void update(long elapsedTime)
    {
        getLabel().setText(values[index].toString());
    }


    @Override
    public void setSize(final int w, final int h)
    {
        super.setSize(w, h);

        getPreviousButton().setHeight(h);
        getNextButton().setHeight(h);
        getLabel().setHeight(h);
    }


    public void setHeight(final int h)
    {
        setSize(getWidth(), h);
    }


    @Override
    public void render(final MHGraphicsCanvas g)
    {
        validateWidth(g);

        //getLabel().centerOn(getBounds(), g);
        getLabel().render(g);

        g.setColor(this.normalColors.backgroundColor);
        g.fillRect(getBounds());

        //getLabel().centerOn(getBounds(), g);
        getLabel().render(g);

        g.setColor(normalColors.borderColor);
        g.drawRect(getBounds());
        
        btnPrevious.render(g);
        btnNext.render(g);
    }


    private void validateWidth(final MHGraphicsCanvas g)
    {
        // Make the buttons the same size, then give the label
        // whatever room is left.

            final int prevWidth = (int)(btnPrevious.getFont().stringWidth(btnPrevious.getText()) * 1.1);
            final int nextWidth = (int)(btnNext.getFont().stringWidth(btnNext.getText()) * 1.1);
            int buttonWidth;
            if (prevWidth > nextWidth)
                buttonWidth = prevWidth;
            else
                buttonWidth = nextWidth;

            btnPrevious.setWidth(buttonWidth);
            btnNext.setWidth(buttonWidth);

            lblValue.setWidth(getWidth() - buttonWidth*2);

            btnPrevious.setPosition(getX(), getY());
            lblValue.setPosition(btnPrevious.getX()+btnPrevious.getWidth(), getY());
            btnNext.setPosition(getX()+getWidth()-buttonWidth, getY());
    }


    public int getSelectedIndex()
    {
        return index;
    }


    public void setSelectedIndex(final int indexNumber)
    {
        if (index < 0)
            index = 0;
        else if (index >= values.length)
            index = values.length - 1;
        else
            index = indexNumber;
    }


    public Object getSelectedValue()
    {
        if (index < 0)
            index = values.length - 1;
        else if (index >= values.length)
            index = 0;
        
        return values[index];
    }


    protected void decrement()
    {
        index--;
        if (index < 0)
            index = values.length - 1;
    }


    protected void increment()
    {
        index++;
        if (index >= values.length)
            index = 0;
    }

    
    public void setLabelVisible(final boolean show)
    {
        getLabel().setVisible(show);
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
	public void onMouseDown(MHMouseTouchEvent e) 
	{
		btnNext.onMouseDown(e);
		btnPrevious.onMouseDown(e);
	}


	@Override
	public void onMouseUp(MHMouseTouchEvent e) 
	{
		btnNext.onMouseUp(e);
		btnPrevious.onMouseUp(e);
	}


	@Override
	public void onMouseMoved(MHMouseTouchEvent e) 
	{
        btnNext.onMouseMoved(e);
        btnPrevious.onMouseMoved(e);
	}


	@Override
	public void onButtonPressed(MHButton button, MHMouseTouchEvent e) 
	{
        if (button == btnPrevious)
            decrement();
        else if (button == btnNext)
            increment();
	}


    public static MHCycleControl create()
    {
        return new MHCycleControl();
    }


    public void setFont(MHFont font)
    {
        getLabel().setFont(font);
    }
}