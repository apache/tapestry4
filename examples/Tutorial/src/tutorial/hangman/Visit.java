/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package tutorial.hangman;

import java.io.*;
import java.util.*;

/**
 *
 *  Visit information for the Hangman Application.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Visit
implements Serializable
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
        "universe"
    };

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
     */

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

        return (String)wordList.remove(wordList.size() - 1);
    }
}