/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.workbench.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jrefinery.chart.ChartFactory;
import com.jrefinery.chart.JFreeChart;
import com.jrefinery.data.DefaultPieDataset;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 *  Demonstrates more complex form handling (including loops and dynamic addition/deletion of
 *  rows) as well as dynamic image generation using {@link JFreeChart}.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 * 
 **/

public class Chart extends BasePage implements IChartProvider
{
    private List _plotValues;
    private List _removeValues;
    private PlotValue _plotValue;

    public void initialize()
    {
        _plotValues = null;
        _removeValues = null;
        _plotValue = null;
    }

    /**
     *  Invokes {@link #getPlotValues()}, which ensures that (on the very first request cycle),
     *  the persistent values property is set <em>before</em> the page recorder is locked.
     * 
     **/

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        getPlotValues();
    }

    public List getPlotValues()
    {
        if (_plotValues == null)
        {
            _plotValues = new ArrayList();

            _plotValues.add(new PlotValue("Fred", 10));
            _plotValues.add(new PlotValue("Barney", 15));
            _plotValues.add(new PlotValue("Dino", 7));

            fireObservedChange("plotValues", _plotValues);
        }

        return _plotValues;
    }

    public void setPlotValues(List plotValues)
    {
        _plotValues = plotValues;

        fireObservedChange("plotValues", plotValues);
    }

    public PlotValue getPlotValue()
    {
        return _plotValue;
    }

    public void setPlotValue(PlotValue plotValue)
    {
        _plotValue = plotValue;
    }

    /**
     *  Invoked during the render; always returns false.
     * 
     **/

    public boolean isMarkedForDeletion()
    {
        return false;
    }

    /**
     *  Invoked by the deleted checkbox (for each plotValue).  If true,
     *  the the current plotValue is added to the list of plotValues to
     *  remove (though the actual removing is done inside {@link #delete(IRequestCycle)},
     *  after the loop.
     *
     **/

    public void setMarkedForDeletion(boolean value)
    {
        if (value)
        {
            if (_removeValues == null)
                _removeValues = new ArrayList();

            _removeValues.add(_plotValue);

            // Deleting things screws up the validation delegate.
            // That's because the errors are associated with the form name
            // (not the component id), and deleting elements causes
            // all the names to shift.

            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");

            delegate.clear();
        }
    }

    /**
     *  Form listener method; does nothing since we want to stay on this page.
     * 
     **/

    public void submit(IRequestCycle cycle)
    {
    }

    /**
     *  Listener method for the add button, adds an additional (blank) plot value.
     * 
     **/

    public void add(IRequestCycle cycle)
    {
        List plotValues = getPlotValues();

        plotValues.add(new PlotValue());

        setPlotValues(plotValues);
    }

    /**
     *  Listener method for the remove button, removes any checked plot values.
     * 
     *  @see #setMarkedForDeletion(boolean)
     * 
     **/

    public void delete(IRequestCycle cycle)
    {
        if (_removeValues != null)
        {
            List plotValues = getPlotValues();

            plotValues.removeAll(_removeValues);

            setPlotValues(plotValues);
        }
    }

    private IAsset chartImageAsset;

    public IAsset getChartImageAsset()
    {
        if (chartImageAsset == null)
            chartImageAsset = new ChartAsset(getRequestCycle(), this);

        return chartImageAsset;
    }

    /**
     *  This method is invoked by the service (in a seperate request cycle from all the form handling stuff).
     *  The {@link #getChartImageAsset()} method provides an {@link IAsset} that is handled by the 
     *  {@link ChartService}, and the asset encodes the identity of this page.
     * 
     **/

    public JFreeChart getChart()
    {
        DefaultPieDataset data = new DefaultPieDataset();

        List plotValues = getPlotValues();

        int count = plotValues.size();

        for (int i = 0; i < count; i++)
        {
            PlotValue pv = (PlotValue) plotValues.get(i);

            String name = pv.getName();

            if (Tapestry.isNull(name))
                name = "<New>";

            data.setValue(name, new Integer(pv.getValue()));
        }

        JFreeChart result = ChartFactory.createPieChart("Pie Chart", data, false);

        result.setBackgroundPaint(Color.decode("#ffffcc"));

        return result;
    }

}