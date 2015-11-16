package com.autodjteam.autodj.autodj;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ApplicationUnitTest {

    private Composer composerUnderTest;

    @Before
    public void setUp() {
        composerUnderTest = new Composer();
    }

    @Test
    public void PlayPause() {
        composerUnderTest.isPlaying = true;
        composerUnderTest.playPause();
        assertFalse(composerUnderTest.isPlaying);
    }
}