package com.kenmurrell.zerbert;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmotionTest
{

    @Test
    public void GenerateNoteTest()
    {
        Emotion e = new Emotion("love", 0);
        assertEquals("Zerbert#love#", e.encode());
        assertTrue(e.decode("Zerbert#love#", 0).isPresent());
    }

    @Test
    public void EmotionDecoderTest()
    {
        EmotionDecoder ed = new EmotionDecoder();
        ed.register(new Emotion("love", 0));
        ed.register(new Emotion("baby", 1));
        ed.register(new Emotion("horny", 2));
        ed.register(new Emotion("angry", 3));
        ed.register(new Emotion("hungry", 4));
        ed.register(new Emotion("sad", 5));
        assertTrue(ed.decode("Zerbert#love#").isPresent());
        assertTrue(ed.decode("Zerbert#baby#").isPresent());
        assertTrue(ed.decode("Zerbert#horny#").isPresent());
        assertTrue(ed.decode("Zerbert#angry#").isPresent());
        assertTrue(ed.decode("Zerbert#hungry#").isPresent());
        assertTrue(ed.decode("Zerbert#sad#").isPresent());
    }
}
