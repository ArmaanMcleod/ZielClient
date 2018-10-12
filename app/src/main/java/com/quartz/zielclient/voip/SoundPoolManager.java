package com.quartz.zielclient.voip;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.quartz.zielclient.R;

import static android.content.Context.AUDIO_SERVICE;

public class SoundPoolManager {

  private static SoundPoolManager instance;

  private SoundPool soundPool;

  private boolean playing = false;
  private boolean loaded = false;
  private boolean playingCalled = false;

  private float volume;
  private int ringingSoundId;
  private int ringingStreamId;
  private int disconnectSoundId;

  private SoundPoolManager(Context context) {
    // AudioManager audio settings for adjusting the volume
    AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
    if (audioManager != null) {
      float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
      float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      volume = actualVolume / maxVolume;
    }

    // Load the sounds
    int maxStreams = 1;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      soundPool = new SoundPool.Builder()
          .setMaxStreams(maxStreams)
          .build();
    } else {
      soundPool = new SoundPool.Builder()
          .setMaxStreams(maxStreams)
          .setAudioAttributes(new AudioAttributes.Builder()
              .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
              .build())
          .build();
    }

    soundPool.setOnLoadCompleteListener((pool, sampleId, status) -> {
      loaded = true;
      if (playingCalled) {
        playRinging();
        playingCalled = false;
      }
    });

    ringingSoundId = soundPool.load(context, R.raw.incoming, 1);
    disconnectSoundId = soundPool.load(context, R.raw.disconnect, 1);
  }

  public void playRinging() {
    if (loaded && !playing) {
      if (soundPool != null) {
        ringingStreamId = soundPool.play(ringingSoundId, volume, volume, 1, -1, 1f);
      }
      playing = true;
    } else {
      playingCalled = true;
    }
  }

  public void stopRinging() {
    if (playing) {
      if (soundPool != null) {
        soundPool.stop(ringingStreamId);
      }
      playing = false;
    }
  }

  public void playDisconnect() {
    if (loaded && !playing) {
      soundPool.play(disconnectSoundId, volume, volume, 1, 0, 1f);
      playing = false;
    }
  }

  public static void release() {
    SoundPool pool = instance.soundPool;
    if (pool != null) {
      pool.unload(instance.ringingSoundId);
      pool.unload(instance.disconnectSoundId);
      pool.release();
      instance.soundPool = null;
    }

    instance = null;
  }

  public static SoundPoolManager getInstance(Context context) {
    if (instance == null) {
      instance = new SoundPoolManager(context);
    }
    return instance;
  }
}