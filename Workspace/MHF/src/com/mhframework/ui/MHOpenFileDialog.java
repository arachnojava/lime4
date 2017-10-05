package com.mhframework.ui;

import java.io.File;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.io.MHFileFilter;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.event.MHButtonListener;

public class MHOpenFileDialog extends MHDialogBox implements MHButtonListener
{
    private String path, extension;
    private MHButton btnCancel;
    private String selectedFileName = null;
    private boolean finished;
    
    private MHOpenFileDialog(MHScreen parentScreen, String path)
    {
        super(parentScreen, "Showing files in "+path, "Open File");
    }


    public static MHOpenFileDialog create(MHScreen parentScreen, String path, String extension)
    {
        MHOpenFileDialog instance = new MHOpenFileDialog(parentScreen, path);

        instance.path = path;
        instance.extension = extension;

        instance.createButtons();
        
        return instance;
    }
    
    
    private void createButtons()
    {
        getGuiComponents().clear();
        btnCancel = MHButton.create("Cancel");
        btnCancel.addButtonListener(this);
        btnCancel.setSize(120, 30);
        btnCancel.setPosition(getTitleBounds().x+calculateBoxWidth() - btnCancel.getWidth()-10, getTitleBounds().y+calculateBoxHeight() - btnCancel.getHeight() - 10);
        add(btnCancel);

        calculateBoxDimensions();
        File[] files = MHFileFilter.listFiles(path, extension);
        int spacing = 2;
        int columns = 4;
        int buttonWidth = (boxWidth - spacing * (columns+1))/columns;
        int buttonHeight = btnCancel.getHeight();
        int x = getTitleBounds().x + spacing;
        int y = getTitleBounds().y+ getTitleBounds().height + 20;
        for (int i = 0; i < files.length; i++)
        {
            MHButton btn = MHButton.create(files[i].getName());
            btn.addButtonListener(this);
            btn.setSize(buttonWidth, buttonHeight);
            btn.setPosition(x, y);
            add(btn);
            
            y += buttonHeight + spacing;
            if (y > getTitleBounds().y + boxHeight - btnCancel.getHeight() - 20)
            {
                y = getTitleBounds().y+ getTitleBounds().height + 20;
                x += buttonWidth + spacing;
            }
                
        }
        
    }


    @Override
    protected void calculateBoxDimensions()
    {
        preferredWidth = 640;
        boxWidth = 640;
        boxHeight = 480;
    }


    @Override
    public void render(MHGraphicsCanvas g)
    {
        // TODO Auto-generated method stub
        super.render(g);
        
        
    }

    
    protected int calculateBoxWidth()
    {
        boxWidth = 640;
        return boxWidth;
    }
    

    @Override
    protected int calculateBoxHeight()
    {
        boxHeight = 480;
        return boxHeight;
    }
    
    
    @Override
    public void onButtonPressed(MHButton button, MHMouseTouchEvent e)
    {
        if (button == btnCancel)
        {
            selectedFileName = null;
        }
        else
        {
            selectedFileName = path + "/"+ button.getText();
        }

        finished = true;
        
        MHScreenManager.getInstance().changeScreen(getPreviousScreen());
    }

    
    public String getSelectedFileName()
    {
        return selectedFileName;
    }

    
    @Override
    public void onKeyUp(MHKeyEvent e)
    {
    }

    
    public void onMouseUp(MHMouseTouchEvent e)
    {
        getGuiComponents().onMouseUp(e);
    }


    public boolean isFinished()
    {
        // TODO Auto-generated method stub
        return finished;
    }
}
