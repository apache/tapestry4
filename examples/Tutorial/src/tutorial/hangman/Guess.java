//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package tutorial.hangman;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *
 *  Guess page of hangman tutorial; provides a list of guessable
 *  letters for the word.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Guess extends BasePage
{
    private HangmanGame game;
    private String error;

    public void detach()
    {
        game = null;
        error = null;

        super.detach();
    }

    public String getError()
    {
        return error;
    }

    public void makeGuess(String context[], IRequestCycle cycle)
    {
        HangmanGame game = getGame();
        String guess = context[0];
        char letter = guess.charAt(0);

        try
        {
            game.guess(letter);
        }
        catch (GameException ex)
        {
            error = ex.getMessage();

            if (game.getFailed())
                cycle.setPage("Failed");

            return;
        }

        // A good guess.

        if (game.getDone())
            cycle.setPage("Success");
    }

    /**
     *  Gets the current guess string (which shows the correctly guessed letters,
     *  with underscores for unguessed letters) and converts it for presentation
     *  by adding additional spaces between each character.
     *
     **/

    public String getGuessed()
    {
        StringBuffer buffer;
        char[] guessed;

        guessed = getGame().getGuessed();

        buffer = new StringBuffer(2 * guessed.length);
        for (int i = 0; i < guessed.length; i++)
        {
            if (i > 0)
                buffer.append(' ');

            buffer.append(guessed[i]);
        }

        return buffer.toString();
    }

    public Character[] getUnused()
    {
        return convertCharArray(getGame().getUnusedLetters());
    }

    private HangmanGame getGame()
    {
        if (game == null)
        {
            Visit visit = (Visit) getVisit();
            game = visit.getGame();
        }

        return game;
    }

    private Character[] convertCharArray(char[] array)
    {
        Character[] result = new Character[array.length];

        for (int i = 0; i < array.length; i++)
            result[i] = new Character(array[i]);

        return result;
    }

}