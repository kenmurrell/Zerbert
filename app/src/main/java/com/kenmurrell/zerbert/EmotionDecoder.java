package com.kenmurrell.zerbert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmotionDecoder
{

    private final List<Emotion> emotionList;
    public EmotionDecoder()
    {
        this.emotionList = new ArrayList<Emotion>();
    }

    public void register(Emotion e)
    {
        emotionList.add(e);
    }

    public String encode(String item)
    {
        for(Emotion e : emotionList)
        {
            if(e.getId().equals(item))
            {
                return e.encode();
            }
        }
        throw new UnsupportedOperationException("Emotion is not supported");
    }

    public Optional<Emotion> decode(String text)
    {
        for(Emotion e :  emotionList)
        {
            Optional<Emotion> opt = e.decode(text, 0);
            if(opt.isPresent())
            {
                return opt;
            }
        }
        return Optional.empty();
    }
}
