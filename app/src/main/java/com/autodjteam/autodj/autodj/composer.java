
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

public class composer {
    private int currentBeat;
    private double composerCheck; //stores a randomly-generated number for comparison
    private Random randy;
    public int[] parameters; //array of composition parameters
    public int[] sounds;
    public boolean isPlaying;
    public performer playback;
    
    public composer(){
        currentBeat = 1;
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
    		new tempoTimer(parameters[0]);
    	}
        if (currentBeat == 1){ //if we are on the downbeat, perform these steps
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
    	isPlaying = !isPlaying;
        if (isPlaying)
        	compose();
    }
    
    /* helper Classes go here
     * 
     * Helper Class tempoTimer allows the composer to halt for the time in between beats
     * creating a rhythm for composition and playback
     */
    public class tempoTimer {
    	Timer timer; 
    	
    	public tempoTimer(int tempo) {
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
