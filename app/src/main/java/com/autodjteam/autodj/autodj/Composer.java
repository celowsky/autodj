/**
 * The composer class runs through a loop that queues up sounds for playback
 * This class will run through an algorithm to decide which notes and sounds will be played
 * on the next beat
 *
 */
package com.autodjteam.autodj.autodj;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
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
    long now, diff, entries, susm;

    private int currentMeasure; //checks which measure we are in, for fill's sake.
    public double composerCheck; //stores a randomly-generated number for comparison
    public long hookCheck;
    public int hookCount; //counts measures before repeating main melody
    public Random randy;
    public double chordChange; //stores a randomly-generated number for chord changes
    public int[] parameters; //array of composition parameters
    public int[] drumsMaster; //master arrays containing all sounds
    public int[] bassMaster;
    public int[] leadMaster;
    public int[] drums; //active arrays
    public int[] bass;
    public int[] lead;
    public boolean isPlaying;
    public boolean hook;
	public SeekBar tempoSeekBar;
	public SeekBar complexitySeekBar;
	public SeekBar rageSeekBar;
	public SoundPool soundPool;
    private ProgressDialog progressDialog;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        drumsMaster = new int[10];
        bassMaster = new int[21];
        leadMaster = new int[7];

        new LoadViewTask().execute();

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







		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
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
        chordChange = 0;
        hookCount = 1;
        parameters = new int[6];
//        drumsMaster = new int[10];
//        bassMaster = new int[21];
//        leadMaster = new int[7];
		drums = new int[5];
        bass = new int[7];
        lead = new int[7];
        parameters[0] = 60; //default tempo
        parameters[1] = 5; //default complexity
		parameters[2] = 1; //default rage
		parameters[3] = 0; //default chord
        parameters[4] = 0; //current lead note
        parameters[5] = 0; //current melodyChecker
        isPlaying = false;
        hook = true;
        randy = new Random();
        hookCheck = randy.nextLong();
		//onStartup();
    }

    //methods

    //loads all sounds in the library on startup
    public void onStartup(){
        //soundLoad();
        leadMaster[0] = soundPool.load(Composer.this, R.raw.lead1e, 1);//lead sounds
        leadMaster[1] = soundPool.load(Composer.this, R.raw.lead1g, 1);
        leadMaster[2] = soundPool.load(Composer.this, R.raw.lead1a, 1);
        leadMaster[3] = soundPool.load(Composer.this, R.raw.lead1b, 1);
        leadMaster[4] = soundPool.load(Composer.this, R.raw.lead1d, 1);
        leadMaster[5] = soundPool.load(Composer.this, R.raw.lead2e, 1);
        leadMaster[6] = soundPool.load(Composer.this, R.raw.lead2g, 1);
		for (int i = 0; i < 7; i++){
            if (i < 5)
                drums[i] = drumsMaster[i];
            bass[i] = bassMaster[i];
            lead[i] = leadMaster[i];
        }
	}

//    public void soundLoad(){
//        bassMaster[0] = soundPool.load(this, R.raw.cbass1e,1);
//        bassMaster[1] = soundPool.load(this, R.raw.cbass1g,1);
//        bassMaster[2] = soundPool.load(this, R.raw.cbass1a,1);
//        bassMaster[3] = soundPool.load(this, R.raw.cbass1b,1);
//        bassMaster[4] = soundPool.load(this, R.raw.cbass1c,1);
//        bassMaster[5] = soundPool.load(this, R.raw.cbass1d,1);
//        bassMaster[6] = soundPool.load(this, R.raw.cbass2e,1);
//        bassMaster[7] = soundPool.load(this, R.raw.bass1e,1);
//        bassMaster[8] = soundPool.load(this, R.raw.bass1g,1);
//        bassMaster[9] = soundPool.load(this, R.raw.bass1a,1);
//        bassMaster[10] = soundPool.load(this, R.raw.bass1b,1);
//        bassMaster[11] = soundPool.load(this, R.raw.bass1c,1);
//        bassMaster[12] = soundPool.load(this, R.raw.bass1d,1);
//        bassMaster[13] = soundPool.load(this, R.raw.bass2e,1);
//        bassMaster[14] = soundPool.load(this, R.raw.rbass1e,1);
//        bassMaster[15] = soundPool.load(this, R.raw.rbass1g,1);
//        bassMaster[16] = soundPool.load(this, R.raw.rbass1a,1);
//        bassMaster[17] = soundPool.load(this, R.raw.rbass1b,1);
//        bassMaster[18] = soundPool.load(this, R.raw.rbass1c,1);
//        bassMaster[19] = soundPool.load(this, R.raw.rbass1d,1);
//        bassMaster[20] = soundPool.load(this, R.raw.rbass2e,1);
//
//        drumsMaster[0] = soundPool.load(this, R.raw.bassdrum1, 1);//drum sounds
//        drumsMaster[1] = soundPool.load(this, R.raw.snare1,1);
//        drumsMaster[2] = soundPool.load(this, R.raw.hihat1,1);
//        drumsMaster[3] = soundPool.load(this, R.raw.crash1,1);
//        drumsMaster[4] = soundPool.load(this, R.raw.snare2,1);
//        drumsMaster[5] = soundPool.load(this, R.raw.bassdrum2, 1);//drum sounds
//        drumsMaster[6] = soundPool.load(this, R.raw.snare3,1);
//        drumsMaster[7] = soundPool.load(this, R.raw.hihat2,1);
//        drumsMaster[8] = soundPool.load(this, R.raw.crash2,1);
//        drumsMaster[9] = soundPool.load(this, R.raw.snare4,1);
//
//        leadMaster[0] = soundPool.load(this, R.raw.lead1e,1);//lead sounds
//        leadMaster[1] = soundPool.load(this, R.raw.lead1g,1);
//        leadMaster[2] = soundPool.load(this, R.raw.lead1a,1);
//        leadMaster[3] = soundPool.load(this, R.raw.lead1b,1);
//        leadMaster[4] = soundPool.load(this, R.raw.lead1d,1);
//        leadMaster[5] = soundPool.load(this, R.raw.lead2e,1);
//        leadMaster[6] = soundPool.load(this, R.raw.lead2g,1);
//    }

	public void play(int sound) {
		soundPool.play(sound, 1, 1, 1, 0, 1);
	}

    //main composition loop
    public void compose(){
    	if (isPlaying) {
    		new TempoTimer(parameters[0]);
    	}
        if (currentBeat == 1){  //if we are on the downbeat of a measure, update composition parameters
        	updateParameters(tempoSeekBar.getProgress(), complexitySeekBar.getProgress(), rageSeekBar.getProgress());
        }
        if (currentBeat == 1 && hook) {
            randy = new Random(hookCheck);
        }
        else if (currentBeat == 1 && !hook)
            randy = new Random();

        
        //Primary Drum composition
        if (currentMeasure%2 == 0) {
            composerCheck = randy.nextDouble() * 100 + 1;
        }
        if(currentMeasure == 1 && currentBeat == 1)
        	play(drums[3]); //cymbal crash after every fill because CLUB MUSIC.
        
        if (currentMeasure != 8) {
			if ((currentBeat-1)%4 == 0){
			    play(drums[0]); //bass drum (four on the floor)
			}
			if ((currentBeat+1)%4 == 0 && composerCheck < parameters[1]*6 && parameters[1] != 10)
				play(drums[0]); //bass drum extra beats
			if ((currentBeat-1)%8 == 4 && !hook){
			        play(drums[4]); //snare drum (ABSOLUTELY must play on "2 and 4!")
			}
            if ((currentBeat-1)%16 == 8 && hook){
                play(drums[4]); //snare drum half time for the hook
            }
			composerCheck = randy.nextDouble() * 100 + 1;
            if (!hook) {
                if (currentBeat % 2 == 1)
                    play(drums[2]); //hi-hat cymbal hits
            }
            else {
                if ((currentBeat-1)%8 == 4)
                    play(drums[3]);
            }
			
			if (currentBeat%2 == 0 && composerCheck < parameters[1]*10 && parameters[1] != 10) {
                play(drums[1]); //offbeat snare hits, occurring infrequently
            }
            else if (currentBeat%2 == 0 && composerCheck < parameters[1]*4  && parameters[1] != 10) {
                play(drums[0]);//offbeat bass drum hits, infrequent
            }
            else if (parameters[1] == 10){
                if (composerCheck > randy.nextDouble() * 100 + 1)
                    play(drums[0]);
                else if (composerCheck < randy.nextDouble() * 100 + 1)
                    play(drums[1]);
            }
        }
        else 
        	fillCompose();
        if (parameters[1] < 2 && currentBeat == 1)
            parameters[4] = ChangeNote();
        else if (parameters[1] < 8 && currentBeat%2 == 1)
            parameters[4] = ChangeNote();
        else if (parameters[1] >= 8)
            parameters[4] = ChangeNote();
        bassCompose();
        leadCompose();
		if (currentBeat != 16) //checks to see what downbeat we are on
	        currentBeat++;
		else {
		    if (currentMeasure != 8)
		    	currentMeasure++;
		    else {
                if (hookCount == 1){ //goes from hook to improvisation
                    hook = !hook;
                    hookCount++;
                }
                else if (hookCount != 4) {
                    hookCount++;
                }
                else {
                    hook = !hook; //returns to hook from improvisation
                    hookCount = 1;
                }
                currentMeasure = 1;
            }
	        currentBeat = 1;
		}
	}
    
    //Composes drum fills every eight measures for stylistic flair
    public void fillCompose(){
    	if (composerCheck > 50){ //50% chance to play this fill (NOTE: might be useful to make this a parameter)
    		play(drums[1]);
    		if ((currentBeat-1)%4 == 0)
    			play(drums[1]);
    	}
    	else { //50% chance to play this fill
    		if (((currentBeat+1)/2)%2 == 1)
    			play(drums[1]);
    		else
    			play(drums[0]);
    	}
    }

    //This method performs composition for the bass line
    public void bassCompose(){
        if (currentBeat == 1)
            play(bass[parameters[3]]);
        if (parameters[1] == 0) {
            if ((currentBeat - 1) % 6 == 0)
                play(bass[parameters[3]]);
        }
        if ((currentBeat-1)%4 == 0 && currentBeat != 1 && parameters[1] > 0 && parameters[1] < 3)
            play(bass[parameters[3]]);
        if (parameters[1] > 3 && parameters [1] < 7) {
            if (currentBeat < 9 && (currentBeat - 1) % 3 == 0)
                play(bass[parameters[3]]);
            if (currentBeat >= 9 && currentBeat % 3 == 0)
                play(bass[parameters[3]]);
        }
        if (parameters[1] > 7){
            switch((currentBeat-1)%4){
                case 0:
                    if (composerCheck < 60)
                        play(bass[parameters[3]]);
                case 1:
                    if (composerCheck < 10)
                        play(bass[parameters[3]]);
                case 2:
                    if (composerCheck < 30)
                        play(bass[parameters[3]]);
                case 3:
                    if (composerCheck < 20)
                        play(bass[parameters[3]]);
            }
        }
    }
    
    //Updates composition parameters based on current slider values
    public void updateParameters (int temp, int comp, int rage) {
    	//Method for changing parameters to current slider values goes here
		parameters[0]=tempoTransform(temp);
        parameters[1]=comp;
        if (parameters[2] != rage) {
            parameters[2] = rage;
            sampleChange();
        }
        //sets up chord changes
        if (parameters[1] < 5 && currentMeasure == 5)
            parameters[3] = 0;
        if (currentMeasure == 1)
            parameters[3] = 0;
        else {
            chordChange = randy.nextDouble() * 100 + 1;
            parameters[3] = ChangeChord();
        }



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

    public int ChangeChord(){
        switch (parameters[3]) {
            case 0:
                if (chordChange < 60)
                    return 3;
                else if (chordChange < 80)
                    return 4;
                else if (chordChange < 90)
                    return 5;
                else
                    return 1;
            case 1:
                if (chordChange < 50)
                    return 5;
                else if (chordChange < 80)
                    return 2;
                else
                    return 4;
            case 2:
                if (chordChange < 40)
                    return 3;
                else if (chordChange < 70){
                    if (composerCheck < 50)
                        return 0;
                    else
                        return 6;
                }
                else if (chordChange < 90)
                    return 5;
                else
                    return 0;
            case 3:
                if (chordChange < 60)
                    return 4;
                else if (chordChange < 90)
                    return 2;
                else {
                    if (composerCheck < 50)
                        return 0;
                    else
                        return 6;
                }
            case 4:
                if (chordChange < 70)
                    return 2;
                if (chordChange < 90)
                    return 3;
                else
                    return 5;
            case 5:
                if (chordChange < 50) {
                    if (composerCheck < 50)
                        return 0;
                    else
                        return 6;
                }
                else if (chordChange < 70)
                    return 3;
                else
                    return 2;
            case 6:
                if (chordChange < 60)
                    return 3;
                else if (chordChange < 80)
                    return 4;
                else if (chordChange < 90)
                    return 5;
                else
                    return 1;
        }
        return 0;
    }

    public int ChangeNote(){
        chordChange = randy.nextDouble()*100 + 1;
        switch(parameters[4]){
            case 0:
                if (chordChange < 50)
                    return 1;
                else
                    return 2;
            case 1:
                if (chordChange < 50){
                    if (composerCheck < 50)
                        return 0;
                    else
                        return 2;
                }
                else
                    return 3;
            case 2:
                if (chordChange < 50){
                    if (composerCheck < 50)
                        return 1;
                    else
                        return 3;
                }
                else {
                    if (composerCheck < 50)
                        return 0;
                    else
                        return 4;
                }
            case 3:
                if (chordChange < 50){
                    if (composerCheck < 50)
                        return 2;
                    else
                        return 4;
                }
                else {
                    if (composerCheck < 50)
                        return 1;
                    else
                        return 5;
                }
            case 4:
                if (chordChange < 50){
                    if (composerCheck < 50)
                        return 3;
                    else
                        return 5;
                }
                else {
                    if (composerCheck < 50)
                        return 2;
                    else
                        return 6;
                }
            case 5:
                if (chordChange < 50){
                    if (composerCheck < 50)
                        return 4;
                    else
                        return 6;
                }
                else
                    return 3;
            case 6:
                if (chordChange < 50)
                    return 5;
                else
                    return 4;
        }
        return 0;
    }

    public void leadCompose(){
        if (parameters[1] < 2 && composerCheck > parameters[5]*20){
            if (currentBeat == 1) {
                play(lead[parameters[4]]);
                parameters[5]++;
            }
        }
        else if (parameters[1] < 9 && composerCheck > parameters[5]*20){
            if (currentBeat%2 == 1){
                if (composerCheck > 30) {
                    play(lead[parameters[4]]);
                    parameters[5]++;
                }
            }
        }
        else if (composerCheck > 30 && composerCheck > parameters[5]*20) {
                play(lead[parameters[4]]);
                parameters[5]++;
        }
        else {
            parameters[5] = 0;
        }
        if (parameters[5] == 6){
            parameters[5] = 0;
        }
    }

    public void sampleChange(){
        if (parameters[2] < 5){
            for (int i = 0; i < 5; i++){
                drums[i] = drumsMaster[i];
            }
        }
        else {
            for (int i = 0; i < 5; i++){
                drums[i] = drumsMaster[i+5];
            }
        }
        if (parameters[2] < 3){
            for (int i = 0; i < 7; i++)
                bass[i] = bassMaster[i];
        }
        else if (parameters[2] < 7){
            for (int i = 0; i < 7; i++)
                bass[i] = bassMaster[i+7];
        }
        else {
            for (int i = 0; i < 7; i++)
                bass[i] = bassMaster[i+14];
        }
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
            timer.schedule(new ComposerTask(), 15000 / tempo);
        }

        class ComposerTask extends TimerTask {
            public void run() {
                compose();
                timer.cancel();
            }
        }
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(Composer.this);
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("Loading...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Loading application View, please wait...");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(100);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Get the current thread's token
                synchronized (this) {

                    drumsMaster[0] = soundPool.load(Composer.this, R.raw.bassdrum1, 1);//drum sounds
                    drumsMaster[1] = soundPool.load(Composer.this, R.raw.snare1, 1);
                    drumsMaster[2] = soundPool.load(Composer.this, R.raw.hihat1, 1);
                    drumsMaster[3] = soundPool.load(Composer.this, R.raw.crash1, 1);
                    drumsMaster[4] = soundPool.load(Composer.this, R.raw.snare2, 1);
                    drumsMaster[5] = soundPool.load(Composer.this, R.raw.bassdrum2, 1);//drum sounds
                    drumsMaster[6] = soundPool.load(Composer.this, R.raw.snare3, 1);
                    drumsMaster[7] = soundPool.load(Composer.this, R.raw.hihat2, 1);
                    drumsMaster[8] = soundPool.load(Composer.this, R.raw.crash2, 1);
                    drumsMaster[9] = soundPool.load(Composer.this, R.raw.snare4, 1);
                    progressDialog.setProgress(40);
                    bassMaster[0] = soundPool.load(Composer.this, R.raw.cbass1e, 1);//bass sounds
                    bassMaster[1] = soundPool.load(Composer.this, R.raw.cbass1g, 1);
                    bassMaster[2] = soundPool.load(Composer.this, R.raw.cbass1a, 1);
                    bassMaster[3] = soundPool.load(Composer.this, R.raw.cbass1b, 1);
                    bassMaster[4] = soundPool.load(Composer.this, R.raw.cbass1c, 1);
                    bassMaster[5] = soundPool.load(Composer.this, R.raw.cbass1d, 1);
                    bassMaster[6] = soundPool.load(Composer.this, R.raw.cbass2e, 1);
                    bassMaster[7] = soundPool.load(Composer.this, R.raw.bass1e, 1);//bass sounds
                    bassMaster[8] = soundPool.load(Composer.this, R.raw.bass1g, 1);
                    bassMaster[9] = soundPool.load(Composer.this, R.raw.bass1a, 1);
                    bassMaster[10] = soundPool.load(Composer.this, R.raw.bass1b, 1);
                    bassMaster[11] = soundPool.load(Composer.this, R.raw.bass1c, 1);
                    bassMaster[12] = soundPool.load(Composer.this, R.raw.bass1d, 1);
                    bassMaster[13] = soundPool.load(Composer.this, R.raw.bass2e, 1);
                    progressDialog.setProgress(80);
                    bassMaster[14] = soundPool.load(Composer.this, R.raw.rbass1e, 1);
                    bassMaster[15] = soundPool.load(Composer.this, R.raw.rbass1g, 1);
                    bassMaster[16] = soundPool.load(Composer.this, R.raw.rbass1a, 1);
                    bassMaster[17] = soundPool.load(Composer.this, R.raw.rbass1b, 1);
                    bassMaster[18] = soundPool.load(Composer.this, R.raw.rbass1c, 1);
                    bassMaster[19] = soundPool.load(Composer.this, R.raw.rbass1d, 1);
                    bassMaster[20] = soundPool.load(Composer.this, R.raw.rbass2e, 1);
                    progressDialog.setProgress(100);
                }
            }
            finally {
                return null;
            }

        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {
            //close the progress dialog
            progressDialog.dismiss();
        }
    }
}
