package com.kenmurrell.zerbert;

import java.util.Optional;

public class Emotion
{
    private static final String encodedBase = "Zerbert";
    private static final String padding = "#";

    private final String id;
    private final String[] code;
    private final int gif;
    private String verb;

    public Emotion(String id, int gif)
    {
        this.id = id;
        this.code = new String[]{encodedBase, padding, id, padding};
        this.gif = gif;
        this.verb = "is";
    }

    public String encode()
    {
        return String.join("", code);
    }

    public Optional<Emotion> decode(String text, int i)
    {
        int end = code[i].length();
        if(end > text.length())
        {
            return Optional.empty();
        }
        String chunk = text.substring(0, code[i].length());
        if(chunk.equals(code[i]))
        {
            if(i + 1 == code.length)
            {
                return Optional.of(this);
            }
            return decode(text.substring(code[i].length()), i+1);
        }
        return Optional.empty();
    }

    public String getId()
    {
        return id;
    }

    public int getGif()
    {
        return gif;
    }

    public Emotion setVerbAndReturn(String verb)
    {
        this.verb = verb;
        return this;
    }

    public String getVerb()
    {
        return verb;
    }
}
