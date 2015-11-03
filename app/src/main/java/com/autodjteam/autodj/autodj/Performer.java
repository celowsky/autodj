package com.autodjteam.autodj.autodj;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/*Created Andrew Zaruba on 10/26/2015.
        *
        *V1.0
        *
        *This

class, metronome.performer, manages the loading and playback of sound. It will be included
 * in the primary composition loop and used to queue playback.
 */
public class Performer {
    private Context localContext;
    private SoundPool soundBank; //this SoundPool holds all the sounds currently used by AutoDJ
    public Performer(){ //default constructor
        soundBank = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundBank.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int mySoundId, int status) {

            }
        }
        );

    }
    public Performer(Context appContext){ //constructor for the SoundPlayback object
        soundBank = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        localContext = appContext;
    }
    //Methods
    //performer.load(id) Loads a sound and makes it ready for playback
    public int load(String filePath){
        return soundBank.load(filePath, 1);
    }
    //performer.play(id) plays a sound back based on its current id.
    public void play(int id){
        soundBank.play(id,1,1,1,0,1);
    }

}
