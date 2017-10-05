package com.test;
import com.mhframework.MHScreenManager;
import com.mhframework.ai.fsm.MHState;
import com.mhframework.ai.fsm.MHStateContext;
import com.mhframework.core.math.MHVector;
import com.mhframework.gameplay.actor.MHActor;
import com.mhframework.gameplay.actor.MHAnimationSequence;
import com.mhframework.gameplay.actor.MHAnimator;
import com.mhframework.gameplay.actor.MHAnimator.MHAnimationListener;
import com.mhframework.platform.MHPlatform;
import com.mhframework.platform.event.MHKeyEvent;
import com.mhframework.platform.event.MHKeyListener;
import com.mhframework.platform.graphics.MHGraphicsCanvas;


public class TestSprite extends MHActor implements MHStateContext, MHAnimationListener, MHKeyListener
{
    private MHState state;
    private MHAnimator animation;
    static MHAnimationSequence standAnimation, runAnimation, jumpAnimation;
    
    private boolean walkRight = false;
    private boolean walkLeft = false;
    boolean jump = false;
    private MHVector jumpForce = new MHVector(0, -0.8);
    
    public TestSprite()
    {
        super();
        setUpAnimations();
        animation = new MHAnimator(runAnimation);
        this.setImageID(runAnimation.getImageID(0));
        setPosition(150, 10); jump = true;
        state = StandingState.getInstance();
        setMaxSpeed(0.25);
        
        MHPlatform.addKeyListener(this);
    }

    
    private void setUpAnimations()
    {
        standAnimation = new MHAnimationSequence();
        standAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioWalk0.png");
        standAnimation.setDurationMillis(5000);
        
        runAnimation = new MHAnimationSequence();
        runAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioWalk0.png");
        runAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioWalk1.png");
        runAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioWalk2.png");
        runAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioWalk1.png");
        runAnimation.setDurationMillis(25);
        
        jumpAnimation = new MHAnimationSequence();
        jumpAnimation.addFrame(TestScreen.IMAGES_DIR+"MarioJump.png");
        jumpAnimation.setDurationMillis(5000);
    }
    
    
    @Override
    public void changeState(MHState newState)
    {
        if (state != null)
            state.exit(this);
        
        state = newState;
        state.enter(this);
    }

    
    
    public void update(long elapsedTime)
    {
        super.update(elapsedTime);
        animation.update(elapsedTime, this);
        setImageID(animation.getImageID());
        state.execute(this);
        int groundHeight = 300;
        if (getY() > groundHeight)
        {
            this.setPosition(getX(), groundHeight);
            this.getVelocity().setY(0.0);
            
            if (jump)
            {
                jump = false;
                if (walkRight || walkLeft)
                    this.setAnimation(runAnimation);
                else
                {
                    this.setAnimation(standAnimation);
                    setVelocity(0, 0);
                }
            }
        }
        if (getX() < -10)
            getPosition().setX(MHScreenManager.getDisplayWidth());
        else if (getX() > MHScreenManager.getDisplayWidth())
            getPosition().setX(-10);
    }
    
    
    public void render(MHGraphicsCanvas g)
    {
        render(g, (int)position.getX(), (int)position.getY());
    }
    
    
    @Override
    public void animationFrameChanged(String imageID)
    {
        // TODO Play a footstep sound on "MarioWalk0.png".
//        if (walkRight || walkLeft)
//        {
//            System.out.print("Animation frame: " + imageID);
//            System.out.println("\tMillis: " + animation.getFrameDuration());
//        }
    }


    @Override
    public void animationSequenceEnded(MHAnimationSequence animation)
    {
//        if (animation == runAnimation)
//            System.out.println("Animation sequence ended.");
    }


    public void setAnimation(MHAnimationSequence anim)
    {
        this.animation.setAnimationSequence(anim);
    }


    @Override
    public void onKeyDown(MHKeyEvent e)
    {
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyD() || e.getKeyCode() == MHPlatform.getKeyCodes().keyRightArrow())
        {
            if (!walkRight)
            {
                walkRight = true;
            setAnimation(runAnimation);
            this.setVelocity(getMaxSpeed(), getVelocity().getY());
            this.flipHorizontal(false);
            }
        }
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyA() || e.getKeyCode() == MHPlatform.getKeyCodes().keyLeftArrow())
        {
            if (!walkLeft)
            {
                walkLeft = true;
            setAnimation(runAnimation);
            this.setVelocity(-getMaxSpeed(), getVelocity().getY());
            this.flipHorizontal(true);
            }
        }
        if ((e.getKeyCode() == MHPlatform.getKeyCodes().keyW() || e.getKeyCode() == MHPlatform.getKeyCodes().keyUpArrow()) && !jump)
        {
            jump = true;
            this.setVelocity(this.getVelocity().add(jumpForce));
            this.setAnimation(jumpAnimation);
        }
    }


    @Override
    public void onKeyUp(MHKeyEvent e)
    {
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyD() || e.getKeyCode() == MHPlatform.getKeyCodes().keyRightArrow())
        {
            walkRight = false;
            if (!isFlippedHorizontal() && !jump)
                changeState(StandingState.getInstance());
        }
        if (e.getKeyCode() == MHPlatform.getKeyCodes().keyA() || e.getKeyCode() == MHPlatform.getKeyCodes().keyLeftArrow())
        {
            walkLeft = false;
            if (isFlippedHorizontal() && !jump)
                changeState(StandingState.getInstance());
        }
    }
}


class StandingState implements MHState
{
    private static MHState instance;
    
    private StandingState(){}
    
    public static MHState getInstance()
    {
        if (instance == null)
            instance = new StandingState();
        
        return instance;
    }
    
    
    @Override
    public void enter(MHStateContext context)
    { 
        TestSprite sprite = (TestSprite) context;
        sprite.setVelocity(0, 0);
        sprite.setAnimation(TestSprite.standAnimation);
    }

    
    @Override
    public void execute(MHStateContext context)
    {
    }

    
    @Override
    public void exit(MHStateContext context)
    {
    }
}


