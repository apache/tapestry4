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

    public void makeGuess(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        char letter = ((Character)parameters[0]).charValue();
        
        HangmanGame game = getGame();

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