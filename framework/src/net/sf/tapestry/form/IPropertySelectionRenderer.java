package net.sf.tapestry.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Defines an object that works with a {@link PropertySelection} component
 *  to render the individual elements obtained from the {@link IPropertySelectionModel model}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IPropertySelectionRenderer
{
    /**
     *  Begins the rendering of the {@link PropertySelection}.
     *
     **/

    public void beginRender(PropertySelection component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException;

    /**
     *  Invoked for each element obtained from the {@link IPropertySelectionModel model}.
     *
     **/

    public void renderOption(
        PropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
        throws RequestCycleException;

    /**
     *  Ends the rendering of the {@link PropertySelection}.
     *
     **/

    public void endRender(PropertySelection component, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException;
}