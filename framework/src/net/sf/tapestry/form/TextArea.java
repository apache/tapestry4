package net.sf.tapestry.form;

import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a component that manages an HTML &lt;textarea&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/TextArea.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class TextArea extends AbstractFormComponent
{
    private int _rows;
    private int _columns;
    private boolean _disabled;
    private String _name;

    public String getName()
    {
        return _name;
    }

    /** @since 2.2 **/

    private String _value;

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        if (rewinding)
        {
            _value = cycle.getRequestContext().getParameter(_name);

            return;
        }

        writer.begin("textarea");

        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        if (_rows != 0)
            writer.attribute("rows", _rows);

        if (_columns != 0)
            writer.attribute("cols", _columns);

        generateAttributes(writer, cycle);

        if (_value != null)
            writer.print(_value);

        writer.end();

    }

    public int getColumns()
    {
        return _columns;
    }

    public void setColumns(int columns)
    {
        _columns = columns;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public int getRows()
    {
        return _rows;
    }

    public void setRows(int rows)
    {
        _rows = rows;
    }

    /** @since 2.2 **/

    public String getValue()
    {
        return _value;
    }

    /** @since 2.2 **/

    public void setValue(String value)
    {
        _value = value;
    }

}