package net.sf.tapestry.contrib.form;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  Defines an object that works with a {@link MultiplePropertySelection} component
 *  to render the individual elements obtained from the {@link IPropertySelectionModel model}.
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public interface IMultiplePropertySelectionRenderer
{
    /**
     *  Begins the rendering of the {@link MultiplePropertySelection}.
     *
     **/

    public void beginRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException;

    /**
     *  Invoked for each element obtained from the {@link IPropertySelectionModel model}.
     *
     **/

    public void renderOption(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle,
        IPropertySelectionModel model,
        Object option,
        int index,
        boolean selected)
        throws RequestCycleException;

    /**
     *  Ends the rendering of the {@link MultiplePropertySelection}.
     *
     **/

    public void endRender(
        MultiplePropertySelection component,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException;
}