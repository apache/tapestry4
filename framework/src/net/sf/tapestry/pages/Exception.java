package net.sf.tapestry.pages;

import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.exception.ExceptionAnalyzer;
import net.sf.tapestry.util.exception.ExceptionDescription;

/**
 *  Default exception reporting page.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Exception extends BasePage
{
    private ExceptionDescription[] _exceptions;

    public void detach()
    {
        _exceptions = null;

        super.detach();
    }

    public ExceptionDescription[] getExceptions()
    {
        return _exceptions;
    }

    public void setException(Throwable value)
    {
        ExceptionAnalyzer analyzer;

        analyzer = new ExceptionAnalyzer();

        _exceptions = analyzer.analyze(value);
    }

}