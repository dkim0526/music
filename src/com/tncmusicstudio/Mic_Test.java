package com.tncmusicstudio;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


import android.media.MediaRecorder;
import android.media.MediaPlayer;
import java.io.IOException;

public class Mic_Test extends SherlockActivity
{
	Button beat1, beat2, beat3, beat4, beat5, beat6;
	int toggle1, toggle2, toggle3, toggle4, toggle5, toggle6 =0;

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
				setContentView(R.layout.activity_mic);
				setTitle("Mic Check one two");
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				
				/**
				 * Creating all buttons instances
				 * */
				beat1 = (Button) findViewById(R.id.button1);
				beat2 = (Button) findViewById(R.id.button2);
				beat3 = (Button) findViewById(R.id.button3);
				beat4 = (Button) findViewById(R.id.button4);
				beat5 = (Button) findViewById(R.id.button5);
				beat6 = (Button) findViewById(R.id.button6);
				
				beat1.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle1==0){
							beat1.setBackgroundResource(R.drawable.neonorange);
							toggle1=1; 
						}else if(toggle1==1){
							beat1.setBackgroundResource(R.drawable.neonblue);
							toggle1=2; 
						}else if(toggle1==2){
							beat1.setBackgroundResource(R.drawable.neongreen);
							toggle1=3;
						}else if(toggle1==3){
							beat1.setBackgroundResource(R.drawable.default_btn);
							toggle1=0;
						}
						
						
					}
					
				});
				beat2.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle2==0){
							beat2.setBackgroundResource(R.drawable.neonorange);
							toggle2=1; 
						}else if(toggle2==1){
							beat2.setBackgroundResource(R.drawable.neonblue);
							toggle2=2; 
						}else if(toggle2==2){
							beat2.setBackgroundResource(R.drawable.neongreen);
							toggle2=3;
						}else if(toggle2==3){
							beat2.setBackgroundResource(R.drawable.default_btn);
							toggle2=0;
						}
						
					}
					
				});beat3.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle3==0){
							beat3.setBackgroundResource(R.drawable.neonorange);
							toggle3=1; 
						}else if(toggle3==1){
							beat3.setBackgroundResource(R.drawable.neonblue);
							toggle3=2; 
						}else if(toggle3==2){
							beat3.setBackgroundResource(R.drawable.neongreen);
							toggle3=3;
						}else if(toggle3==3){
							beat3.setBackgroundResource(R.drawable.default_btn);
							toggle3=0;
						}
						
					}
					
				});beat4.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle4==0){
							beat4.setBackgroundResource(R.drawable.neonorange);
							toggle4=1; 
						}else if(toggle4==1){
							beat4.setBackgroundResource(R.drawable.neonblue);
							toggle4=2; 
						}else if(toggle4==2){
							beat4.setBackgroundResource(R.drawable.neongreen);
							toggle4=3;
						}else if(toggle4==3){
							beat4.setBackgroundResource(R.drawable.default_btn);
							toggle4=0;
						}
						
					}
					
				});beat5.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle5==0){
							beat5.setBackgroundResource(R.drawable.neonorange);
							toggle5=1; 
						}else if(toggle5==1){
							beat5.setBackgroundResource(R.drawable.neonblue);
							toggle5=2; 
						}else if(toggle5==2){
							beat5.setBackgroundResource(R.drawable.neongreen);
							toggle5=3;
						}else if(toggle5==3){
							beat5.setBackgroundResource(R.drawable.default_btn);
							toggle5=0;
						}
						
					}
					
				});beat6.setOnClickListener(new OnClickListener(){	

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(toggle6==0){
							beat6.setBackgroundResource(R.drawable.neonorange);
							toggle6=1; 
						}else if(toggle6==1){
							beat6.setBackgroundResource(R.drawable.neonblue);
							toggle6=2; 
						}else if(toggle6==2){
							beat6.setBackgroundResource(R.drawable.neongreen);
							toggle6=3;
						}else if(toggle6==3){
							beat6.setBackgroundResource(R.drawable.default_btn);
							toggle6=0;
						}
						
					}
					
				});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_mic, menu);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(9, 59, 99)));
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