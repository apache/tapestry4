/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package tutorial.portal;

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
        {
            new Stock("DJIA", 11005.37, -117.05),
            new Stock("NASDAQ", 2251.03, -30.99),
            new Stock("NYSE", 647.13, -5.85)};

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