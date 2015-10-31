
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
import java.util.Random;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

public class Composer {
    private int currentBeat;
    private int currentMeasure; //checks which measure we are in, for fill's sake.
    private double composerCheck; //stores a randomly-generated number for comparison
    private Random randy;
    public int[] parameters; //array of composition parameters
    public int[] sounds;
    public boolean isPlaying;
    public performer playback;
    
    public Composer(){
        currentBeat = 1;
        currentMeasure = 1;
        parameters = new int[2];
        parameters[0] = 120; //default tempo
        parameters[1] = 50; //default offbeat check
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
    	if (isPlaying) {
    		new TempoTimer(parameters[0]);
    	}
        if (currentBeat == 1){ 
        	updateParameters(); //if we are on the downbeat of a measure, update composition parameters
        }
        
        //Primary Drum composition
        composerCheck = randy.nextDouble()*100 + 1;
        if(currentMeasure == 1 && currentBeat == 1)
        	playback.play(sounds[3]); //cymbal crash after every fill because CLUB MUSIC.
        
        if (currentMeasure != 8) {
			if ((currentBeat-1)%4 == 0){
			    playback.play(sounds[0]); //bass drum (four on the floor)
			}
			if ((currentBeat+1)%4 == 0 && composerCheck < parameters[1])
				playback.play(sounds[0]); //bass drum offbeats
			
			if ((currentBeat-1)%8 == 4){
			    playback.play(sounds[1]); //snare drum (ABSOLUTELY must play on "2 and 4!")
			}
			
			if (currentBeat%2 == 0)
				playback.play(sounds[2]); //offbeat hi-hat cymbal hits
			
			if (currentBeat%2 == 0 && (int) composerCheck < parameters[1]){
			    playback.play(sounds[1]); //offbeat snare hits, occuring infrequently
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
    		playback.play(sounds[1]);
    		if ((currentBeat-1)%4 == 0)
    			playback.play(sounds[1]);
    	}
    	else { //50% chance to play this fill
    		if (((currentBeat+1)/2)%2 == 1)
    			playback.play(sounds[1]);
    		else
    			playback.play(sounds[0]);
    	}
    }
    
    //Updates composition parameters based on current slider values
    public void updateParameters () {
    	//Method for changing parameters to current slider values goes here
    	
    	//so: set parameters[0] (tempo parameter) equal to tempo slider's current value.
    }
    
    //on a press of the play/pause button, playPause toggles the parameter
    //that specifies whether or not the composer is playing music
    //between 0 and 1
    public void playPause(){
    	isPlaying = !isPlaying;
        if (isPlaying)
        	compose();
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
    		timer.schedule(new ComposerTask(), 60000/tempo);
    	}
    	class ComposerTask extends TimerTask {
    		public void run() {
    			timer.cancel();
    			compose();
    		}
    	}
    }
}
