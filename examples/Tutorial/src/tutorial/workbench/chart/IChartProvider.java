package tutorial.workbench.chart;

import com.jrefinery.chart.JFreeChart;

/**
 *  An object which can provide a {@link JFreeChart} (to the {@link ChartService}).
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 * 
 **/

public interface IChartProvider
{
    public JFreeChart getChart();
}