package net.sf.tapestry.junit.mock.c9;

import net.sf.tapestry.html.BasePage;

/**
 *  Used to validate that the enhancer will reject classes where the
 *  property exists (that is, with non-abstract accessors).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class Four extends BasePage
{
	private String _word;
	
    public String getWord()
    {
        return _word;
    }

    public void setWord(String word)
    {
        _word = word;
    }

}
