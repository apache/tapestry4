package net.sf.tapestry.wml;

import java.io.OutputStream;

import net.sf.tapestry.AbstractPage;
import net.sf.tapestry.IMarkupWriter;

/**
 *  Concrete class for WML decks. Most decks
 *  should be able to simply subclass this, adding new properties and
 *  methods.  An unlikely exception would be a deck that was not based
 *  on a template.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 0.2.9
 * 
 **/

public class Deck extends AbstractPage
{
    public IMarkupWriter getResponseWriter(OutputStream out)
    {
        return new WMLWriter(out);
    }

}