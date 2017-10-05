package com.mhframework.platform.pc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHVector;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHMouseTouchEvent;

public class MHPCInputEventHandler implements KeyListener, MouseListener, MouseMotionListener
{
    public MHPCInputEventHandler(JFrame appFrame)
    {
        appFrame.addKeyListener(this);
        appFrame.addMouseListener(this);
        appFrame.addMouseMotionListener(this);
    }

    
    private MHKeyEvent parseKeyEvent(KeyEvent e)
    {
        int code = e.getKeyCode();
        boolean shift = e.isShiftDown();
        boolean control = e.isControlDown();
        boolean alt = e.isAltDown();
        
        return new MHKeyEvent(code, shift, control, alt);
    }
    
    
    private MHMouseTouchEvent parseMouseEvent(MouseEvent e)
    {
        MHVector v = MHPlatform.getDisplayOrigin();
        int x = (int)(e.getX()-v.getX());
        int y = (int)(e.getY()-v.getY());
        return new MHMouseTouchEvent(x, y, e.isPopupTrigger(), MHScreenManager.getInstance().getCurrentScreen());
    }
    
    
    public void keyPressed(KeyEvent e)
    {
        MHInputEventHandler.getInstance().onKeyDown(parseKeyEvent(e));
    }

    
    public void keyReleased(KeyEvent e)
    {
        MHInputEventHandler.getInstance().onKeyUp(parseKeyEvent(e));
    }

    
    public void keyTyped(KeyEvent e)
    {
        // Should this method be used?  What should it do?
    }


    public void mouseClicked(MouseEvent e)
    {
        // Should this method be used?  What should it do?
    }


    public void mouseEntered(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseMoved(parseMouseEvent(e));
    }


    public void mouseExited(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseMoved(parseMouseEvent(e));
    }


    public void mousePressed(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseDown(parseMouseEvent(e));
    }


    public void mouseReleased(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseUp(parseMouseEvent(e));
    }


    public void mouseDragged(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseMoved(parseMouseEvent(e));
    }


    public void mouseMoved(MouseEvent e)
    {
        MHInputEventHandler.getInstance().onMouseMoved(parseMouseEvent(e));
    }
}
