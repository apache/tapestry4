package tutorial.hangman;

/**
 *
 *  Exception thrown if a guess is in error.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class GameException extends Exception
{
	public GameException(String message)
	{
		super(message);
	}
}