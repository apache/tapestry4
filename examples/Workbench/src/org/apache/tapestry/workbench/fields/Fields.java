/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.workbench.fields;

import java.math.BigDecimal;

import org.apache.tapestry.workbench.WorkbenchValidationDelegate;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Fields extends BasePage
{
    public static final int INT_MIN = 5;
    public static final int INT_MAX = 20;
    public static final double DOUBLE_MIN = 3.14;
    public static final double DOUBLE_MAX = 27.5;
    public static final BigDecimal DECIMAL_MIN = new BigDecimal("2");

    public static final BigDecimal DECIMAL_MAX =
        new BigDecimal("100.123456234563456734563456356734567456784567456784567845675678456785678");

    public static final long LONG_MIN = 6;
    public static final long LONG_MAX = 21;

    public static final int STRING_MIN_LENGTH = 3;

    private boolean _clientValidationEnabled = true;
    
    public void detach()
    {
        _clientValidationEnabled = true;
        
        super.detach();
    }
       
    
    public void clientValidationChanged(IRequestCycle cycle)
    {
        // Do nothing.
    }

    public void formSubmit(IRequestCycle cycle)
    {

        WorkbenchValidationDelegate delegate = (WorkbenchValidationDelegate) getBeans().getBean("delegate");

        // If no error message, advance to the Results page,

        // otherwise, stay here and show the error message.

        if (!delegate.getHasErrors())
            cycle.activate("FieldsResults");
    }

    public boolean isClientValidationEnabled()
    {
        return _clientValidationEnabled;
    }

    public void setClientValidationEnabled(boolean clientValidationEnabled)
    {
        _clientValidationEnabled = clientValidationEnabled;
        
        fireObservedChange("clientValidationEnabled", _clientValidationEnabled);
    }

}