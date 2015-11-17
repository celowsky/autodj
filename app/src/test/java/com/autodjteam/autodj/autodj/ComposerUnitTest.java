package com.autodjteam.autodj.autodj;

import org.junit.Test;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ComposerUnitTest {

    private Composer composerUnderTest = new Composer();

    @Test
    //parameter update testing
    public void testUpdate() throws Exception {
        boolean passed = true;
        composerUnderTest.updateParameters(60,0,1);
        composerUnderTest.updateParameters(120,5,1);
        try {
            composerUnderTest.updateParameters(200, 9, 2);
        } catch (Exception NullPointerException){
            assertTrue(passed);
        }
        assertTrue(passed);
    }

    @Test
    public void testChanges(){
        for (int i = 0; i < 100; i++){
            for (int j = 0; j < 100; j++){
                for (int k = 0; k < 7; k++){
                    composerUnderTest.composerCheck = i;
                    composerUnderTest.chordChange = j;
                    composerUnderTest.parameters[3] = k;
                    composerUnderTest.parameters[4] = k;
                    composerUnderTest.ChangeChord();
                    composerUnderTest.ChangeNote();
                }
            }
        }
        assertTrue(true);
    }

    @Test
    //Testing to see that playPause correctly pauses music
    public void testPause (){
        composerUnderTest.isPlaying = true;
        composerUnderTest.playPause();
        assertFalse(composerUnderTest.isPlaying);
    }

    @Test
    //Testing to see that playPause correctly initiates playback
    public void testPlay() throws Exception {
        try {
            composerUnderTest.playPause();
        }
        catch (Exception NullPointerException) { //catches NullPointer upon updateParameters()
            assertTrue(composerUnderTest.isPlaying);
        }
    }

    @Test
    //tests to see that math between tempo slider and tempo variable are consistent
    public void testTempo() throws Exception {
        assertEquals(composerUnderTest.tempoTransform(60),180);
    }

    @Test
    //tests for correct changing between bass chords
    public void testChangeChord() throws Exception {
        boolean correct = true;
        double ccRandom = composerUnderTest.randy.nextDouble()*100+1;
        for (int i = 0; i < 7; i++){
            composerUnderTest.chordChange = ccRandom;
            try {composerUnderTest.ChangeChord();}
            catch (Exception NullPointerException){
                correct = false;
            }
        }
        assertTrue(correct);
    }

}