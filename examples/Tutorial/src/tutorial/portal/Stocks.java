package tutorial.portal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IValidationDelegate;
import net.sf.tapestry.valid.RenderString;

/**
 *  PageLink that presents (fake) stock information.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Stocks extends BasePage
{
    /**
     * For this tutorial, we have a shared set of 'base stocks' that are
     * used by everyone.  Individual users can add addtional stocks
     * to the list.
     *
     **/

    private static Stock[] stockBase =
        {
            new Stock("DJIA", 11005.37, -117.05),
            new Stock("NASDAQ", 2251.03, -30.99),
            new Stock("NYSE", 647.13, -5.85)};

    private static Stock[] otherStocks = { new Stock("PMIX", 12.73, 1.01), // Wishful thinking
        new Stock("MSFT", 70.91, -0.81), new Stock("SUN", 41.32, .2)};

    private String tickerId;
    private Stock stock;

    /**
     *  The composite list of stocks, by combining stockBase with
     *  userStocks.
     *
     **/

    private List stocks;

    /**
     *  The list of stocks for this user.
     *
     **/

    private List userStocks;

    public void detach()
    {
        tickerId = null;
        stock = null;
        stocks = null;
        userStocks = null;

        super.detach();
    }

    public List getStocks()
    {
        if (stocks == null)
        {
            stocks = new ArrayList();

            for (int i = 0; i < stockBase.length; i++)
                stocks.add(stockBase[i]);

            if (userStocks != null)
                stocks.addAll(userStocks);
        }

        return stocks;
    }

    private void setErrorField(IValidationDelegate delegate, String id, String message)
    {
        IFormComponent component = (IFormComponent) getComponent(id);

        delegate.setFormComponent(component);
        delegate.record(new RenderString(message), null, null);
    }

    public void addStock(IRequestCycle cycle)
    {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");

        if (delegate.getHasErrors())
            return;

        String newId = tickerId.toUpperCase();
        List existingStocks = getStocks();
        int count = existingStocks.size();
        for (int i = 0; i < count; i++)
        {
            Stock s = (Stock) existingStocks.get(i);
            if (s.tickerId.equals(newId))
            {
                setErrorField(delegate, "inputTickerId", "Already in list.");
                return;
            }
        }

        for (int i = 0; i < otherStocks.length; i++)
        {
            if (otherStocks[i].tickerId.equals(newId))
            {
                stocks = null;

                if (userStocks == null)
                    setUserStocks(new ArrayList());

                userStocks.add(otherStocks[i]);

                tickerId = null;

                return;
            }
        }

        setErrorField(
            delegate,
            "inputTickerId",
            "Unrecognized ticker id.  Try 'PMIX', 'MSFT' or 'SUN' (for the demo).'");
    }

    public void setStock(Stock value)
    {
        stock = value;
    }

    public Stock getStock()
    {
        return stock;
    }

    public DecimalFormat getDecimalFormat()
    {
        return null;
    }

    public void setTickerId(String value)
    {
        tickerId = value;
    }

    public String getTickerId()
    {
        return tickerId;
    }

    public List getUserStocks()
    {
        return userStocks;
    }

    public void setUserStocks(List value)
    {
        userStocks = value;

        fireObservedChange("userStocks", value);
    }

}