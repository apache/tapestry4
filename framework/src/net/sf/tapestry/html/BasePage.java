package net.sf.tapestry.html;

import java.io.OutputStream;

import net.sf.tapestry.AbstractPage;
import net.sf.tapestry.IMarkupWriter;

/**
 *  Concrete class for HTML pages. Most pages
 *  should be able to simply subclass this, adding new properties and
 *  methods.  An unlikely exception would be a page that was not based
 *  on a template.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 **/

public class BasePage extends AbstractPage
{
    /**
     *  Returns a new {@link HTMLWriter}.
     *
     **/

    public IMarkupWriter getResponseWriter(OutputStream out)
    {
        return new HTMLWriter(out);
    }
}