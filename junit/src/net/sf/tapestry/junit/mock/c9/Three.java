package net.sf.tapestry.junit.mock.c9;

import net.sf.tapestry.html.BasePage;

/**
 *  Demonstration for a class that has abstract accessor methods and initializes
 *  the property in finishLoad().
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public abstract class Three extends BasePage
{
    public abstract String getWord();

    public abstract void setWord(String word);

    protected void finishLoad()
    {
        setWord("Tapestry");
    }

}
