package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.util.prop.*;
import com.primix.tapestry.valid.*;
import java.io.*;
import java.util.*;
import java.text.*;


/**
 *  Page that presents (fake) stock information.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Stocks
    extends ErrorPage
{
    /**
     * For this tutorial, we have a shared set of 'base stocks' that are
     * used by everyone.  Individual users can add addtional stocks
     * to the list.
     *
     */
    
    private static Stock[] stockBase =
    {
        new Stock("DJIA", 11005.37, -117.05),
            new Stock("NASDAQ", 2251.03, -30.99),
            new Stock("NYSE", 647.13, -5.85)
    };

    private static Stock[] otherStocks =
    {
        new Stock("PMIX", 12.73, 1.01), // Wishful thinking
            new Stock("MSFT", 70.91, -0.81),
            new Stock("SUN", 41.32, .2)
    };
    
    private String tickerId;
    private Stock stock;
    /**
     *  The composite list of stocks, by combining stockBase with
     *  userStocks.
     *
     */
    
    private List stocks;
    
    /**
     *  The list of stocks for this user.
     *
     */
    
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
    
    public IActionListener getFormListener()
    {
        return new IActionListener()
        {
            public void actionTriggered(IComponent component, IRequestCycle cycle)
                throws RequestCycleException
            {
                addStock();
            }
        };
    }
    
    private void addStock()
    {
        int i;
        
        if (Tapestry.isNull(tickerId))
        {
            setErrorField("inputTickerId", "No stock ticker value entered.");
            return;
        }
        
        String newId = tickerId.trim().toUpperCase();
        List existingStocks = getStocks();
        int count = existingStocks.size();
        for (i = 0; i < count; i++)
        {
            Stock s = (Stock)existingStocks.get(i);
            if (s.tickerId.equals(newId))
            {
                setErrorField("inputTickerId", "Already in list.");
                return;
            }
        }
        
        for (i = 0; i < otherStocks.length; i++)
        {
            if (otherStocks[i].tickerId.equals(newId))
            {
                stocks = null;
                
                if (userStocks == null)
                    setUserStocks(new ArrayList());
                
                userStocks.add(otherStocks[i]);
                
                tickerId = null;
                ((IValidatingTextField)getNestedComponent("inputTickerId")).refresh();
                
                return;
            }
        }
        
        setErrorField("inputTickerId", "Unrecognized ticker id.  Try 'PMIX', 'MSFT' or 'SUN' (for the demo).'");
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
