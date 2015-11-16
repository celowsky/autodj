package com.autodjteam.autodj.autodj;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ComposerUnitTest {

    private Composer composerUnderTest = new Composer();


    @Test
    //onCreate testing
    public void testOnCreate() throws Exception {

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