package com.mhframework.platform;



/********************************************************************
 * Manages all sound data for a game application made with the
 * MHFramework package.  I recommend that maintaining the sound
 * manager for a game application should be the responsibility of
 * the application's data model.
 *
 * <p>This class borrows heavily from the SoundManager class
 * presented in the the book <i>Java 1.4 Game Programming</i> by
 * Andrew Mulholland and Glenn Murphy (Wordware Publishing).
 * 
 * @author Michael Henson
 */
public abstract class MHSoundManager
{
    /**
     * Constant for telling the MHSoundManager object to
     * automatically assign an available channel number for playing
     * a sound.
     */
    public static final int AUTO_ASSIGN_CHANNEL = -1;


    ////////////////////////
    ////  Data Members  ////
    ////////////////////////

    /** Array of channel IDs. */
    protected final int channelSoundIdRef[];

    private boolean soundOn = true;

    ///////////////////
    ////  Methods  ////
    ///////////////////


    /****************************************************************
     * Constructor.
     *
     * Instantiates internal data structures and initializes the
     * channel IDs.
     */
	public MHSoundManager()
	{
		// Initialize the vector to hold the sound data...
		channelSoundIdRef = new int[32];

		for(int i=0; i<32; i++)
		{
			channelSoundIdRef[i] = -1;
		}
	}


    /****************************************************************
     * Adds a sound file to the sound manager.
     *
     * @param filepath  The path and file name of the sound file to
     *                   be added.
     *
     * @return If successful, the position in the sound data vector
     * 			where the new sound byte data was stored;
     *          otherwise -1.
     */
	public abstract int addSound(final String filepath);


    public synchronized void play(final int soundId)
    {
        play(soundId, false, MHSoundManager.AUTO_ASSIGN_CHANNEL);
    }
    
    
    public synchronized void play(final int[] sounds)
    {
        new Thread()
        {
            public void run()
            {
                for (int soundID = 0; soundID < sounds.length; soundID++)
                {
                    play(sounds[soundID]);
                    while (isSoundPlaying(sounds[soundID]));
                }
            }
        }.start();
    }
    
    
    
    
    /****************************************************************
     * Plays a sound.
     *
     * Loads sound byte data into the channel specified by the input
     * channel ID.  If the <tt>channelId</tt> parameter is the
     * constant <tt>MHSoundManager.AUTO_ASSIGN_CHANNEL</tt>, then an
     * available channel is automatically assigned.  The sound data
     * is then looped if <tt>loop</tt> is true, or just played once
     * if <tt>loop</tt> is false.
     *
     * <p><b>Note:</b>  This method was made "synchronized" due to
     * problems with calling it from event handlers.  The same
     * channel was being allocated to multiple sound instances,
     * which caused the program to hang.
     *
     * @param soundId   ID of which sound to play.
     * @param loop      True if sound should be looped.
     * @param channelId ID of the channel on which to play the
     *                   sound.
     */
	public abstract void play(final int soundId,
	                                 final boolean loop,
	                                 final int channelId);


    /****************************************************************
     * Stops the sound specified by <tt>soundId</tt> if it is
     * currently playing.
     *
     * @param soundId The ID of the sound to be stopped.
     */
	public abstract void stop(final int soundId);


    /****************************************************************
     * Stops the sound playing in the channel specified by channelId.
     *
     * @param channelId The channel containing the sound to be
     *                   stopped.
     */
	public abstract void stopChannel(final int channelId);


    /****************************************************************
     * States whether the requested channel is currently playing a
     * sound.
     *
     * <p>Checks two conditions to determine this:
     * <ol>
     *   <li>Does the channel contain sound byte data?
     *   <li>Is the channel open?
     * </ol>
     *
     * <p>If both of the above conditions are true, the channel is
     * playing a sound.
     *
     * @param channelId The ID of the channel to be checked for
     *                   sound data.
     *
     * @return True if channel is playing a sound; false otherwise.
     */
	public abstract boolean isChannelPlaying(final int channelId);


    /****************************************************************
     * States whether the sound specified by soundId is currently
     * playing.
     *
     * @param soundId The ID of the sound in question.
     *
     * @return True if sound is playing; false otherwise.
     */
	public boolean isSoundPlaying(final int soundId)
	{
		// check to see if any occurence of the sound is playing...
		for(int i=0; i<32; i++)
		{
			if(channelSoundIdRef[i] == soundId)
			{
				return true;
			}
		}
		return false;
	}


    /****************************************************************
     * Stops all active channels.
     */
	public void stopAllChannels()
	{
		// stop active channels...
		for(int i=0; i<32; i++)
			stopChannel(i);
	}


    public void remove(int soundID)
    {
        // TODO Auto-generated method stub
        
    }


    public void setSoundOn(boolean soundOn)
    {
        this.soundOn = soundOn;
    }


    public boolean isSoundOn()
    {
        return soundOn;
    }

}
