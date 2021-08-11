package com.kenmurrell.zerbert;

import java.util.Optional;

public class EmotionDecoder
{
    private static final String encodedBase = "Zerbert";
    private static final String padding = "#";
    private static final String[] format = new String[]{encodedBase, padding};

    public static String encode(String emotion)
    {
        return String.join("", format) + emotion;
    }

    public static Optional<String> decode(String code)
    {
        return decode(code, 0);
    }

    private static Optional<String> decode(String code, int i)
    {
        if(i >= format.length)
        {
            return Optional.of(code);
        }
        String item = format[i];
        if(code.length() >= item.length() && code.startsWith(item))
        {
            return decode(code.substring(item.length()), i+1);
        }
        else
        {
            return Optional.empty();
        }
    }
}
