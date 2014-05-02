package com.tncmusicstudio;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tncmusicstudio.R;

public class Beats_Activity extends SherlockActivity {
	boolean check = true;
	boolean saving = true;
	Menu mymenu;
	private static SPPlayer sp;

	Button beat1, beat2, beat3, beat4, beat5, beat6;
	Button[] mybeats = {};
	public static final String PREFS = "beat_prefs";
	public static final String beat_key = "mybeats";

	SharedPreferences settings;
	SharedPreferences.Editor edit;
	public static final String DEFAULT_STRING_ARRAY = "000000000000";
	static String myset = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beats_);
		setTitle("One Shots");
		setUpSound();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		settings = getSharedPreferences(PREFS, 0);
		edit = settings.edit();
		/* then it's been set up, otherwise set it up */
		myset = settings.getString(beat_key, DEFAULT_STRING_ARRAY);
		if (myset.length() != 12) {
			myset = DEFAULT_STRING_ARRAY;
			edit.putString(beat_key, DEFAULT_STRING_ARRAY);
			edit.commit();
		}

		/**
		 * Creating all buttons instances
		 * */
		beat1 = (Button) findViewById(R.id.button1);
		beat2 = (Button) findViewById(R.id.button2);
		beat3 = (Button) findViewById(R.id.button3);
		beat4 = (Button) findViewById(R.id.button4);
		beat5 = (Button) findViewById(R.id.button5);
		beat6 = (Button) findViewById(R.id.button6);

		/* tag indicates that it's blue */
		beat1.setTag(true);
		beat2.setTag(true);
		beat3.setTag(true);
		beat4.setTag(true);
		beat5.setTag(true);
		beat6.setTag(true);

		Button[] beatcopy = { beat1, beat2, beat3, beat4, beat5, beat6 };
		mybeats = beatcopy;
		setBeatColors();

		/*
		 * For saving the beats- this is how it will work On action down, if
		 * saved is on && it's blue (the tag is true) -> then add it to the
		 * String and then commit else if saved is on && it's orange (the tag is
		 * true) -> then remove it from the String and then commit
		 */
		beat1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat1, 6))
							return false;
						playBeat(6);
					} else {
						if (!commits(beat1, 0))
							return false;
						playBeat(0);
					}
					/* if it's blue */
					if (beat1.getTag().equals(true)) {
						beat1.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat1.setTag(false);
					} else {
						beat1.setBackgroundResource(R.drawable.blue5);
						/* tag orange */
						beat1.setTag(true);
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					System.err.println("beat1 up: " + beat1.getTag()
							+ " saving? : " + saving);

					if (!saving) {
						beat1.setBackgroundResource(R.drawable.blue5);
						beat1.setTag(true);
					} else {

					}
				}
				return true;
			}
		});
		beat2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat2, 7))
							return false;
						playBeat(7);

					} else {
						if (!commits(beat2, 1))
							return false;
						playBeat(1);
					}
					/* if it's blue */

					if (beat2.getTag().equals(true)) {
						beat2.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat2.setTag(false);
					} else {
						beat2.setBackgroundResource(R.drawable.blue5);
						/* tag orange */
						beat2.setTag(true);
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!saving) {
						beat2.setBackgroundResource(R.drawable.blue5);
						beat2.setTag(true);
					} else {

					}
				}
				return true;
			}
		});

		beat3.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat3, 8))
							return false;
						playBeat(8);

					} else {
						if (!commits(beat3, 2))
							return false;
						playBeat(2);
					}
					/* if it's blue */

					if (beat3.getTag().equals(true)) {
						beat3.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat3.setTag(false);
					} else {
						beat3.setBackgroundResource(R.drawable.blue5);
						/* tag orange */
						beat3.setTag(true);
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!saving) {
						beat3.setBackgroundResource(R.drawable.blue5);
						beat3.setTag(true);
					} else {

					}
				}
				return true;
			}
		});

		beat4.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat4, 9))
							return false;
						playBeat(9);

					} else {
						if (!commits(beat4, 3))
							return false;
						playBeat(3);
					}
					/* if it's blue */
					if (beat4.getTag().equals(true)) {
						beat4.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat4.setTag(false);
					} else {
						beat4.setBackgroundResource(R.drawable.blue5);
						/* tag orange */
						beat4.setTag(true);
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!saving) {
						beat4.setBackgroundResource(R.drawable.blue5);
						beat4.setTag(true);
					} else {

					}
				}
				return true;
			}
		});

		beat5.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat5, 10))
							return false;
						playBeat(10);

					} else {
						if (!commits(beat5, 4))
							return false;
						playBeat(4);
					}
					/* if it's blue */
					if (beat5.getTag().equals(true)) {
						beat5.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat5.setTag(false);
					} else {
						beat5.setBackgroundResource(R.drawable.blue5);
						/* tag orange */
						beat5.setTag(true);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!saving) {
						beat5.setBackgroundResource(R.drawable.blue5);
						beat5.setTag(true);
					} else {

					}
				}
				return true;
			}
		});
		beat6.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mymenu.getItem(1).getTitle().equals("One Shots")) {
						if (!commits(beat6, 11))
							return false;
						playBeat(11);

					} else {
						if (!commits(beat6, 5))
							return false;
						playBeat(5);
					}
					/* if it's blue */
					if (beat6.getTag().equals(true)) {
						beat6.setBackgroundResource(R.drawable.orange);
						/* tag orange */
						beat6.setTag(false);
					} else {
						beat6.setBackgroundResource(R.drawable.blue5);
						/* tag blue */
						beat6.setTag(true);
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (!saving) {
						beat6.setBackgroundResource(R.drawable.blue5);
						beat6.setTag(true);
					} else {

					}
				}
				return true;
			}
		});

	}

	public boolean commits(Button b, int num) {
		if (saving) {
			if (countNum() >= 4 && b.getTag().equals(true)) {
				Toast.makeText(
						this,
						"You've already saved 4 beats, Unselect one to save another",
						Toast.LENGTH_SHORT).show();
				return false;
			}
			// if saving and it's blue then add it to the string
			// and commit it
			if (b.getTag().equals(true)) {
				myset = setString(myset, num, true);
				edit.putString(beat_key, myset);
				edit.commit();
			} else {
				// if saving and it's orange then take the beat
				// from the string and commit
				myset = setString(myset, num, false);
				edit.putString(beat_key, myset);
				edit.commit();
			}
		}
		return true;
	}

	public static int countNum() {
		int num = 0;
		for (int i = 0; i < myset.length(); i++) {
			if (myset.charAt(i) == '1')
				num++;
		}
		return num;
	}

	public String setString(String str, int index, boolean set) {
		char[] myArr = str.toCharArray();
		myArr[index] = (set) ? '1' : '0';
		String acc = "";
		for (int i = 0; i < myArr.length; i++) {
			acc += myArr[i];
		}
		// System.out.println("acc: " + acc + " set: " + set + " index: " +
		// index);
		return acc;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.beats_, menu);
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
		} else if (item.getItemId() == R.id.piano_icon) {
			Intent i = new Intent(this, PlayAroundActivity.class);
			startActivity(i);
			return true;
		} else if (item.getItemId() == R.id.loop_icon) {
			Toast.makeText(this, "The Loop has not been implemented yet",
					Toast.LENGTH_SHORT).show();
			return true;
		} else if (item.getItemId() == R.id.toggle) {

			System.out.println("String: " + myset);
			if (check) {
				check = false;
				item.setTitle(R.string.toggleBeats);
				setTitle("Beats");
				if(!saving) setDefaultColors();
				else setBeatColors();

			} else {
				check = true;
				if(!saving) setDefaultColors();
				else setBeatColors();
				item.setTitle(R.string.toggleShots);
				setTitle("One Shots");

			}
			return true;
		} else if (item.getItemId() == R.id.menu_sub) {
			if (saving) {
				// change to beats
				item.setTitle(R.string.mode_play);
				// item.getItemId(R.id.loop_icon).setVisible(false);
				// ((Menu) item).getItem(1).setVisible(false);
				Toast.makeText(this, "Start playing some beats!",
						Toast.LENGTH_SHORT).show();
				saving = false;
				setDefaultColors();

			} else {
				item.setTitle(R.string.mode_save);
				Toast.makeText(this, "Start saving some beats",
						Toast.LENGTH_SHORT).show();

				saving = true;
				setBeatColors();
			}
			return true;
		}

		else
			return false;
	}

	private void setBeatColors() {
		for (int i = 0; i < mybeats.length; i++) {
			int index = i;
			if (check)
				index += 6;
			System.out.println("index: " + index + "myset: " + myset);
			if (myset.charAt(index) == '0') {
				mybeats[i].setBackgroundResource(R.drawable.blue5);
				mybeats[i].setTag(true);
			}
			else {
				mybeats[i].setBackgroundResource(R.drawable.orange);
				mybeats[i].setTag(false);
			}
		}
	}

	private void setDefaultColors() {
		for (int i = 0; i < mybeats.length; i++) {
			mybeats[i].setBackgroundResource(R.drawable.blue5);
		}
	}

	private void setUpSound() {
		// Log.e("SP", "FAILED ON ONCREATE");
		AssetManager am = getAssets();
		// activity only stuff
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		this.sp = new SPPlayer(am, audioManager);
	}

	public static String[] beatArrss = { "beat1", "beat2", "beat3", "beat4",
			"beat5", "beat6", "clap", "snare", "oneshot3", "oneshot4",
			"oneshot5", "oneshot6" };

	public static void playBeat(int i) {
		sp.playNote(beatArrss[i], 1);
	}

}
