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

    private static Stock[] _stockBase =
        { new Stock("DJIA", 11005.37, -117.05), new Stock("NASDAQ", 2251.03, -30.99), new Stock("NYSE", 647.13, -5.85)};

    private static Stock[] _otherStocks = { new Stock("PMIX", 12.73, 1.01), // Wishful thinking
        new Stock("MSFT", 70.91, -0.81), new Stock("SUN", 41.32, .2)};

    private String _tickerId;
    private Stock _stock;

    /**
     *  The composite list of stocks, by combining stockBase with
     *  userStocks.
     *
     **/

    private List _stocks;

    /**
     *  The list of stocks for this user, which is stored persistently.
     *
     **/

    private List _userStocks;

    public void initialize()
    {
        _tickerId = null;
        _stock = null;
        _stocks = null;
        _userStocks = null;
    }

    public List getStocks()
    {
        if (_stocks == null)
        {
            _stocks = new ArrayList();

            for (int i = 0; i < _stockBase.length; i++)
                _stocks.add(_stockBase[i]);

            if (_userStocks != null)
                _stocks.addAll(_userStocks);
        }

        return _stocks;
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

        String newId = _tickerId.toUpperCase();
        List existingStocks = getStocks();
        int count = existingStocks.size();

        for (int i = 0; i < count; i++)
        {
            Stock s = (Stock) existingStocks.get(i);
            if (s.getTickerId().equals(newId))
            {
                setErrorField(delegate, "inputTickerId", "Already in list.");
                return;
            }
        }

        for (int i = 0; i < _otherStocks.length; i++)
        {
            if (_otherStocks[i].getTickerId().equals(newId))
            {

                List userStocks = getUserStocks();

                if (userStocks == null)
                    userStocks = new ArrayList();

                userStocks.add(_otherStocks[i]);

                // Don't set a persistant property until its final value
                // is determined; then don't change it.
                
                setUserStocks(userStocks);

                // Force a recompute of the stock list, on redisplay

                _stocks = null;
                _tickerId = null;

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
        _stock = value;
    }

    public Stock getStock()
    {
        return _stock;
    }

    public void setTickerId(String value)
    {
        _tickerId = value;
    }

    public String getTickerId()
    {
        return _tickerId;
    }

    public List getUserStocks()
    {
        return _userStocks;
    }

    public void setUserStocks(List value)
    {
        _userStocks = value;

        fireObservedChange("userStocks", value);
    }

}