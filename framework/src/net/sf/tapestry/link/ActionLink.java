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
package net.sf.tapestry.link;

import net.sf.tapestry.IAction;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderRewoundException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A component for creating a link that is handled using the action service.
 * 
 *  [<a href="../../../../../ComponentReference/ActionLink.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ActionLink extends GestureLink implements IAction
{
    private IActionListener _listener;
    private IBinding _statefulBinding;

    /**
     *  Returns true if the stateful parameter is bound to
     *  a true value.  If stateful is not bound, also returns
     *  the default, true.
     * 
     *  <p>Note that this method can be called when the
     *  component is not rendering, therefore it must
     *  directly access the {@link IBinding} for the stateful
     *  parameter.
     *
     **/

    public boolean getRequiresSession()
    {
        if (_statefulBinding == null)
            return true;

        return _statefulBinding.getBoolean();
    }

    /**
     *  Returns {@link Tapestry#ACTION_SERVICE}.
     * 
     **/

    protected String getServiceName()
    {
        return Tapestry.ACTION_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        String actionId;

        actionId = cycle.getNextActionId();

        if (cycle.isRewound(this))
        {
            _listener.actionTriggered(this, cycle);

            throw new RenderRewoundException(this);
        }

        return new Object[] { actionId };
    }

    public IBinding getStatefulBinding()
    {
        return _statefulBinding;
    }

    public void setStatefulBinding(IBinding statefulBinding)
    {
        _statefulBinding = statefulBinding;
    }

    public IActionListener getListener()
    {
        return _listener;
    }

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

}