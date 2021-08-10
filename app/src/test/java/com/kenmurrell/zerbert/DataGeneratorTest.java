package com.kenmurrell.zerbert;

import org.junit.Test;

public class DataGeneratorTest
{
    static String text = "There now is your insular city of the Manhattoes, belted round by wharves as Indian isles by coral reefs—commerce surrounds it with her surf. Right and left, the streets take you waterward. Its extreme downtown is the battery, where that noble mole is washed by waves, and cooled by breezes, which a few hours previous were out of sight of land. Look at the crowds of water-gazers there.\n" +
            "\n\n" +
            "Circumambulate the city of a dreamy Sabbath afternoon. Go from Corlears Hook to Coenties Slip, and from thence, by Whitehall, northward. What do you see?—Posted like silent sentinels all around the town, stand thousands upon thousands of mortal men fixed in ocean reveries. Some leaning against the spiles; some seated upon the pier-heads; some looking over the bulwarks of ships from China; some high aloft in the rigging, as if striving to get a still better seaward peep. But these are all landsmen; of week days pent up in lath and plaster— tied to counters, nailed to benches, clinched to desks. How then is this? Are the green fields gone? What do they here?";
    static String key = "MOBY";

    @Test
    public void GenerateNoteTest()
    {
        System.out.println(DataUtils.encryptText(text, key));
    }

    @Test
    public void GenerateHashTest()
    {
        System.out.println(DataUtils.SHA1(key));
    }
}
