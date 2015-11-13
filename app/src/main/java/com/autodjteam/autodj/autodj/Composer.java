/**
 * The composer class runs through a loop that queues up sounds for playback by the performer
 * class. This class will run through an algorithm to decide which notes and sounds will be played
 * on the next beat
 *
 * NOTES FOR NEXT MEETING:
 *
 *     LOADING SOUNDS: time how long startup takes when loading each sound individually
 *     using raw commands. If it takes a while, try storing each of the file ids in an
 *     array and using that instead!
 */
package com.autodjteam.autodj.autodj;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Composer extends AppCompatActivity{

    private int currentBeat;

    long last = 0;
    long now, diff, entries, sum;

    private int currentMeasure; //checks which measure we are in, for fill's sake.
    private double composerCheck; //stores a randomly-generated number for comparison
    private Random randy;
    public int[] parameters; //array of composition parameters
    public int[] sounds;
    public boolean isPlaying;
    //public Performer playback;
	public SeekBar tempoSeekBar;
	public SeekBar complexitySeekBar;
	public SeekBar rageSeekBar;
	public SoundPool soundPool;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "User"));

		tempoSeekBar = (SeekBar) findViewById(R.id.tempoSeekBar);
		tempoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar mySeekBar, int progress, boolean fromUser) {
                TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
                tempoTextView.setText(String.valueOf("Tempo: " + tempoTransform(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

		complexitySeekBar = (SeekBar) findViewById(R.id.complexitySeekBar);
		complexitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar mySeekBar, int progress, boolean fromUser) {
				TextView complexityTextView = (TextView)findViewById(R.id.complexityTextView);
				complexityTextView.setText(String.valueOf("Complexity: " + progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		rageSeekBar = (SeekBar) findViewById(R.id.rageSeekBar);
		rageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar mySeekBar, int progress, boolean fromUser) {
				TextView rageTextView = (TextView) findViewById(R.id.rageTextView);
				rageTextView.setText(String.valueOf("Rage: " + progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

//        ToggleButton tapTempoButton = (ToggleButton)findViewById(R.id.tapTempoButton);
//        tapTempoButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//				final int BPM = 60000;
//				if (last == 0) {
//					last = System.currentTimeMillis();
//				} else {
//					now = System.currentTimeMillis();
//					diff = now - last;
//
//					int tapTempo = BPM / (int) diff;
//
//					if (tapTempo <= 240 && tapTempo >= 60) {
//						//parameters[0] = tapTempo;
//
//						tempoSeekBar.setProgress((tapTempo - 60) / 2);
//						TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
//						tempoTextView.setText(String.valueOf("Tempo: " + tapTempo));
//					}
//					last = 0;
//					now = 0;
//				}
//			}
//		});
//		XML for the above toggle button
//		<ToggleButton
//		android:layout_width="160dp"
//		android:layout_height="100dp"
//		android:layout_alignParentBottom="true"
//		android:layout_alignParentRight="true"
//		android:textOff="Tap Tempo"
//		android:textOn="Tap Tempo"
//		android:id="@+id/tapTempoButton" />







		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		//sound = soundPool.load(this, R.raw.bassdrum1, 1);
		onStartup();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
	}



	public void clickTempo(View v) {
		final int BPM = 60000;
		if (last == 0) {
			last = System.currentTimeMillis();
		} else {
			now = System.currentTimeMillis();
			diff = now - last;
            if (diff == 0)
                diff = 1;
			int tapTempo = BPM / (int) diff;

			if (tapTempo <= 240 && tapTempo >= 60) {
				//parameters[0] = tapTempo;

				tempoSeekBar.setProgress((tapTempo - 60) / 2);
				TextView tempoTextView = (TextView) findViewById(R.id.tempoTextView);
				tempoTextView.setText(String.valueOf("Tempo: " + tapTempo));
			}
			last = now;
		}
	}




    public Composer(){
        currentBeat = 1;
        currentMeasure = 1;
        parameters = new int[3];
		sounds = new int[10];
        parameters[0] = 60; //default tempo
        parameters[1] = 5; //default complexity
		parameters[2] = 1; //default rage
        isPlaying = false;
        randy = new Random();
		//playback = new Performer();
		//onStartup();
    }

    //methods

    //loads all sounds in the library on startup
    public void onStartup(){
		sounds[0] = soundPool.load(this, R.raw.bassdrum1, 1);
		sounds[1] = soundPool.load(this, R.raw.snare1,1);
		sounds[2] = soundPool.load(this, R.raw.hihat1,1);
		sounds[3] = soundPool.load(this, R.raw.crash1,1);
        sounds[4] = soundPool.load(this, R.raw.snare2,1);
	}

	public void play(int sound) {
		soundPool.play(sound, 1, 1, 1, 0, 1);
	}

    //main composition loop
    public void compose(){
    	if (isPlaying) {
    		new TempoTimer(parameters[0]);
    	}
        if (currentBeat == 1){ 
        	updateParameters(); //if we are on the downbeat of a measure, update composition parameters
        }
        
        //Primary Drum composition
        composerCheck = randy.nextDouble()*100 + 1;
        if(currentMeasure == 1 && currentBeat == 1)
        	play(sounds[3]); //cymbal crash after every fill because CLUB MUSIC.
        
        if (currentMeasure != 8) {
			if ((currentBeat-1)%4 == 0){
			    play(sounds[0]); //bass drum (four on the floor)
			}
			if ((currentBeat+1)%4 == 0 && composerCheck < parameters[1]*10)
				play(sounds[0]); //bass drum extra beats
			if ((currentBeat-1)%8 == 4){
			    play(sounds[4]); //snare drum (ABSOLUTELY must play on "2 and 4!")
			}
			
			if (currentBeat%2 == 1)
				play(sounds[2]); //hi-hat cymbal hits
			
			if (currentBeat%2 == 0 && composerCheck < parameters[1]*10) {
                play(sounds[1]); //offbeat snare hits, occuring infrequently
            }
            else if (currentBeat%2 == 0 && composerCheck < parameters[1]*20) {
                play(sounds[0]);
            }
        }
        else 
        	fillCompose();
		if (currentBeat != 16) //checks to see what downbeat we are on
	        currentBeat++;
		else {
		    if (currentMeasure != 8)
		    	currentMeasure++;
		    else
		    	currentMeasure = 1;
	        currentBeat = 1;
		}
	}
    
    //Composes drum fills every eight measures for stylistic flair
    public void fillCompose(){
    	if (composerCheck > 50){ //50% chance to play this fill (NOTE: might be useful to make this a parameter)
    		play(sounds[1]);
    		if ((currentBeat-1)%4 == 0)
    			play(sounds[1]);
    	}
    	else { //50% chance to play this fill
    		if (((currentBeat+1)/2)%2 == 1)
    			play(sounds[1]);
    		else
    			play(sounds[0]);
    	}
    }
    
    //Updates composition parameters based on current slider values
    public void updateParameters () {
    	//Method for changing parameters to current slider values goes here
		int tempo = tempoSeekBar.getProgress();
		parameters[0]=tempoTransform(tempo);
        parameters[1]=complexitySeekBar.getProgress();
        parameters[2]=rageSeekBar.getProgress();
    	//so: set parameters[0] (tempo parameter) equal to tempo slider's current value.
    }

    public int tempoTransform(int tempo){
		return 2*tempo+60;
	}
    //on a press of the play/pause button, playPause toggles the parameter
    //that specifies whether or not the composer is playing music
    //between 0 and 1
    public void playPause(){
    	isPlaying = !isPlaying;
        if (isPlaying)
        	compose();
    }

	public void playPause(View view) {
		playPause();
		//soundPool.play(sound,1,1,1,0,1);
	}




    /* helper Classes go here
     * 
     * Helper Class tempoTimer allows the composer to halt for the time in between beats
     * creating a rhythm for composition and playback
     */
    public class TempoTimer {
    	Timer timer; 
    	
    	public TempoTimer(int tempo) {
    		timer = new Timer();
    		timer.schedule(new ComposerTask(), 15000/tempo);
    	}
    	class ComposerTask extends TimerTask {
    		public void run() {
				compose();
				timer.cancel();
    		}
    	}
    }
}
