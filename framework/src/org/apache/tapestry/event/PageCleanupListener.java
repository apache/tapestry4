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

package org.apache.tapestry.event;

import java.util.EventListener;

/**
 *  Listener interface for objects that need to know when the engine
 *  containing the page is discarded.  This is typically relevant
 *  only to components that have persistent page properties that
 *  require some kind of cleanup (typically, because they
 *  are references to EJBs, or something similar).
 *
 *  <p>This mechanism has always been of limited use and stability.  It
 *  has been deprecated.  As of release 2.4, each persistent page property
 *  is stored as an individual {@link javax.servlet.http.HttpSession}
 *  attribute; the {@link javax.servlet.http.HttpSessionBindingListener}
 *  interface can more manageable accomplish the same thing.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *  @deprecated With no replacement.
 * 
 **/

public interface PageCleanupListener extends EventListener
{
    /**
     *  Invoked when the page is notified, by the {@link org.apache.tapestry.IEngine}
     *  to cleanup; this occurs when the engine is discarded
     *  because its {@link javax.servlet.http.HttpSession} was
     *  invalidated.  The page is rolled back to its
     *  last state and then invokes this method.
     *
     *  <p>{@link PageEvent#getRequestCycle()} will return null.
     **/

    public void pageCleanup(PageEvent event);
}