package com.kenmurrell.zerbert;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmotionDecoderTest
{
    @Test
    public void EncodeTest()
    {
        assertEquals("Zerbert#love", EmotionDecoder.encode("love"));
    }

    @Test
    public void DecodeTest()
    {
        assertEquals("love", EmotionDecoder.decode("Zerbert#love").orElseThrow(AssertionError::new));
        assertEquals("baby", EmotionDecoder.decode("Zerbert#baby").orElseThrow(AssertionError::new));
        assertEquals("horny", EmotionDecoder.decode("Zerbert#horny").orElseThrow(AssertionError::new));
        assertEquals("angry", EmotionDecoder.decode("Zerbert#angry").orElseThrow(AssertionError::new));
        assertEquals("hungry", EmotionDecoder.decode("Zerbert#hungry").orElseThrow(AssertionError::new));
        assertEquals("sad", EmotionDecoder.decode("Zerbert#sad").orElseThrow(AssertionError::new));
    }
}
