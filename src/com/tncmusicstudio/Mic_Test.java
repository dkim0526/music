package com.tncmusicstudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
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
import com.model.ButtonMap;

/*
 * How to solidify player?
 * 1) delete looping true, and make my own looping so that when I start, I set a timer to seek back to 0 at a certain duration
 */

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
	private HashMap<Integer, ButtonMap> loop2Key;

	public Mic_Test() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";

		fnf = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.contains(FILE_NAME_EXT))
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
		loop2Key = new HashMap<Integer, ButtonMap>();
		/* tag indicates that it's blue */
		// resetTags();
		if (beat1 == null) {
			System.out.println("beat 1 is null");
		}
		setRecListeners(beat1, 0);
		setRecListeners(beat2, 1);
		setRecListeners(beat3, 2);
		setRecListeners(beat4, 3);
		setRecListeners(beat5, 4);
		setRecListeners(beat6, 5);

		setUpKeyListeners();

	}

	private void setUpKeyListeners() {
		Log.e("keylisten", "IM SETTING THIS UP NAOOOO!");
		// first 3 beats
		loop2Key.put(KeyEvent.KEYCODE_U, new ButtonMap(beat1, false));
		loop2Key.put(KeyEvent.KEYCODE_I, new ButtonMap(beat2, false));
		loop2Key.put(KeyEvent.KEYCODE_O, new ButtonMap(beat3, false));

		// bottom 3 beats
		loop2Key.put(KeyEvent.KEYCODE_J, new ButtonMap(beat4, false));
		loop2Key.put(KeyEvent.KEYCODE_K, new ButtonMap(beat5, false));
		loop2Key.put(KeyEvent.KEYCODE_L, new ButtonMap(beat6, false));

		// long clicks
		// first 3 beats
		loop2Key.put(KeyEvent.KEYCODE_Q, new ButtonMap(beat1, true));
		loop2Key.put(KeyEvent.KEYCODE_W, new ButtonMap(beat2, true));
		loop2Key.put(KeyEvent.KEYCODE_E, new ButtonMap(beat3, true));

		// bottom 3 beats
		loop2Key.put(KeyEvent.KEYCODE_A, new ButtonMap(beat4, true));
		loop2Key.put(KeyEvent.KEYCODE_S, new ButtonMap(beat5, true));
		loop2Key.put(KeyEvent.KEYCODE_D, new ButtonMap(beat6, true));
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("key", "down: " + keyCode);
		ButtonMap result = null;
		if ((result = loop2Key.get(keyCode)) != null) {
			Log.i("key", "valid result: " + result);
			if (result.getClick()) {
				result.getB().performLongClick();
			} else
				result.getB().performClick();
			return true;
		}
		// play the sound based on the hashmap from keyCode to note

		return false;
	}

	void setRecListeners(final Button b, final int num) {
		if (b == null) {
			System.out.println("asdfasdfasdfasdf");
			return;
		}
		b.setBackgroundResource(R.drawable.default_btn);

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
					startRecording(b, num);
					break;
				case 1:
					b.setBackgroundResource(R.drawable.neonblue);

					System.out.println("is it null ? : "
							+ (mrList[num] == null));
					if (mrList[num] != null) {
						stopRecording(num);
						startPlaying(num);
					}
					break;
				case 2:
					b.setBackgroundResource(R.drawable.neongreen);

					pausePlaying(num);
					break;
				default:
					b.setBackgroundResource(R.drawable.neonblue);

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
				mFileName = getFileName(num);

				System.out.println("long click- loading previous : " + num);
				// set the state as 2, which is the paused neon green state
				if (searchFiles(num)) {
					states[num] = 2;
					b.setBackgroundResource(R.drawable.neonblue);
					startPlaying(num);
				} else {
					Toast.makeText(getApplicationContext(),
							"sorry, no file name found preloaded",
							Toast.LENGTH_SHORT).show();
				}
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

	private boolean searchFiles(int num) {
		File[] myfiles = Environment.getExternalStorageDirectory().listFiles(
				fnf);
		if (myfiles != null) {

			for (File i : myfiles) {
				System.out.println(" get FileName: " + getFileName(num)
						+ " i: " + i.getName());
				if (getFileName(num).contains(i.getName())) {
					return true;
				}
			}
		}
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
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		System.out.println("starting to play: " + num + " with file: "
				+ mFileName);

		mpList[num] = player;

		PresetReverb mReverb = new PresetReverb(0, 0);// <<<<<<<<<<<<<
		mReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
		mReverb.setEnabled(true);

		try {
			// File file = new File(mFileName);
			// FileInputStream inputStream = new FileInputStream(file);
			// cutting off 200 b/c there's latency
			player.setDataSource(mFileName);
			player.attachAuxEffect(mReverb.getId());
			player.setAuxEffectSendLevel(3.0f);
			// player.setDataSource(inputStream.getFD(), 0,
			// player.getDuration()- 200);
			// inputStream.close();

			player.prepare();
			player.setLooping(true);

			player.start();
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
		player.seekTo(0);
		player.start();
	}

	private void stopPlaying(int num) {
		MediaPlayer player = mpList[num];
		player.stop();
		player.release();
		player = null;
	}

	private void startRecording(Button b, int num) {
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// recorder.setAudioChannels(2);
		recorder.setAudioEncodingBitRate(128);
		// recorder.setAudioSamplingRate(44100);
		recorder.setOutputFile(mFileName);
		try {
			recorder.prepare();
			Thread.sleep(0);
		} catch (IOException e) {
			System.out.println("failed in ioexcept");
			Log.e(LOG_TAG, "prepare() failed");
		} catch (InterruptedException ex) {
			Log.e(LOG_TAG, "interrupted sleep");
		}

		mrList[num] = recorder;
		System.out.println("starting to record: " + (recorder == null)
				+ " mrlist: " + (mrList[num] == null));

		recorder.start();

		try {
			Thread.sleep(400);

			b.setBackgroundResource(R.drawable.neonorange);

			Toast.makeText(getApplicationContext(), "start recording!",
					Toast.LENGTH_SHORT).show();
		} catch (InterruptedException ex) {

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

	@Override
	public void onDestroy() {
		super.onDestroy();

		stopAll();
	}

	@Override
	public void onStop() {
		super.onStop();

		stopAll();
	}

	private String getFileName(int i) {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		str += FILE_NAME_UNMOD + i + FILE_NAME_EXT;
		return str;
	}
}
