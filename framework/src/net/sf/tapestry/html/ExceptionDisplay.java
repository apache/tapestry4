package net.sf.tapestry.html;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.bean.EvenOdd;
import net.sf.tapestry.util.exception.ExceptionDescription;

/**
 *  Component used to display an already formatted exception.
 * 
 *  [<a href="../../../../../ComponentReference/ExceptionDisplay.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionDisplay extends BaseComponent
{
    private IBinding _exceptionsBinding;
    private ExceptionDescription _exception;
    private int _count;
    private int _index;
    private EvenOdd _evenOdd;

    public void setExceptionsBinding(IBinding value)
    {
        _exceptionsBinding = value;
    }

    public IBinding getExceptionsBinding()
    {
        return _exceptionsBinding;
    }

    /**
     *  Each time the current exception is set, as a side effect,
     *  the evenOdd helper bean is reset to even.
     * 
     **/
    
    public void setException(ExceptionDescription value)
    {
        _exception = value;
        
        _evenOdd.setEven(true);
    }

    public ExceptionDescription getException()
    {
        return _exception;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        ExceptionDescription[] exceptions =
            (ExceptionDescription[]) _exceptionsBinding.getObject(
                "exceptions",
                ExceptionDescription[].class);

        _count = exceptions.length;
        
        try
        {
            _evenOdd = (EvenOdd)getBeans().getBean("evenOdd");
            
            super.renderComponent(writer, cycle);
        }
        finally
        {
            _exception = null;
            _evenOdd = null;
        }
    }

    public void setIndex(int value)
    {
        _index = value;
    }

    public boolean isLast()
    {
        return _index == (_count - 1);
    }
}