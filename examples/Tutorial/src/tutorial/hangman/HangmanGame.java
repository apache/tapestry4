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
    private char[] _word;
    private char[] _guessed;
    private boolean[] _used;
    private int _lettersLeft;

    private int _missed;
    private int _maxMisses;

    private static final int N_LETTERS = 26;

    /**
     *  Starts a new game, resetting the number of misses.
     *
     **/

    public void start(String word, int maxMisses)
    {
        _word = word.toUpperCase().toCharArray();

        _guessed = new char[_word.length];
        for (int i = 0; i < _word.length; i++)
            _guessed[i] = '_';

        if (_used == null)
            _used = new boolean[N_LETTERS];

        for (int i = 0; i < N_LETTERS; i++)
            _used[i] = false;

        _lettersLeft = _word.length;
        _missed = 0;
        _maxMisses = maxMisses;
    }

    /**
     *  Returns true when the maximum number of misses has been reached.
     *
     **/

    public boolean getFailed()
    {
        return _missed >= _maxMisses;
    }

    /**
     *  Returns true when all letters have been guessed.
     *
     **/

    public boolean getDone()
    {
        return _lettersLeft == 0;
    }

    /**
     *  Returns an array of characters, each position is either a correctly guessed
     *  letter, or an underscore (for an as-yet unguessed letter).
     *
     **/

    public char[] getGuessed()
    {
        return _guessed;
    }

    /**
     *  Returns the word being guessed.
     *
     **/

    public String getWord()
    {
        return new String(_word);
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

        _used[letter - 'A'] = true;

        for (int i = 0; i < _word.length; i++)
        {
            if (_word[i] == letter)
            {
                found = true;
                // Replace the underscore with the actual letter
                _guessed[i] = letter;
                _lettersLeft--;
            }
        }

        if (!found)
        {
            _missed++;
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
            if (!_used[letter - 'A'])
                buffer[length++] = letter;
        }

        char[] result = new char[length];
        System.arraycopy(buffer, 0, result, 0, length);

        return result;
    }

    public int getMissed()
    {
        return _missed;
    }

    public int getMaxMisses()
    {
        return _maxMisses;
    }
}