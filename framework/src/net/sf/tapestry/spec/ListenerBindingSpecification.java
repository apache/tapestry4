package net.sf.tapestry.spec;

/**
 *  Special subclass of {@link net.sf.tapestry.spec.BindingSpecification} used
 *  to encapsulate additional information the additional information 
 *  specific to listener bindings.  In a ListenerBindingSpecification, the
 *  value property is the actual script (and is aliased as property script), 
 *  but an additional property,
 *  language, (which may be null) is needed.  This is the language
 *  the script is written in.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class ListenerBindingSpecification extends BindingSpecification
{
    protected String _language;
    
    public ListenerBindingSpecification(String language, String script)
    {
        super(BindingType.LISTENER, script);
        
        _language = language;
    }
    
    public String getLanguage()
    {
        return _language;
    }
    
    public String getScript()
    {
        return getValue();
    }
}
