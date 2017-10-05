package com.test;

import com.mhframework.MHGame;
import com.mhframework.gameplay.actor.MHAnimationSequence;
import com.mhframework.gameplay.actor.MHAnimator;
import com.mhframework.resources.MHResourceManager;

public class MHAnimatorTest implements MHAnimator.MHAnimationListener
{
    static MHAnimationSequence seq;
    static MHAnimator a;
    long lastFrameChangeTime = MHGame.getGameTimerValue();

    public MHAnimatorTest()
    {
        seq  = new MHAnimationSequence();
        seq.setDurationSeconds(0.5);
        seq.addFrame(MHResourceManager.MHF_LOGO);
        seq.addFrame(MHResourceManager.MHF_LOGO);
        seq.addFrame(MHResourceManager.MHF_LOGO);

        a = new MHAnimator(seq);
    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        MHAnimatorTest app = new MHAnimatorTest();
        
        long start = MHGame.getGameTimerValue();
        long lastIter = MHGame.getGameTimerValue();
        while (MHGame.getGameTimerValue() - start < 5000)
        {
            while (MHGame.getGameTimerValue() - lastIter < 5);
            
            a.update(MHGame.getGameTimerValue() - lastIter, app);
            lastIter = MHGame.getGameTimerValue();
        }
    }


    @Override
    public void animationFrameChanged(String imageID)
    {
        System.out.println("\t\t\tFrame changing after " + (MHGame.getGameTimerValue() - lastFrameChangeTime) + " milliseconds");
        
        for (int tab = 0; tab < a.getCurrentFrameIndex(); tab++)
            System.out.print('\t');
        
        System.out.print("Frame " + a.getCurrentFrameIndex());
        System.out.println(":  " + a.getFrameDuration() + " ms");
        
        lastFrameChangeTime = MHGame.getGameTimerValue();
    }


    @Override
    public void animationSequenceEnded(MHAnimationSequence animation)
    {
        System.out.println("====  SEQUENCE ENDED  ====");
    }
}
