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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 *  Parses the Slashdot XML file and provides headlines.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Slashdot extends BasePage
{
    private static final Log LOG = LogFactory.getLog(Slashdot.class);

    /**
     *  The {@link List} of {@link SlashdotStory} items.  This is <em>not</em> cleared
     *  at the end of the request cycle, because it is data that can be shared
     *  between sessions.
     *
     **/

    private List _stories;
    private SlashdotStory _story;
    private long _lastRefresh = 0;

    private static final int REFRESH_INTERVAL = 30 * 1024;

    private static String RESOURCE_PATH = "http://slashdot.org/slashdot.xml";

    /**
     *  The page acts like a cache of parsed stories, so we leave the stories and lastRefresh
     *  properties alone.  This is bending the rules, but valid.
     * 
     **/
    
    public void initialize()
    {
        _story = null;
    }

    public void setStory(SlashdotStory value)
    {
        _story = value;
    }

    public SlashdotStory getStory()
    {
        return _story;
    }

    public List getStories()
    {
        long now = System.currentTimeMillis();

        if (now - _lastRefresh > REFRESH_INTERVAL)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Forcing refresh");

            _stories = null;
        }

        if (_stories == null)
            readStories();

        return _stories;
    }

    private void readStories()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Reading Slashdot stories from " + RESOURCE_PATH);

        URL url = null;
        SlashdotParser parser = new SlashdotParser();

        try
        {
            url = new URL(RESOURCE_PATH);
        }
        catch (MalformedURLException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        try
        {
            _stories = parser.parseStories(url);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        _lastRefresh = System.currentTimeMillis();
    }

}