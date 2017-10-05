package com.mhframework.ui;

import com.mhframework.MHGame;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyCodes;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHInputDialogBox extends MHDialogBox
{
    private MHLabel txtInput;
    private StringBuffer input;
    private boolean cursorFlash;
    private long flashTime;
    
    public MHInputDialogBox(MHScreen parentScreen, String text)
    {
        super(parentScreen, text);
    }


    public MHInputDialogBox(MHScreen parentScreen, String text, String titleText)
    {
        super(parentScreen, text, titleText);
    }


    public MHInputDialogBox(MHScreen parentScreen, String text, MHFont font)
    {
        super(parentScreen, text, font);
    }


    public MHInputDialogBox(MHScreen parentScreen, String text, String title,
            MHFont font, MHFont titleFont)
    {
        super(parentScreen, text, title, font, titleFont);
    }
    
    
    
    
    
    private MHLabel getInputBox()
    {
        if (txtInput == null)
        {
            txtInput = new MHLabel(" ");
            txtInput.setFont(textFont);
            this.add(txtInput);
        }
        
        return txtInput;
    }

    
    @Override
    public void render(MHGraphicsCanvas g)
    {
        super.render(g);
        if (cursorFlash)
        {
            if (input == null)
                input = new StringBuffer();
            
            int h = textFont.getHeight();
            int x = getTitleBounds().x + 10 + textFont.stringWidth(input.toString());
            int y = getTitleBounds().y + boxHeight - 10 - h;
            
            g.setColor(MHColor.BLUE);
            g.drawLine(x, y, x, y+h);
        }
        
    }

    
    public void onKeyUp(MHKeyEvent e)
    {
        // This is here to prevent key presses from closing the dialog while
        // the user is typing.
    }
    
    
    @Override
    public void onKeyDown(MHKeyEvent e)
    {
        if (input == null)
            input = new StringBuffer();
        
        final char keyChar = e.getKeyChar();
        final int  keyCode = e.getKeyCode();

        final MHKeyCodes codes = MHPlatform.getKeyCodes();
        
        if (isAccepted(keyCode, keyChar))
            input.append(keyChar);
        else if (keyChar == ' ' || keyChar == '\t')
            input.append(' ');
        else if (keyCode == codes.keyBackspace())
        {
            if (input.length() > 0)
                input.deleteCharAt(input.length()-1);
        }
        else if (keyCode == codes.keyEnter())
        {
            MHScreenManager.getInstance().changeScreen(getPreviousScreen());
        }
        else if (keyCode == codes.keyEscape())
        {
            input.setLength(0);
            MHScreenManager.getInstance().changeScreen(getPreviousScreen());
        }
        else
            super.onKeyDown(e);

        getInputBox().setText(input.toString());
    }

    
    public String getInput()
    {
        return getInputBox().getText();
    }
    

    @Override
    protected int calculateBoxHeight()
    {
        boxHeight = super.calculateBoxHeight();
        // Factor in the height of the input box.
        int inputHeight = super.textFont.getHeight() + 10;
        getInputBox().setHeight(inputHeight);
        boxHeight += inputHeight;
        
        return boxHeight;
    }

    
    private boolean isAccepted(final int keyCode, final char keyChar)
    {
        final int type = Character.getType(keyCode);
        return Character.isLetterOrDigit(keyChar)
            || type == Character.CONNECTOR_PUNCTUATION
            || type == Character.OTHER_PUNCTUATION
            || keyChar == '.'
            || keyChar == '\''
            || keyChar == '-';
    }


    @Override
    public void update(long elapsedTime)
    {
        calculateBoxDimensions();
        
        MHRectangle boxBounds = new MHRectangle();
        boxBounds.x = getTitleBounds().x + 5;
        boxBounds.height = this.calculateBoxHeight();
        boxBounds.width = this.preferredWidth-5;
        boxBounds.y = getTitleBounds().y + boxBounds.height - (textFont.getHeight() + 15);
        
        getInputBox().setBounds(boxBounds);
        
        if (MHGame.getGameTimerValue() - flashTime > 500)
        {
            cursorFlash = !cursorFlash;
            flashTime = MHGame.getGameTimerValue();
        }
        
        super.update(elapsedTime);
    }


    @Override
    public void onMouseUp(MHMouseTouchEvent e)
    {
        // Do nothing because this is a keyboard-oriented interface.
    }   
}
