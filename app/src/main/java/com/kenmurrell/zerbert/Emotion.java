package com.kenmurrell.zerbert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Emotion
{

    private final String id;
    private final String cxt;
    private final List<Integer> gifs;
    private final Random r;

    private Emotion(String id, String cxt, List<Integer> gifs)
    {
        this.id = id;
        this.cxt = cxt;
        this.gifs = gifs;
        r = new Random();
    }

    public String getId()
    {
        return id;
    }

    public String getContext()
    {
        return cxt;
    }

    public int getGif()
    {
        if(gifs.isEmpty())
        {
            return 0;
        }
        return gifs.get(r.nextInt(gifs.size()));
    }

    public static class Builder
    {
        private String id;
        private String cxt;
        private final List<Integer> gifs;

        public Builder()
        {
            cxt = "%s is %s";
            gifs = new ArrayList<>();
        }

        public Builder setId(String id)
        {
            this.id = id;
            return this;
        }

        public Builder setCxt(String cxt)
        {
            this.cxt = cxt;
            return this;
        }

        public Builder addGif(int gif)
        {
            gifs.add(gif);
            return this;
        }

        public Emotion build()
        {
            if(id == null)
            {
                throw new UnsupportedOperationException("Must have an id");
            }
            return new Emotion(id, cxt, gifs);
        }

    }




}
