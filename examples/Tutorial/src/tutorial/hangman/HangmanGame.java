/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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
		int index;
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