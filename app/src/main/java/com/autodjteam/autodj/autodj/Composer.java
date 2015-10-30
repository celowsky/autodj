package com.autodjteam.autodj.autodj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

public class Composer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String tempo = intent.getStringExtra(MainActivity.EXTRA_TEMPO);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    private int currentBeat;
    private double composerCheck; //stores a randomly-generated number for comparison
    private Random randy;
    public int[] parameters; //array of composition parameters
    public int[] sounds;
    public double[] probabilities; //list of music event probabilities
    public boolean isPlaying;
    public performer playback;

    public Composer(){
        currentBeat = 1;
        parameters = new int[2];
        parameters[0] = 120; //default tempo
        parameters[1] = 50; //default offbeat check
        probabilities = new double[10];
        isPlaying = false;
        randy = new Random();
    }

    //methods

    //loads all sounds in the library on startup
    public void onStartup(){
        //load sounds (currently four sounds planned)
        //playback.load(alskdjflasd)
    }

    //main composition loop
    public void compose(){
        if (currentBeat == 1){ //if we are on the downbeat, perform these steps

        }
        if (!isPlaying){
            return; //if the user no longer wants to play music
        }
        //Composition goes here

        composerCheck = randy.nextDouble()*100;
        if (currentBeat == 1 || currentBeat == 9){
            playback.play(sounds[0]); //bass drum (four on the floor)
        }
        if (currentBeat == 5 || currentBeat == 13){
            playback.play(sounds[1]); //snare drum (absolutely must play on "2 and 4!)
        }
        if (currentBeat%2 == 0 && (int) composerCheck < parameters[1]){
            playback.play(sounds[1]);
        }

        if (currentBeat != 16) //checks to see what downbeat we are on
            currentBeat++;
        else
            //make changes to variables here
            currentBeat = 1;

    }


    //on a press of the play/pause button, playPause toggles the parameter
    //that specifies whether or not the composer is playing music
    //between 0 and 1
    public void playPause(){
        if (!isPlaying) {
            isPlaying = true;
            compose();
        }
        else {
            isPlaying = false;
        }
    }
}
