package tutorial.hangman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 *  Visit information for the Hangman Application.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Visit implements Serializable
{
    private static final String[] words =
        {
            "tapestry",
            "servlet",
            "microsoft",
            "tango",
            "gorilla",
            "internet",
            "application",
            "developer",
            "tutorial",
            "hangman",
            "dogfight",
            "greyhound",
            "massacre",
            "universe" };

    private List wordList;

    private HangmanGame game;

    public HangmanGame getGame()
    {
        if (game == null)
            game = new HangmanGame();

        return game;
    }

    /**
     *  Starts a new game with a randomly selected word.
     *
     **/

    public void start(int maxMisses)
    {
        getGame().start(getWord(), maxMisses);
    }

    private String getWord()
    {
        if (wordList == null)
            wordList = new ArrayList();

        if (wordList.size() == 0)
        {
            // Create a list of words that the user will see and shuffle them
            // in random order.  This prevents repeats of words until the
            // user has seen them all.

            wordList.addAll(Arrays.asList(words));
            Collections.shuffle(wordList);
        }

        // Remove the last word in the list.

        return (String) wordList.remove(wordList.size() - 1);
    }
}