package net.sf.tapestry.spec;

/**
 *  Stores a binding specification, which identifies the static value
 *  or nested property name for the binding.  The name of the binding (which
 *  matches a bindable property of the contined component) is implicitly known.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class BindingSpecification
{
    private BindingType type;
    private String value;

    public BindingSpecification(BindingType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public BindingType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public void setType(BindingType value)
    {
        type = value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}