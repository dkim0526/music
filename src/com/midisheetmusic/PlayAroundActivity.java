package com.midisheetmusic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.model.NotePlay;
import com.model.RecManager;
import com.model.RecNotes;

public class PlayAroundActivity extends SherlockActivity {
	String[] values = new String[] { "Level 0: Keyboard Note Training! ",
			"Level 1: Major Scales", "Level 2: Learning Chords",
			"Level 3: Actual Songs" };
	public static final String MidiDataID = "MidiDataID";
	public static final String MidiTitleID = "MidiTitleID";
	public static final int settingsRequestCode = 1;

	private Piano piano; /* The piano at the top */
	private LinearLayout layout; /* THe layout */
	private long midiCRC; /* CRC of the midi bytes */
	private LevelActivity tutorialActivityLevel;

	private LevelActivity mTuts;/* to get the string */
	private String pos;
	private boolean stop = false;
	/* for note playing */
	private SPPlayer sp;
	private boolean loaded = false;
	/* To be able to collapse items with beats */
	Menu mymenu;

	/* button to guide image falling process */
	Button playNote, backTut, nextTut, restartTut;
	Spinner beats;
	Spinner save_list;
	/**
	 * "note" + "key" + "1|2 position" +
	 * "1st primary or 2nd secondary imageview"
	 */
	ImageView noteC1A, noteC1B, noteD1A, noteD1B, noteE1A, noteE1B, noteF1A,
			noteF1B, noteG1A, noteG1B, noteA1A, noteA1B, noteB1A, noteB1B,
			noteC2A, noteC2B;
	ImageView[] ivArray;
	/* layouts initialized */

	/* init thread */
	// PianoThread pthread;
	/* animation stuff */
	Animation animC1;
	Animation animC2;
	Animation animD1;
	Animation animD2;
	Animation animE1;
	Animation animE2;
	Animation animF1;
	Animation animF2;
	Animation anim1;
	Animation anim2, anim3, anim4, anim5, anim6, anim7;
	int i = 0;// random
	int c1Count = 0;
	int d1Count = 0;

	// private noteFallTask nFallAnim;
	private LinearLayout popupLayout; /* layout for pop-up window */
	int popUpCount;
	boolean click = true;
	boolean touchable = true;
	/* for popup windows */
	LayoutParams params;
	PopupWindow popUp;
	TextView textview;

	int index = 0;
	int tArr[] = {};

	int numClicks = 0; // count numclicks to the buttons

	static int callBack = 0;

	RelativeLayout rl;

	private pianoAsync setPiano;
	ProgressDialog dialog;

	private NotePlay[] cmajor1, cmajor2, littlelamb1, littlelamb2, littlelamb3,
			twinkle1, twinkle2, twinkle3, twinkle4, biebs1, biebs2, biebs3,
			biebs4, biebs5;

	/* recording file */
	private final static String filename = "Rec.txt";
	// ArrayList<ArrayList<RecNotes>> rn = null;
	// HashMap implementation
	HashMap<String, ArrayList<RecNotes>> rn = null;

	final Context mctx = this;
	RecManager rm;
	private int beatstate = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		popUpCount = 0;
		ivArray = new ImageView[2];
		rm = new RecManager(this);
		/********/
		// Bundle bundle = getIntent().getExtras();
		// tut = bundle.getString(LevelActivity.mSelected);
		// Log.i("tutlevel", "tutlvl" + tut);
		// tut = tut.substring(6, tut.indexOf(":"));
		// Log.i("tutlevel", "new tutlvl" + tut);
		// tutLevel = Integer.parseInt(tut);
		// Log.i("tutlevel", "parsed tutlvl" + tut);
		/********/

		// ClefSymbol.LoadImages(this);
		// TimeSigSymbol.LoadImages(this);
		// MidiPlayer.LoadImages(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setTitle("Music Studio");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		try {
			setPiano = new pianoAsync();
			setPiano.execute();

			// else
			// createView();
		} catch (Exception ex) {
			Log.e("animation", "fail");
		}
	}

	private void setUpAnimation() {
		anim1 = AnimationUtils.loadAnimation(this, R.animator.falling);
		anim1.setFillAfter(false);
		anim2 = AnimationUtils.loadAnimation(this, R.animator.falling);
		anim2.setFillAfter(false);
		anim3 = AnimationUtils.loadAnimation(this, R.animator.falling);
		anim3.setFillAfter(false);
		anim4 = AnimationUtils.loadAnimation(this, R.animator.falling);
		anim4.setFillAfter(false);
		anim5 = AnimationUtils.loadAnimation(this, R.animator.falling);
		anim5.setFillAfter(false);

	}

	private void setUpSound() {
		// Log.e("SP", "FAILED ON ONCREATE");
		AssetManager am = getAssets();
		// activity only stuff
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		sp = new SPPlayer(am, audioManager);
	}

	class NBNodes {
		String note;
		int duration;

		NBNodes() {

		}
	}

	public void animate() {
		/**
		 * Data structure encoding into SQL Lite DB param 1- tempo param 2-
		 * ArrayList of NBNodes NBNodes contain 1 - full note, 2 - half 4-
		 * quarter 8- 8th ... etc. and the note for each
		 * 
		 * Ex: Mary had a little lamb
		 * [B/4][A/4][G/4][A/4][B/4][B/4][B/2][A/4][A/
		 * 4][A/2][B/4][D/4][D/2][B/4][A/4][G/4][A/4][B/4][B/4]
		 * [B/4][B/4][A/4][A/4][B/4][A/4][G/1] Tempo scales the startoffset
		 */

		ImageView[] noteArr = { noteB1A, noteA1A, noteG1A, noteA1A, noteB1B };
		anim1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// anim1.reset();
				// anim1.setStartOffset(1000);
				// anim1.startNow();
				// anim2.setStartOffset(1000);
				// noteBl.startAnimation(anim2);
				Log.e("animlist", "end1" + anim2.getStartOffset());
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.e("animlist", "repeat1");
			}

			@Override
			public void onAnimationStart(Animation animation) {
				Log.e("animlist", "start1");
				anim2.setStartOffset(200);
				noteA1A.startAnimation(anim2);
			}
		});
		anim2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// anim1.reset();
				// anim1.setStartOffset(1000);
				// anim1.startNow();
				// anim2.setStartOffset(1000);
				// noteBl.startAnimation(anim2);
				Log.e("animlist", "end2" + anim2.getStartOffset());
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.e("animlist", "repeat1");
			}

			@Override
			public void onAnimationStart(Animation animation) {
				Log.e("animlist", "start2");
				if (!anim1.hasEnded())
					Log.e("animlist", "ANIM1 HAS NOTENDED YET");
				anim3.setStartOffset(500);
				noteG1A.startAnimation(anim3);
			}
		});
		anim3.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				anim1.setStartOffset(200);
				noteA1A.startAnimation(anim1);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// anim1.setStartOffset(2000);
				// noteC1B.startAnimation(anim1);

			}

		});
		// AnimatorSet s = new AnimatorSet();
		// s.play(anim1).before(anim2);
		// s.play(anim2).before(anim3);
		// noteB1A.startAnimation(anim1);
		// anim1.setStartOffset(1000);
		// aSet.addAnimation(anim1);
		// anim1.setRepeatCount(Animation.INFINITE);
		// anim1.setRepeatMode(Animation.RESTART);
		// anim2.setRepeatCount(Animation.INFINITE);
		// anim2.setRepeatMode(Animation.RESTART);

		// noteBl.startAnimation(anim1);
		// noteOr2.startAnimation(anim2);
		// noteBl2.startAnimation(anim2);
		// noteOr3.startAnimation(anim3);
		// noteBl3.startAnimation(anim3);

	}

	/* Create the MidiPlayer and Piano views */
	//

	public static int getCallBack() {
		return callBack;
	}

	public static void callBack(int value) {
		callBack = value;
	}

	private void createViewOnlyPiano() {
		Display display = getWindowManager().getDefaultDisplay();
		// Point size = new Point();
		int width = display.getWidth();
		int height = display.getHeight();
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		/* for the button record */
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		/* for the button replay */
		LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		/* for the button beat1 */
		LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		/* for the button beat2 */
		LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		/* for the spinner beat set */
		LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		/* for the spinner context menu */
		LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		LinearLayout rl = new LinearLayout(this);
		rl.setOrientation(LinearLayout.HORIZONTAL);
		// layoutParams3.leftMargin = 100;
		// layoutParams3.topMargin = 100;
		// layoutParams3.setMargins(10, 10, 10, 10);
		/*
		 * For the button to start that tutorial
		 */
//		playNote = new Button(this);
//		playNote.setText("Record");
//		playNote.setLayoutParams(layoutParams2);

		/*
		 * For the button to play the next tutorial
		 */
//		nextTut = new Button(this);
//		nextTut.setText("Replay");
//		nextTut.setLayoutParams(layoutParams3);

		/*
		 * For the button to play the back tutorial
		 */
		backTut = new Button(this);
		backTut.setText("Beat1");
		backTut.setLayoutParams(layoutParams4);

		/*
		 * For the button to play the restart tutorial
		 */
		restartTut = new Button(this);
		restartTut.setText("Beat2");
		restartTut.setLayoutParams(layoutParams5);

		/* spinners- beats has beat sets, save_list is the context menu */
		beats = new Spinner(this);
//		save_list = new Spinner(this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayList<String> beatset_arr = new ArrayList<String>();
		beatset_arr.add("Beat 1");
		beatset_arr.add("Beat 2");

//		ArrayList<String> context_arr = new ArrayList<String>();
//		context_arr.add("Save Rec");
//		context_arr.add("List Rec");

		beats.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				Log.i("spinner", "in beats with pos: " + pos);
				beatstate = pos;
			}

//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//		});
//
//		save_list.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int pos, long id) {
//				// TODO Auto-generated method stub
//				Log.i("spinner", "in save_list with pos: " + pos);
//				switch (pos) {
//				case 0:
//					option_save(rm);
//					break;
//				case 1:
//					option_list(rm);
//					break;
//				default:
//					break;
//				}
//			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		ArrayAdapter<String> beatArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, beatset_arr);
//		ArrayAdapter<String> contextArrayAdapter = new ArrayAdapter<String>(
//				this, android.R.layout.simple_spinner_dropdown_item,
//				context_arr);

		beats.setAdapter(beatArrayAdapter);
		beats.setLayoutParams(layoutParams6);

//		save_list.setAdapter(contextArrayAdapter);
//		save_list.setLayoutParams(layoutParams7);

//		rl.addView(playNote);
//		rl.addView(nextTut);
//		rl.addView(backTut);
//		rl.addView(restartTut);

		rl.addView(beats);
//		rl.addView(save_list);

		layout.addView(rl, layoutParams);

		popupLayout = new LinearLayout(this);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		textview = new TextView(this);
		textview.setText("Hi this is a sample text for popup window");
		popUp = new PopupWindow(this);

		final String[] blinkNotes = { "3N", "4N", "5N", "6N", "2#" };
		final int[] playAscending = { 0, 1, 2, 3, 4, 5, 6, 7 };
		final int[] playDescending = { 7, 6, 5, 4, 3, 2, 1, 0 };

		/*
		 * final NotePlay[] playAscending = { 0, 1, 2, 3, 4, 5, 6, 7 }; final
		 * NotePlay[] playDescending = { 7, 6, 5, 4, 3, 2, 1, 0 };
		 */
		initSongs();

		/**
		 * Black {C#, D#, F#, G#, A#, C#, D#, F#, G#, A#} White {C, D, E, F, G,
		 * A, B, C, D, E, F, G, A, B} piano.tutorialNote([# of note][sharp # or
		 * N]); to stop, setTutUnShade = -1
		 * 
		 * array (play, next, previous restart)
		 */

//		OnClickListener myPlay0 = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				/* onClickHandler(0); */
//				piano.startRec();
//			}
//		};
//		OnClickListener myPlay1 = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// onClickHandler(1);
//				piano.playBack(1);
//				Log.i("PlayAround", "OLD PLAY BUTTON PRESSED");
//			}
//		};
		/* using beats 0, 1, 3, 4 */
		OnClickListener myPlay2 = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (beatstate == 0) {
					piano.playBeat(0);
				} else {
					piano.playBeat(3);
				}
				// mctx.openOptionsMenu();
				// onClickHandler(2);
				// piano.playBeat(0);
			}
		};
		OnClickListener myPlay3 = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// onClickHandler(3);
				if (beatstate == 0) {
					piano.playBeat(1);
				} else {
					piano.playBeat(4);
				}
			}
		};

//		playNote.setOnClickListener(myPlay0);
//		nextTut.setOnClickListener(myPlay1);
		backTut.setOnClickListener(myPlay2);
		restartTut.setOnClickListener(myPlay3);

		popupLayout.addView(textview, params);
		popUp.setContentView(popupLayout);

		// tableLayout.addView(tableRow);
		// layout.addView(tableLayout);
		/** DONE ***/
		int[] marginVal = new int[4];
		marginVal[0] = (int) (width / 200);
		marginVal[1] = (int) (height / 10);
		int right = 0;
		int left = 0;
		marginVal[2] = right;
		marginVal[3] = left;
		layoutParams.setMargins(0, 0, 0, 0);

		RelativeLayout rlPiano = new RelativeLayout(this);
		RelativeLayout.LayoutParams lpPiano = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		piano = new Piano(this, marginVal, sp, true);
		/*** SET BUTTON WIDTH AND HEIGHT FROM PIANO KEY WIDTH AND HEIGHTS **/
		// noteOr.setMinimumWidth(piano.getKeyWidths("white")); //change to
		// public static later
		/*** PIANO.FIREDISPLAY(NOTEKEY) ***/
		rlPiano.addView(piano, lpPiano);
		layout.addView(rlPiano);
		Bitmap bmImg = decodeSampledBitmapFromResource(getResources(),
				R.drawable.pianobckgd1, 768, 469);
		BitmapDrawable background = new BitmapDrawable(bmImg);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			layout.setBackgroundDrawable(background);
		} else {
			layout.setBackground(background);
		}
		setContentView(layout);
		// player.SetPiano(piano);
		layout.requestLayout();
	}

	/**
	 * Black keys range from 0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5 C#
	 * D# F# G# A# C# D# F# G# A#
	 */
	private void initSongs() {
		/************* C-major scale init *******************/
		int size = 10;
		cmajor1 = new NotePlay[size];
		cmajor2 = new NotePlay[size];
		final double[] cNote1 = { 0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5,
				9.5 };
		final int[] cDur1 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		final double[] cNote2 = { 9.5, 8.5, 7.5, 6.5, 5.5, 4.5, 3.5, 2.5, 1.5,
				0.5 };
		final int[] cDur2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

		for (int i = 0; i < cmajor1.length; i++)
			cmajor1[i] = new NotePlay(cDur1[i], cNote1[i]);
		for (int i = 0; i < cmajor2.length; i++)
			cmajor2[i] = new NotePlay(cDur2[i], cNote2[i]);

		/********** Mary had a little lamb **************/

		final int[] llNote1 = { 6, 5, 4, 5, 6, 6, 6, 5, 5, 5, 6, 8, 8 };
		final int[] llDur1 = { 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2 };

		final int[] llNote2 = { 6, 5, 4, 5, 6, 6, 6 };
		final int[] llDur2 = { 1, 1, 1, 1, 1, 1, 2 };

		final int[] llNote3 = { 5, 5, 6, 5, 4 };
		final int[] llDur3 = { 1, 1, 1, 1, 4 };

		int lambsize1 = llNote1.length;
		int lambsize2 = llNote2.length;
		int lambsize3 = llNote3.length;
		littlelamb1 = new NotePlay[lambsize1];
		littlelamb2 = new NotePlay[lambsize2];
		littlelamb3 = new NotePlay[lambsize3];

		for (int i = 0; i < littlelamb1.length; i++)
			littlelamb1[i] = new NotePlay(llDur1[i], llNote1[i]);
		for (int i = 0; i < littlelamb2.length; i++)
			littlelamb2[i] = new NotePlay(llDur2[i], llNote2[i]);
		for (int i = 0; i < littlelamb3.length; i++)
			littlelamb3[i] = new NotePlay(llDur3[i], llNote3[i]);

		/*********** Twinkle Twinkle Little Stars **************/

		final int[] ttNote1 = { 0, 0, 4, 4, 5, 5, 4 };
		final int[] ttDur1 = { 1, 1, 1, 1, 1, 1, 2 };

		final int[] ttNote2 = { 3, 3, 2, 2, 1, 1, 0 };
		final int[] ttDur2 = { 1, 1, 1, 1, 1, 1, 2 };

		final int[] ttNote3 = { 4, 4, 3, 3, 2, 2, 1, 4, 4, 3, 3, 2, 2, 1 };
		final int[] ttDur3 = { 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2 };

		final int[] ttNote4 = { 3, 3, 2, 2, 1, 4, 0 };
		final int[] ttDur4 = { 1, 1, 1, 1, 1, 1, 2 };

		int ttsize1 = ttNote1.length;
		int ttsize2 = ttNote2.length;
		int ttsize3 = ttNote3.length;
		int ttsize4 = ttNote4.length;
		twinkle1 = new NotePlay[ttsize1];
		twinkle2 = new NotePlay[ttsize2];
		twinkle3 = new NotePlay[ttsize3];
		twinkle4 = new NotePlay[ttsize4];

		for (int i = 0; i < ttsize1; i++)
			twinkle1[i] = new NotePlay(ttDur1[i], ttNote1[i]);
		for (int i = 0; i < ttsize2; i++)
			twinkle2[i] = new NotePlay(ttDur2[i], ttNote2[i]);
		for (int i = 0; i < ttsize3; i++)
			twinkle3[i] = new NotePlay(ttDur3[i], ttNote3[i]);
		for (int i = 0; i < ttsize4; i++)
			twinkle4[i] = new NotePlay(ttDur4[i], ttNote4[i]);

		/************** Justin Biebs *************/

		final int[] jbNote1 = { 9, 9, 9, 9, 8, 8, 7, 8, 8, 8, 7, 7, 9, 9, 9, 9,
				8, 8, 7, 8, 8, 8, 7, 7 };
		final int[] jbDur1 = { 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1,
				1, 1, 1, 2, 2, 2, 1, 2 };

		final int[] jbNote2 = { 9, 9, 9, 9, 8, 8, 7, 8, 8, 8, 7, 7, 9, 9, 9, 9,
				8, 9, 9, 9, 9, 8 };
		final int[] jbDur2 = { 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 2, 1, 1, 1, 1,
				2, 1, 1, 1, 1, 2 };

		final int[] jbNote3 = { 7, 7, 7, 11, 11, 11, 10, 9, 10, 9 };
		final int[] jbDur3 = { 1, 1, 1, 2, 2, 2, 1, 1, 1, 4 };

		final int[] jbNote4 = { 7, 7, 11, 10, 9, 8, 7 };
		final int[] jbDur4 = { 1, 1, 2, 2, 2, 2, 6 };

		final int[] jbNote5 = { 7, 7, 11, 10, 9, 8, 7, 8, 9, 10, 9 };
		final int[] jbDur5 = { 1, 1, 2, 2, 2, 2, 3, 1, 1, 1, 4 };

		int jbsize1 = jbNote1.length;
		int jbsize2 = jbNote2.length;
		int jbsize3 = jbNote3.length;
		int jbsize4 = jbNote4.length;
		int jbsize5 = jbNote5.length;
		biebs1 = new NotePlay[jbsize1];
		biebs2 = new NotePlay[jbsize2];
		biebs3 = new NotePlay[jbsize3];
		biebs4 = new NotePlay[jbsize4];
		biebs5 = new NotePlay[jbsize5];

		for (int i = 0; i < jbsize1; i++)
			biebs1[i] = new NotePlay(jbDur1[i], jbNote1[i]);
		for (int i = 0; i < jbsize2; i++)
			biebs2[i] = new NotePlay(jbDur2[i], jbNote2[i]);
		for (int i = 0; i < jbsize3; i++)
			biebs3[i] = new NotePlay(jbDur3[i], jbNote3[i]);
		for (int i = 0; i < jbsize4; i++)
			biebs4[i] = new NotePlay(jbDur4[i], jbNote4[i]);
		for (int i = 0; i < jbsize5; i++)
			biebs5[i] = new NotePlay(jbDur5[i], jbNote5[i]);
		/***************************************************/

	}

	protected void onClickHandler(int button) {
		// TODO Auto-generated method stub

		if (button == 0) {
			Log.i("asdf", "playnote");
			initSongs();
			// No increment
		} else if (button == 1) {
			Log.i("asdf", "nextTut");
			// initSongs();
			numClicks++;
		} else if (button == 2) {
			Log.i("asdf", "backTut");
			initSongs();
			numClicks--;
		} else if (button == 3) {
			Log.i("asdf", "restartTut");
			initSongs();
			// initnotes
			numClicks = 0;
		} else {
			Log.e("OOB", "button out of bounds");
			numClicks = 0;
		}

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/** Show an error dialog with the given message */
	private void showDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle("Piano Tutorial");
		builder.setCancelable(false);
		builder.setPositiveButton("Finished",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		builder.setNegativeButton("Restart",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	//
	// public void playNote(String note) {
	// sp.playNote();
	// }

	/** Create the SheetMusic view with the given options */
	// private void createSheetMusic(/* MidiOptions options */) {
	// // if (!options.showPiano) {
	// // piano.setVisibility(View.GONE);
	// // } else {
	// // piano.setVisibility(View.VISIBLE);
	// // }
	// sheet = new SheetMusic(this);
	// // sheet.initDefault(options);
	// // sheet.setPlayer(player);
	// layout.addView(sheet);
	// // piano.SetMidiFile(midifile, options, player);
	// // piano.SetShadeColors(options.shade1Color, options.shade2Color);
	// layout.requestLayout();
	// sheet.callOnDraw();
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.activity_playaround, menu);

		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.rgb(223, 160, 23)));
		mymenu = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		switch (item.getItemId()) {
		case 16908332:

		{
			Intent i = new Intent(this, AndroidDashboardDesignActivity.class);
			startActivity(i);
			return true;
		}

		case R.id.play_icon: {
			piano.playBack(1);
			return true;
		}
		case R.id.record_icon: {
			stop = !stop; //perhaps use to change the icon from record to pause and viceversa?
			//if(!stop)
			piano.startRec();
			if(stop)
				Toast.makeText(this, "Recording Jam", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "Stop Recording", Toast.LENGTH_SHORT).show();
			return true;
		}
		case R.id.beat1: {
			piano.addToRec(new RecNotes(
					(Calendar.getInstance().getTimeInMillis() - piano
							.getStartTime().getTimeInMillis()), 0));
			piano.playBeat(0);
			return true;
		}
		case R.id.beat2: {
			piano.addToRec(new RecNotes(
					(Calendar.getInstance().getTimeInMillis() - piano
							.getStartTime().getTimeInMillis()), 1));
			piano.playBeat(1);
			return true;
		}
		case R.id.beat3: {
			piano.addToRec(new RecNotes(
					(Calendar.getInstance().getTimeInMillis() - piano
							.getStartTime().getTimeInMillis()), 2));
			piano.playBeat(2);
			return true;
		}
		case R.id.beat4: {
			piano.addToRec(new RecNotes(
					(Calendar.getInstance().getTimeInMillis() - piano
							.getStartTime().getTimeInMillis()), 3));
			piano.playBeat(3);
			return true;
		}
		case R.id.submenu1: {
			mymenu.getItem(2).setVisible(false);
			mymenu.getItem(3).setVisible(false);
			mymenu.getItem(4).setVisible(false);
			mymenu.getItem(5).setVisible(false);

			return true;
		}
		case R.id.submenu2: {
			mymenu.getItem(2).setVisible(true);
			mymenu.getItem(3).setVisible(false);
			mymenu.getItem(4).setVisible(false);
			mymenu.getItem(5).setVisible(false);

			return true;
		}
		case R.id.submenu3: {
			mymenu.getItem(2).setVisible(true);
			mymenu.getItem(3).setVisible(true);
			mymenu.getItem(4).setVisible(false);
			mymenu.getItem(5).setVisible(false);
			
			Intent goListRec = new Intent(this, SoonToBe.class);
			Log.i("Intent", "go recordList activity");
			//Bundle bund;
			
			//goListRec.putExtra("DATABASE", new Gson().toJson(rm));
			startActivity(goListRec);
			
			return true;
		}
		case R.id.submenu4: {
			mymenu.getItem(2).setVisible(true);
			mymenu.getItem(3).setVisible(true);
			mymenu.getItem(4).setVisible(true);
			mymenu.getItem(5).setVisible(false);

			return true;
		}
		case R.id.submenu5: {
			mymenu.getItem(2).setVisible(true);
			mymenu.getItem(3).setVisible(true);
			mymenu.getItem(4).setVisible(true);
			mymenu.getItem(5).setVisible(true);

			return true;
		}
		case R.id.save_rec:
			return option_save(rm);
		case R.id.list_rec:
			return option_list(rm);
		}
		return false;
	}

	private boolean option_save(RecManager rm) {
		if (piano.getMyRec().size() == 0) {
			Toast.makeText(this, "no recording found", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
		Log.i("option", "saving rec option");
		Toast.makeText(this, "saving your recording", Toast.LENGTH_SHORT)
				.show();
		rm.saveRec(piano.getMyRec());
		return true;
	}

	private boolean option_list(RecManager rm) {

		// call recordListActivity

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
		builder.setTitle("Play Your Recordings");
		//
		// ListView modeList = new ListView(this);
		// String[] stringArray = (String[])rn.keySet().toArray();
		// modeList.setOnItemClickListener(new OnItemClickListener(){
		//
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, android.R.id.text1,
		// stringArray);
		// modeList.setAdapter(modeAdapter);
		//
		// builder.setView(modeList);
		// final Dialog dialog = builder.create();
		//
		// dialog.show();

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
				piano.setMyRec(rn.get(elem));
				Toast.makeText(mctx, "Hit Play!", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		sp.release();
		// try {
		// pthread.join(500);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// Log.e("pthread", "unable to join");
		// }
		// pthread.setRunning(false);

	}

	public void notePlayed(String note) {
		//
	}

	private Context retContext() {
		return this;
	}

	public class pianoAsync extends AsyncTask<Void, Void, Boolean> {
		boolean running = true;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(retContext(), "Loading...",
					"Go Town and Country!", true);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			Log.e("noteFallTask", "BackExecute");
			try {
				setUpSound();
				setUpAnimation();

				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			// finish?
			// TODO Auto-generated method
			Log.e("noteFallTask", "PostExecute");
			createViewOnlyPiano();
			dialog.dismiss();
			// Log.e("noteFallTask", "PostExecute");
		}

		@Override
		protected void onCancelled() {
			Log.e("noteFallTask", "CANCELED");
		}

		private void setRunning(boolean val) {
			running = val;
		}
	}
	// private void createView() {
	//
	// Display display = getWindowManager().getDefaultDisplay();
	// // Point size = new Point();
	// int width = display.getWidth();
	// int height = display.getHeight();
	// // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	//
	// layout = new LinearLayout(this);
	// layout.setOrientation(LinearLayout.VERTICAL);
	// LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
	// /* for the button */
	// LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	// rl = new RelativeLayout(this);
	//
	// /* note 1 */
	// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 2 */
	// RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 3 */
	// RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 4 */
	// RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 5 */
	// RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 6 */
	// RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	// /* note 6 */
	// RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.WRAP_CONTENT,
	// RelativeLayout.LayoutParams.MATCH_PARENT);
	//
	// playNote = new Button(this);
	// playNote.setText("fall!");
	// playNote.setLayoutParams(layoutParams2);
	// rl.addView(playNote);
	//
	// noteC1A = new ImageView(this);
	// noteD1A = new ImageView(this);
	// noteE1A = new ImageView(this);
	// noteF1A = new ImageView(this);
	// noteG1A = new ImageView(this);
	// noteA1A = new ImageView(this);
	// noteB1A = new ImageView(this);
	// noteC1B = new ImageView(this);
	// noteD1B = new ImageView(this);
	// noteE1B = new ImageView(this);
	// noteF1B = new ImageView(this);
	// noteG1B = new ImageView(this);
	// noteA1B = new ImageView(this);
	// noteB1B = new ImageView(this);
	// noteC1A.setImageResource(R.drawable.orange_note);
	// noteD1A.setImageResource(R.drawable.blue_note);
	// noteE1A.setImageResource(R.drawable.orange_note);
	// noteF1A.setImageResource(R.drawable.blue_note);
	// noteG1A.setImageResource(R.drawable.orange_note);
	// noteA1A.setImageResource(R.drawable.blue_note);
	// noteB1A.setImageResource(R.drawable.orange_note);
	// noteC1B.setImageResource(R.drawable.orange_note);
	// noteD1B.setImageResource(R.drawable.blue_note);
	// noteE1B.setImageResource(R.drawable.orange_note);
	// noteF1B.setImageResource(R.drawable.blue_note);
	// noteG1B.setImageResource(R.drawable.orange_note);
	// noteA1B.setImageResource(R.drawable.blue_note);
	// noteB1B.setImageResource(R.drawable.orange_note);
	//
	// // noteOr.setVisibility(View.INVISIBLE);
	// // noteOr2.setVisibility(View.INVISIBLE);
	//
	// /**
	// * Margins- 35- C3 115- D3 195 275 355 435 515
	// */
	// lp.setMargins(35, 0, 0, 300);
	// lp2.setMargins(110, 0, 0, 300);
	// lp3.setMargins(185, 0, 0, 300);
	// lp4.setMargins(225, 0, 0, 300);
	// lp5.setMargins(305, 0, 0, 300);
	// lp6.setMargins(385, 0, 0, 300);
	// lp7.setMargins(430, 0, 0, 300);
	// rl.addView(noteC1A, lp);
	// rl.addView(noteD1A, lp2);
	// rl.addView(noteE1A, lp3);
	// rl.addView(noteF1A, lp4);
	// rl.addView(noteG1A, lp5);
	// rl.addView(noteA1A, lp6);
	// rl.addView(noteB1A, lp7);
	// rl.addView(noteC1B, lp);
	// rl.addView(noteD1B, lp2);
	// rl.addView(noteE1B, lp3);
	// rl.addView(noteF1B, lp4);
	// rl.addView(noteG1B, lp5);
	// rl.addView(noteA1B, lp6);
	// rl.addView(noteB1B, lp7);
	//
	// // noteOr.setVisibility(View.INVISIBLE);
	// // noteOr2.setVisibility(View.INVISIBLE);
	//
	// layout.addView(rl, layoutParams);
	//
	// popupLayout = new LinearLayout(this);
	// params = new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT);
	//
	// textview = new TextView(this);
	// textview.setText("Hi this is a sample text for popup window");
	// popUp = new PopupWindow(this);
	//
	// /**
	// * Black {C#, D#, F#, G#, A#, C#, D#, F#, G#, A#} White {C, D, E, F, G,
	// * A, B, C, D, E, F, G, A, B} piano.tutorialNote([# of note][sharp # or
	// * N]); to stop, setTutUnShade = -1
	// *
	// */
	// playNote.setOnClickListener(new Button.OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// numClicks++;
	//
	// // if(numClicks%2 == 0 && numClicks != 0)
	// // {
	// // Log.i("numClicks", "num: " + numClicks);
	// // piano.toggleShade();
	// // }
	// // piano.tutorialNote("3#");
	// // if (click) {
	// // Log.i("Click", "BUTTON IS CLICKED");
	// // popUp.showAtLocation(layout, Gravity.LEFT, 8, 4);
	// // popUp.update(50, 50, 300, 100);
	// // click = false;
	// // } else {
	// // popUp.dismiss();
	// // click = true;
	// // }
	// }
	// });
	// popupLayout.addView(textview, params);
	// popUp.setContentView(popupLayout);
	//
	// // tableLayout.addView(tableRow);
	// // layout.addView(tableLayout);
	// /** DONE ***/
	// int[] marginVal = new int[4];
	// marginVal[0] = (int) (width / 200);
	// marginVal[1] = (int) (height / 10);
	// int right = 0;
	// int left = 0;
	// marginVal[2] = right;
	// marginVal[3] = left;
	// layoutParams.setMargins(0, 0, 0, 0);
	//
	// RelativeLayout rlPiano = new RelativeLayout(this);
	// RelativeLayout.LayoutParams lpPiano = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.MATCH_PARENT,
	// RelativeLayout.LayoutParams.WRAP_CONTENT);
	// piano = new Piano(this, marginVal, sp, false);
	// /*** SET BUTTON WIDTH AND HEIGHT FROM PIANO KEY WIDTH AND HEIGHTS **/
	// // noteOr.setMinimumWidth(piano.getKeyWidths("white")); //change to
	// // public static later
	// /*** PIANO.FIREDISPLAY(NOTEKEY) ***/
	// rlPiano.addView(piano, lpPiano);
	// layout.addView(rlPiano);
	//
	// setContentView(layout);
	// // player.SetPiano(piano);
	// layout.requestLayout();
	// /**
	// * LASTLY: MAKE A BUTTON SO THAT WHEN WE PRESS IT, IT WILL USE VIEWFLIP
	// * TO CHANGE TO THE SAME PIANO VIEW BUT SCROLLED UP ONE OCTAVE
	// */
	// }
}