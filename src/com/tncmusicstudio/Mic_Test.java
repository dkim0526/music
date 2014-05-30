package com.tncmusicstudio;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Mic_Test extends SherlockActivity {
	boolean mStartRecording = true;
	boolean mStartPlaying = true;

	private MediaPlayer mPlayer1 = null, mPlayer2 = null, mPlayer3, mPlayer4,
			mPlayer5, mPlayer6;
	private MediaRecorder mRecorder1 = null, mRecorder2 = null, mRecorder3,
			mRecorder4, mRecorder5, mRecorder6;
	private MediaPlayer[] mpList = { mPlayer1, mPlayer2, mPlayer3, mPlayer4,
			mPlayer5, mPlayer6 };
	private MediaRecorder[] mrList = { mRecorder1, mRecorder2, mRecorder3,
			mRecorder4, mRecorder5, mRecorder6 };

	private static final String LOG_TAG = "AudioRecordTests";
	private static final String FILE_NAME_UNMOD = "/audiorecordtest";
	private static final String FILE_NAME_EXT = ".mp4";

	private FilenameFilter fnf;
	private Button beat1, beat2, beat3, beat4, beat5, beat6;
	private int[] states;

	private static String mFileName = null;
	Menu mymenu;

	public Mic_Test() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";

		fnf = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.contains(".3gp"))
					return true;
				return false;
			}
		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setTitle("Mic Check one two");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_mic);

		/**
		 * Creating all buttons instances
		 * */
		beat1 = (Button) findViewById(R.id.rec1);
		beat2 = (Button) findViewById(R.id.rec2);
		beat3 = (Button) findViewById(R.id.rec3);
		beat4 = (Button) findViewById(R.id.rec4);
		beat5 = (Button) findViewById(R.id.rec5);
		beat6 = (Button) findViewById(R.id.rec6);

		states = new int[6];
		/* tag indicates that it's blue */
		// resetTags();
		setRecListeners(beat1, 0);
		setRecListeners(beat2, 1);
		setRecListeners(beat3, 2);
		setRecListeners(beat4, 3);
		setRecListeners(beat5, 4);
		setRecListeners(beat6, 5);

	}

	void setRecListeners(Button b, final int num) {
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// I need either a toggle or a tag setter
				mFileName = getFileName(num);
				System.out
						.println("mFile: " + mFileName + " state: "
								+ states[num] + " mrList[0] : "
								+ (mrList[num] == null));
				switch (states[num]) {
				case 0:
					startRecording(num);
					break;
				case 1:
					System.out.println("is it null ? : "
							+ (mrList[num] == null));
					if (mrList[num] != null) {
						stopRecording(num);
						startPlaying(num);
					}
					break;
				case 2:
					pausePlaying(num);
					break;
				default:
					resumePlaying(num);
					states[num] -= 2;
					break;
				}
				System.out.println("finish? : " + (mrList[0] == null));

				states[num]++;

			}

		});
		b.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				System.out.println("long click");
				// TODO Auto-generated method stub
				return true;
			}
		});
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
		} else if (item.getItemId() == R.id.play_icon) {
			onPlay(mStartPlaying);
			mStartPlaying = !mStartPlaying;
			return true;
		} else if (item.getItemId() == R.id.save_icon) {
			// gonna try looping
			// mPlayer.start();

			File[] myfiles = Environment.getExternalStorageDirectory()
					.listFiles(fnf);
			if (myfiles != null) {
				for (File i : myfiles) {
					System.out.println(" i : " + i.getName());
				}
			}
			System.out.println(" current file: " + mFileName);
			return true;
		} else
			return false;
	}

	private void onRecord(boolean start) {
		if (start) {
			// startRecording();
		} else {
			// stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			// startPlaying();
		} else {
			// stopPlaying();
		}
	}

	private void stopAll() {
		for (int i = 0; i < mpList.length; i++) {
			if (mpList[i] != null) {
				stopPlaying(i);
			}
		}
	}

	private void startPlaying(int num) {
		MediaPlayer player = new MediaPlayer();
		System.out.println("starting to play: " + num + " with file: "
				+ mFileName);

		mpList[num] = player;
		try {
			player.setDataSource(mFileName);
			player.prepare();
			player.start();
			player.setLooping(true);
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void pausePlaying(int num) {
		MediaPlayer player = mpList[num];
		player.pause();
	}

	private void resumePlaying(int num) {
		MediaPlayer player = mpList[num];
		// player.seekTo(0);
		player.start();
	}

	private void stopPlaying(int num) {
		MediaPlayer player = mpList[num];
		player.release();
		player = null;
	}

	private void startRecording(int num) {
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setAudioChannels(2);
		recorder.setAudioEncodingBitRate(128);
		recorder.setAudioSamplingRate(44100);
		recorder.setOutputFile(mFileName);
		try {
			recorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mrList[num] = recorder;
		System.out.println("starting to record: " + (recorder == null)
				+ " mrlist: " + (mrList[num] == null));
		// TODO: make toast
		Toast.makeText(getApplicationContext(), "start recording!",
				Toast.LENGTH_SHORT).show(); 
		if(true) {
		recorder.start();
		}
	}

	private void stopRecording(int num) {
		MediaRecorder recorder = mrList[num];
		System.out.println("finishing record: " + (recorder == null)
				+ " filename: " + mFileName);

		recorder.stop();
		recorder.release();
		recorder = null;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder1 != null) {
			mRecorder1.release();
			mRecorder1 = null;
		}

		if (mPlayer1 != null) {
			mPlayer1.release();
			mPlayer1 = null;
		}
	}

	private String getFileName(int i) {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		str += FILE_NAME_UNMOD + i + FILE_NAME_EXT;
		return str;
	}
}
