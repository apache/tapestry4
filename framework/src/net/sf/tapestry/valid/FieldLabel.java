package net.sf.tapestry.valid;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Used to label an {@link IFormComponent}.  Because such fields
 *  know their displayName (user-presentable name), there's no reason
 *  to hard code the label in a page's HTML template (this also helps
 *  with localization).
 *
 *  [<a href="../../../../../ComponentReference/FieldLabel.html">Component Reference</a>]

 *
 *  @author Howard Lewis Lewis Ship
 *  @version $Id$
 * 
 **/

public class FieldLabel extends AbstractComponent
{
    private IFormComponent _field;
    private String _displayName;

    /**
     *  Gets the {@link IField} 
     *  and {@link IValidationDelegate delegate},
     *  then renders the label obtained from the field.  Does nothing
     *  when rewinding.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;

        String finalDisplayName = (_displayName != null) ? _displayName : _field.getDisplayName();

        if (finalDisplayName == null)
            throw new RequestCycleException(
                Tapestry.getString("FieldLabel.no-display-name", _field.getExtendedId()),
                this);

        IValidationDelegate delegate = Form.get(cycle).getDelegate();

        delegate.writeLabelPrefix(_field, writer, cycle);

        writer.print(finalDisplayName);

        delegate.writeLabelSuffix(_field, writer, cycle);
    }

    public String getDisplayName()
    {
        return _displayName;
    }

    public void setDisplayName(String displayName)
    {
        _displayName = displayName;
    }

    public IFormComponent getField()
    {
        return _field;
    }

    public void setField(IFormComponent field)
    {
        _field = field;
    }

}