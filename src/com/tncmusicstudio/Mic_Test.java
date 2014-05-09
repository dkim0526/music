package com.tncmusicstudio;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


import android.media.MediaRecorder;
import android.media.MediaPlayer;
import java.io.IOException;

public class Mic_Test extends SherlockActivity
{
	
    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    private MediaPlayer   mPlayer = null;
    private static String mFileName = null;

	Menu mymenu; 
    private MediaRecorder mRecorder = null;
    private static final String LOG_TAG = "AudioRecordTest";

    public Mic_Test(){
    	  mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
          mFileName += "/audiorecordtest.3gp";
    	
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
				requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

				setTitle("Mic Check one two");
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_mic, menu);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(223, 160, 23)));
		mymenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (16908332 == item.getItemId())

		{
			Intent i = new Intent(this, AndroidDashboardDesignActivity.class);
			startActivity(i);
			return true;
		} else if (item.getItemId() == R.id.mic_icon) {
			onRecord(mStartRecording);     
            mStartRecording = !mStartRecording;
			return true;
		}
		else if (item.getItemId() == R.id.play_icon) {
			onPlay(mStartPlaying);
            mStartPlaying = !mStartPlaying;
			return true;
		}else if (item.getItemId() == R.id.save_icon) {

			return true;
		}
		else
			return false;
	}
	private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

   
    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
