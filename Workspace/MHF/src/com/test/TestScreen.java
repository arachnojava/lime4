package com.test;
//package com.botz;

import com.mhframework.MHGame;
import com.mhframework.MHScreen;
import com.mhframework.MHScreenManager;
import com.mhframework.core.math.MHRandom;
import com.mhframework.core.math.MHVector;
import com.mhframework.core.math.geom.MHRectangle;
import com.mhframework.core.math.physics.MHPhysicsCore;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHInputEventHandler;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.event.MHMouseTouchEvent;
import com.mhframework.platform.event.MHMouseTouchListener;
import com.mhframework.platform.graphics.MHColor;
import com.mhframework.platform.graphics.MHFont;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.ui.MHButton;
import com.mhframework.ui.MHDropDownMenu;
import com.mhframework.ui.MHLabel;
import com.mhframework.ui.MHPopUpMenu;
import com.mhframework.ui.event.MHButtonListener;




public class TestScreen extends MHScreen implements MHMouseTouchListener, MHButtonListener, MHKeyListener
{
    public static String IMAGES_DIR = "assets/images/";
    MHLabel lblTest,lblMultiLineTest;
    MHButton btnExit;
    MHColor bgColor;
    MHColor groundColor;
    MHColor textColor;
    MHVector position, velocity;
    double speed = 0.1;
    int screenWidth, screenHeight;
    String message = "Test Screen";
    MHPopUpMenu popup;
    MHDropDownMenu menu1, menu2;
    TestSprite sprite;
    
    public TestScreen()
    {
        super("Test Screen");
        MHInputEventHandler.getInstance().addMouseTouchListener(this);
        MHInputEventHandler.getInstance().addKeyListener(this);
    }

    
    @Override
    public void load()
    {
        sprite = new TestSprite();
        sprite.setGravity(true);
        MHPhysicsCore.getInstance().addBody(sprite.getPhysics());
        MHPhysicsCore.getInstance().setMaxFallingSpeed(0.5);
        
        bgColor = MHPlatform.createColor(0, 0, 100, 255);
        groundColor = MHPlatform.createColor(0, 180, 0, 255);
        textColor = MHPlatform.createColor(0, 255, 180, 255);
                
        screenWidth = (int) MHScreenManager.getApplicationObject().getDisplaySize().getX();
        screenHeight = (int) MHScreenManager.getApplicationObject().getDisplaySize().getY();

        
        int x = MHRandom.random(10, screenWidth-20);
        int y = MHRandom.random(10, screenHeight-20);
        position = new MHVector(x, y);
        velocity = new MHVector(MHRandom.random(0.1, 2.0), MHRandom.random(0.1, 2.0));
        velocity = velocity.normalize().multiply(speed);
        lblTest = MHLabel.create("This is a label!");
        lblTest.setBounds(10, 20, 200, 20);
        lblMultiLineTest = MHLabel.create("Here is another label with a lot more text to waste space and test the line wrap.");
        lblMultiLineTest.setBounds(10, lblTest.getY()+lblTest.getHeight() + 5, 200, 20);

        if (btnExit == null)
        {
            btnExit = MHButton.create(MHButton.Type.TEXT, "Exit");
            btnExit.setBounds(new MHRectangle(screenWidth-100, screenHeight - 60, 80, 40));
            btnExit.addButtonListener(this);
            add(btnExit);
        }
        
        //add(lblTest);
        //add(lblMultiLineTest);
        
        
        
//        popup = new MHPopUpMenu();
//        MHButton btn1 = MHButton.create(MHButton.Type.TEXT, "Some Command");
//        btn1.addButtonListener(this);
//        popup.add(btn1);
//        popup.add(MHButton.create(MHButton.Type.TEXT, "Another Command"));
//        popup.add(MHButton.create(MHButton.Type.TEXT, "Do Something Else"));
//        popup.add(MHButton.create(MHButton.Type.TEXT, "No, thanks."));
//        
//        menu1 = new MHDropDownMenu("Menu", 200, 0, 128, 20, this);
//        menu1.addMenuItem(btn1);
//        menu1.addMenuItem(MHButton.create(MHButton.Type.TEXT, "More Commands"));
//        
//        menu2 = new MHDropDownMenu("Other Menu", menu1.getX()+menu1.getWidth()+2, menu1.getY(), 128, 20, this);
//        menu2.addMenuItem(MHButton.create(MHButton.Type.TEXT, "Buffalo Wings"));
//        menu2.addMenuItem(MHButton.create(MHButton.Type.TEXT, "Watermelon"));
//        menu2.addMenuItem(MHButton.create(MHButton.Type.TEXT, "Hot Dog"));
//        
//        add(menu1);
//        add(menu2);
    }

    
    @Override
    public void update(long elapsedTime)
    {
        super.update(elapsedTime);

        sprite.update(elapsedTime);
        
        position = position.add(velocity.multiply(elapsedTime));
        
        int maxX = screenWidth - MHFont.getDefaultFont().stringWidth(message);
        
        if (position.getX() <= 0)
        {
            position.setX(0);
            velocity.setX(-velocity.getX());
        }
        if (position.getX() > maxX)
        {
            position.setX(maxX);
            velocity.setX(-velocity.getX());
        }
        if (position.getY() <= 0)
        {
            position.setY(0);
            velocity.setY(-velocity.getY());
        }
        if (position.getY() > screenHeight)
        {
            position.setY(screenHeight);
            velocity.setY(-velocity.getY());
        }
    }

    
    @Override
    public void render(MHGraphicsCanvas g)
    {
        g.fill(bgColor);
        
        sprite.render(g);
        
        g.setColor(groundColor);
        g.fillRect(0, 300+sprite.getHeight(), MHScreenManager.getDisplayWidth(), 300+sprite.getHeight());

        g.setColor(textColor);
        g.drawString("FPS: " + MHGame.getFramesPerSecond(), 10, 20);
        g.drawString(message, (int)position.getX(), (int)position.getY());
        super.render(g);
    }


	public void onMouseDown(MHMouseTouchEvent e) 
	{
		message = "Mouse down at (" + e.getX() + ", " + e.getY() + ")";
		
		super.onMouseDown(e);
	}


	public void onMouseUp(MHMouseTouchEvent e) 
	{
		message = "Mouse up at (" + e.getX() + ", " + e.getY() + ")";
		if (e.isRightClick())
			popup.show(this, e.getX(), e.getY(), 128, 20);
		
		super.onMouseUp(e);
	}


	public void onMouseMoved(MHMouseTouchEvent e) 
	{
		message = "Mouse moved at (" + e.getX() + ", " + e.getY() + ")";
		
		super.onMouseMoved(e);
	}



    @Override
    public void onButtonPressed(MHButton button, MHMouseTouchEvent e)
    {
        if (button == btnExit)
            MHGame.setProgramOver(true);
    }


	@Override
	public void onKeyDown(MHKeyEvent e) 
	{
		message = "Key down: " + e.getKeyCode();
	}


	@Override
	public void onKeyUp(MHKeyEvent e) 
	{
		message = "Key up: " + e.getKeyCode();
	}


	@Override
	public void render(MHGraphicsCanvas g, int x, int y) {
		// TODO Auto-generated method stub
		
	}

}