package net.sf.tapestry.form;

import net.sf.tapestry.IBinding;

/**
 *  Implements a component that manages an HTML &lt;input type=text&gt; or
 *  &lt;input type=password&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/TextField.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TextField extends AbstractTextField
{
    private IBinding _valueBinding;
    
    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    public String readValue()
    {
        return _valueBinding.getString();
    }

    public void updateValue(String value)
    {
        _valueBinding.setString(value);
    }
}