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