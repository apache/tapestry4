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

package org.apache.tapestry.vlib;

import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.callback.PageCallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.vlib.pages.Login;

/**
 *  Base page used for pages that should be protected by the {@link Login} page.
 *  If the user is not logged in, they are redirected to the Login page first.
 *  Also, implements an error property and a validationDelegate.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Protected extends BasePage implements IErrorProperty, PageValidateListener
{
    private IValidationDelegate _validationDelegate;

    public void initialize()
    {
        _validationDelegate = null;
    }

    public IValidationDelegate getValidationDelegate()
    {
        if (_validationDelegate == null)
            _validationDelegate = new VirtualLibraryDelegate();

        return _validationDelegate;
    }

    protected void setErrorField(String componentId, String message)
    {
        IFormComponent component = (IFormComponent) getComponent(componentId);

        IValidationDelegate delegate = getValidationDelegate();

        delegate.setFormComponent(component);
        delegate.record(message, null);
    }

    /**
     *  Returns true if the delegate indicates an error, or the error property is not null.
     *
     **/

    protected boolean isInError()
    {
        return getError() != null || getValidationDelegate().getHasErrors();
    }

    /**
     *  Checks if the user is logged in.  If not, they are sent
     *  to the {@link Login} page before coming back to whatever this
     *  page is.
     *
     **/

	public void pageValidate(PageEvent event)
    {
        Visit visit = (Visit) getVisit();

        if (visit != null && visit.isUserLoggedIn())
            return;

        // User not logged in ... redirect through the Login page.

        Login login = (Login) getRequestCycle().getPage("Login");

        login.setCallback(new PageCallback(this));

        throw new PageRedirectException(login);
    }
}