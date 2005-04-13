// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.workbench.chart;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jCharts.Chart;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PieChart2DProperties;
import org.jCharts.test.TestDataGenerator;

/**
 * Demonstrates more complex form handling (including loops and dynamic addition/deletion of rows)
 * as well as dynamic image generation using JCharts.
 * 
 * @author Howard Lewis Ship, Luis Neves
 * @since 1.0.10
 */

public abstract class ChartPage extends BasePage implements IChartProvider, PageBeginRenderListener
{

    /**
     * Invokes {@link #getPlotValues()}, which ensures that (on the very first request cycle), the
     * persistent values property is set <em>before</em> the page recorder is locked.
     */

    public void pageBeginRender(PageEvent event)
    {
        if (getPlotValues() == null)
        {
            List plotValues = new ArrayList();

            plotValues.add(new PlotValue("Fred", 10));
            plotValues.add(new PlotValue("Barney", 15));
            plotValues.add(new PlotValue("Dino", 7));

            setPlotValues(plotValues);
        }
    }

    public abstract List getPlotValues();

    public abstract void setPlotValues(List plotValues);

    public abstract PlotValue getPlotValue();

    public abstract List getRemoveValues();

    public abstract void setRemoveValues(List removeValues);

    /**
     * Invoked during the render; always returns false.
     */

    public boolean isMarkedForDeletion()
    {
        return false;
    }

    /**
     * Invoked by the deleted checkbox (for each plotValue). If true, the the current plotValue is
     * added to the list of plotValues to remove (though the actual removing is done inside
     * {@link #delete(IRequestCycle)}, after the loop.
     */

    public void setMarkedForDeletion(boolean value)
    {
        if (value)
        {
            List removeValues = getRemoveValues();

            if (removeValues == null)
            {
                removeValues = new ArrayList();
                setRemoveValues(removeValues);
            }

            removeValues.add(getPlotValue());

            // Deleting things screws up the validation delegate.
            // That's because the errors are associated with the form name
            // (not the component id), and deleting elements causes
            // all the names to shift.

            IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");

            delegate.clear();
        }
    }

    /**
     * Listener method for the add button, adds an additional (blank) plot value.
     */

    public void add()
    {
        List plotValues = getPlotValues();

        plotValues.add(new PlotValue());

        setPlotValues(plotValues);
    }

    /**
     * Listener method for the remove button, removes any checked plot values.
     * 
     * @see #setMarkedForDeletion(boolean)
     */

    public void delete()
    {
        List removeValues = getRemoveValues();

        if (removeValues != null)
        {
            List plotValues = getPlotValues();

            plotValues.removeAll(removeValues);

            setPlotValues(plotValues);
        }
    }

    private IAsset chartImageAsset;

    public IAsset getChartImageAsset()
    {
        return new ChartAsset(getRequestCycle(), this);
    }

    /**
     * This method is invoked by the service (in a seperate request cycle from all the form handling
     * stuff). The {@link #getChartImageAsset()}method provides an {@link IAsset}that is handled
     * by the {@link ChartService}, and the asset encodes the identity of this page.
     */

    public Chart getChart()
    {
        LegendProperties legendProperties = new LegendProperties();
        legendProperties.setNumColumns(2);
        legendProperties.setPlacement(LegendProperties.RIGHT);
        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setBackgroundPaint(Color.decode("#ffffcc"));

        Chart result = new PieChart2D(getData(), legendProperties, chartProperties, 400, 350);

        return result;
    }

    private PieChartDataSet getData()
    {
        List plotValues = getPlotValues();
        int count = plotValues.size();
        double[] data = new double[count];
        String[] labels = new String[count];
        PieChart2DProperties properties = new PieChart2DProperties();

        for (int i = 0; i < count; i++)
        {
            PlotValue pv = (PlotValue) plotValues.get(i);

            String name = pv.getName();

            if (HiveMind.isBlank(name))
                name = "<New>";

            data[i] = new Double(pv.getValue()).doubleValue();
            labels[i] = new String(name);
        }

        Paint[] paints = TestDataGenerator.getRandomPaints(count);

        try
        {
            return new PieChartDataSet("Pie Chart", data, labels, paints, properties);
        }
        catch (ChartDataException e)
        {
            return null;
        }
    }

}