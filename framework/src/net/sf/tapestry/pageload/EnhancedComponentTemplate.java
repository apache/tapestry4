package net.sf.tapestry.pageload;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 * @author mindbridge
 *
 */
public class EnhancedComponentTemplate extends BaseComponent 
    implements IEnhancedComponent
{
    private Object _$templateParameterValue;
    private boolean _$templateParameterCached;

    public EnhancedComponentTemplate()
    {
        resetParametersCache();
    }

    /**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
        EnhancedComponentDetachListener detachListener = 
            new EnhancedComponentDetachListener(this);
        getPage().addPageDetachListener(detachListener);
	}

    public void resetParametersCache()
    {
        _$templateParameterCached = false;
    }

    public Object get$TemplateParameter()
    {
        if (!_$templateParameterCached) {
            IBinding binding = getBinding("$TemplateParameter");
            if (binding != null)
                _$templateParameterValue = binding.getObject();
            _$templateParameterCached = true;
        }
        return _$templateParameterValue;
    }
    
    public void set$TemplateParameter(Object value)
    {
        _$templateParameterValue = value;
        IBinding binding = getBinding("$TemplateParameter");
        if (binding != null)
            binding.setObject(value);
        _$templateParameterCached = true;
    }
}

