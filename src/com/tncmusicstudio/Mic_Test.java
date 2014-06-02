package com.tncmusicstudio;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.model.ButtonMap;
import com.model.RecManager;
import com.model.RecNotes;

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
	
	private ArrayList<RecNotes> jam1 = null, jam2 = null, jam3 = null, jam4 = null, jam5 = null, jam6 = null;
	//private ArrayList<ArrayList<RecNotes>> list = b;

	private static final String LOG_TAG = "AudioRecordTests";
	private static final String FILE_NAME_UNMOD = "/audiorecordtest";
	private static final String FILE_NAME_EXT = ".mp4";

	private FilenameFilter fnf;
	private Button beat1, beat2, beat3, beat4, beat5, beat6;
	private int[] states;
	private Boolean[] isBeat = {false, false, false, false, false, false};

	private static String mFileName = null;
	Menu mymenu;
	private HashMap<Integer, Button> loop2Key;
	private HashMap<String, ArrayList<RecNotes>> rn;
	private RecManager rm;
	final Context mctx = this;
	private SPPlayer sp;
	public static String[] beatArrss = { "beat1", "beat2", "beat3", "beat4", "beat5", "beat6", "clap", 
										 "snare", "oneshot3", "oneshot4", "oneshot5", "oneshot6" };
	
	private HashMap<Integer, ArrayList<RecNotes>> listJams;
	private int offset = 0;
	private ArrayList<RecNotes> myRec;
	private int numb;
	//private static SPPlayer soundPool;
	private Timer time;
	private Boolean pauseJam;
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

		rm = new RecManager(this);
		listJams = new HashMap<Integer, ArrayList<RecNotes>>(); 
//		listJams.put(0, jam1);
//		listJams.put(1, jam2);
//		listJams.put(2, jam3);
//		listJams.put(3, jam4);
//		listJams.put(4, jam5);
//		listJams.put(5, jam6);
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
		//isBeat = new Boolean[6];
		loop2Key = new HashMap<Integer, Button>();
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
		setUpSound();

	}

	private void setUpKeyListeners() {
		Log.e("keylisten", "IM SETTING THIS UP NAOOOO!");
		// first 3 beats
		loop2Key.put(KeyEvent.KEYCODE_U, beat1);
		loop2Key.put(KeyEvent.KEYCODE_I, beat2);
		loop2Key.put(KeyEvent.KEYCODE_O, beat3);

		// bottom 3 beats
		loop2Key.put(KeyEvent.KEYCODE_J, beat4);
		loop2Key.put(KeyEvent.KEYCODE_K, beat5);
		loop2Key.put(KeyEvent.KEYCODE_L, beat6);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("key", "down: " + keyCode);
		Button result = null;
		if ((result = loop2Key.get(keyCode)) != null) {
			Log.i("key", "valid result: " + result);
			result.performClick();
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
					b.setBackgroundResource(R.drawable.neonorange);
					startRecording(num);
					break;
				case 1:
					b.setBackgroundResource(R.drawable.neonblue);

					System.out.println("is it null ? : " + (mrList[num] == null));
					if (isBeat[num]){
						Log.i("isBeat","case 1" + isBeat[num]);
						myRec = listJams.get(num);
						playBack(1);
					}
					else if (mrList[num] != null) {
							stopRecording(num);
							startPlaying(num);
					}
					
					break;
				case 2:
					b.setBackgroundResource(R.drawable.neongreen);
					if(isBeat[num]){
						Log.i("Pause Button", "PAUSE, PAUSE" + num + pauseJam);
						pauseJam = true;
					}
					else
						pausePlaying(num);
					break;
				default:
					b.setBackgroundResource(R.drawable.neonblue);
					if(!isBeat[num]){
						resumePlaying(num);
					}else{
						myRec = listJams.get(num);
						playBack(1);
					}
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
				
				if (states[num] == 0) {
				Log.i("long click", "loading jam in "+num);
					
					showListRecs(rm, num);
					b.setBackgroundResource(R.drawable.neongreen);
//					myRec = listJams.get(num);
//					playBack(1);
					//startRecording(num);
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
		// recorder.setAudioChannels(2);
		recorder.setAudioEncodingBitRate(128);
		// recorder.setAudioSamplingRate(44100);
		recorder.setOutputFile(mFileName);
		try {
			recorder.prepare();
			// Thread.sleep(1000);
		} catch (IOException e) {
			System.out.println("failed in ioexcept");
			Log.e(LOG_TAG, "prepare() failed");
		}

		mrList[num] = recorder;
		System.out.println("starting to record: " + (recorder == null)
				+ " mrlist: " + (mrList[num] == null));
		// TODO: make toast
		Toast.makeText(getApplicationContext(), "start recording!",
				Toast.LENGTH_SHORT).show();
		recorder.start();
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

	private String getFileName(int i) {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		str += FILE_NAME_UNMOD + i + FILE_NAME_EXT;
		return str;
	}
	private boolean showListRecs(RecManager rm, int num) {

		// call recordListActivity
		this.numb = num;
		rn = rm.loadRec();
		CharSequence[] items = new CharSequence[rn.size()];
		Set<String> rnKeys = rn.keySet();

		// for (int i = 0; i < items.length; i++) {
		// items[i] = "Recording " + i;
		// }
		int i = 0;
		for (String st : rnKeys) {
			items[i] = st;
			i++;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Load Your Recordings");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Set<String> re = rn.keySet();
				int i = 0;
				String elem = "";
				for (String st : re) {
					if (i == item) {
						elem = st;
						break;
					}
					i++;
				}
				Log.i("showListRecs","elem in list = " + elem);
				Boolean f = setMyRec(rn.get(elem));
				Log.i("showListRecs","elem " + elem +"is set?"+ f);
				Toast.makeText(mctx, "Jam Loaded!", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}
	private boolean setMyRec(ArrayList<RecNotes> song){
		//for(int i = 0; i < states.length; i++){
			int i = numb;
			if(states[i] == 0){
				Log.i("setMyRec","setting recording in " + i);
				listJams.remove(i);
				Log.i("setMyRec","adding to list");
				listJams.put(i,song);
				isBeat[i] = true;
				states[i]++;
				//break;
			}
		//}
		return true;
	}
	private void setUpSound() {
		// Log.e("SP", "FAILED ON ONCREATE");
		AssetManager am = getAssets();
		// activity only stuff
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		this.sp = new SPPlayer(am, audioManager);
	}
	public void playBack(int speed) {

		time = new Timer();
		Calendar mycal = Calendar.getInstance();
		Calendar copy = Calendar.getInstance();

		Log.i("recstart", "size: " + myRec.size());

		for (int i = 0; i < myRec.size(); i++) {
//			int i = 0;				############## Loop code, won't work probably
//		while(true){
//			if(!pauseJam){
//			if(i == myRec.size())
//				i = 0;
			mycal = copy;
			if (i == 0) {
				mycal.add(Calendar.MILLISECOND, (int) (myRec.get(i)
						.getCurrTime() + offset));
			} else {
				mycal.add(Calendar.MILLISECOND, (int) (myRec.get(i)
						.getCurrTime() + offset - myRec.get(i - 1)
						.getCurrTime()));
			}
			Log.i("recstart",
					"going to play it at: "
							+ (mycal.getTimeInMillis() - copy.getTimeInMillis())
							+ " with the offset: "
							+ (int) myRec.get(i).getCurrTime());
			final int index = i;
			time.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stubx`
					if (!myRec.get(index).isBeat()) {

						Log.i("recstart",
								"about to play note: "
										+ myRec.get(index).getNoteToPlay());
						sp.playNote(myRec.get(index).getNoteToPlay(), 1);

					} else {
						// is beat
						playBeat(myRec.get(index).getBeat());
						Log.i("recstart", "is a beat! playing beat: "
								+ myRec.get(index).getBeat());
					}
				}

			}, mycal.getTime());
//			i++;
//		}
		}
	}

	public void playBeat(int i) {
		sp.playNote(beatArrss[i], 1);
	}
}
