package tutorial.hangman;

import java.io.Serializable;

/**
 *
 *  The logic for a game of Hangman.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class HangmanGame implements Serializable
{
	private char[] word;
	private char[] guessed;
	private boolean[] used;
	private int lettersLeft;

	private int missed;
	private int maxMisses;

	private static final int N_LETTERS = 26;

	/**
	 *  Starts a new game, resetting the number of misses.
	 *
	 **/

	public void start(String word, int maxMisses)
	{
		this.word = word.toUpperCase().toCharArray();

		guessed = new char[this.word.length];
		for (int i = 0; i < this.word.length; i++)
			guessed[i] = '_';

		if (used == null)
			used = new boolean[N_LETTERS];

		for (int i = 0; i < N_LETTERS; i++)
			used[i] = false;

		lettersLeft = this.word.length;
		missed = 0;
		this.maxMisses = maxMisses;
	}

	/**
	 *  Returns true when the maximum number of misses has been reached.
	 *
	 **/

	public boolean getFailed()
	{
		return missed >= maxMisses;
	}

	/**
	 *  Returns true when all letters have been guessed.
	 *
	 **/

	public boolean getDone()
	{
		return lettersLeft == 0;
	}

	/**
	 *  Returns an array of characters, each position is either a correctly guessed
	 *  letter, or an underscore (for an as-yet unguessed letter).
	 *
	 **/

	public char[] getGuessed()
	{
		return guessed;
	}

	/**
	 *  Returns the word being guessed.
	 *
	 **/

	public String getWord()
	{
		return new String(word);
	}

	/**
	 *  Guesses a letter.  Returns the number of letters left to guess.
	 *
	 *  @param letter a single letter, in the range 'A' to 'Z'.
	 *
	 *  @throws GameException if the letter doesn't appear
	 *  in the word.
	 *
	 **/

	public void guess(char letter) throws GameException
	{
		boolean found = false;

		used[letter - 'A'] = true;

		for (int i = 0; i < word.length; i++)
		{
			if (word[i] == letter)
			{
				found = true;
				// Replace the underscore with the actual letter
				guessed[i] = letter;
				lettersLeft--;
			}
		}

		if (!found)
		{
			missed++;
			throw new GameException("'" + letter + "' is not in the word.");
		}
	}

	/**
	 *  Returns an array of unused letters that may be guessed.
	 *
	 **/

	public char[] getUnusedLetters()
	{
		char[] buffer = new char[N_LETTERS];
		int length = 0;

		for (char letter = 'A'; letter <= 'Z'; letter++)
		{
			if (!used[letter - 'A'])
				buffer[length++] = letter;
		}

		char[] result = new char[length];
		System.arraycopy(buffer, 0, result, 0, length);

		return result;
	}

	public int getMissed()
	{
		return missed;
	}

	public int getMaxMisses()
	{
		return maxMisses;
	}
}