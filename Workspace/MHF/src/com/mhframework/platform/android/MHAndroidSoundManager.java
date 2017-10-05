package com.mhframework.platform.android;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.mhframework.platform.MHSoundManager;

public class MHAndroidSoundManager extends MHSoundManager 
{
	private SoundPool soundPool;
	
	public MHAndroidSoundManager()
	{
		super();
		
        MHAndroidApplication.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

	
	@Override
	public int addSound(String filepath) 
	{
		int soundID = -1;

		try 
		{
			AssetManager assetManager = MHAndroidApplication.getActivity().getAssets();
			AssetFileDescriptor descriptor = assetManager.openFd(filepath);
			soundID = soundPool.load(descriptor, 1);
		} 
		catch (IOException e) 
		{
		}
		
		return soundID;
    }

	
	@Override
	public void play(int soundId, boolean loop, int channelId) 
	{
		int loopMode = (loop ? -1 : 0);
		soundPool.play(soundId, 1, 1, 0, loopMode, 1);
	}

	@Override
	public void stop(int soundId) 
	{
		soundPool.stop(soundId);
	}

	@Override
	public void stopChannel(int channelId) 
	{
	}

	@Override
	public boolean isChannelPlaying(int channelId) 
	{
		return false;
	}

}
