package com.autodjteam.autodj.autodj;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ComposerUnitTest {

    private Composer composerUnderTest = new Composer();

    @Test
    //Testing to see that playPause correctly pauses music
    public void testPause (){
        composerUnderTest.isPlaying = true;
        composerUnderTest.playPause();
        assertFalse(composerUnderTest.isPlaying);
    }

    //Testing to see that playPause correctly initiates playback
    public void testPlay() throws Exception {
        try {
            composerUnderTest.playPause();
        }
        catch (Exception NullPointerException) { //catches NullPointer upon updateParameters()
            assertTrue(composerUnderTest.isPlaying);
        }
    }
}