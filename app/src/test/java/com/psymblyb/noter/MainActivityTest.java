package com.psymblyb.noter;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Test
    public void onCreate() {
        assertTrue("failed", MainActivity.validateType());
    }

}