//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package tutorial.portal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Category;

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
    private static final Category CAT = Category.getInstance(Slashdot.class);

    /**
     *  The {@link List} of {@link SlashdotStory} items.  This is <em>not</em> cleared
     *  at the end of the request cycle, because it is data that can be shared
     *  between sessions.
     *
     **/

    private List stories;
    private SlashdotStory story;
    private long lastRefresh = 0;

    private static final int REFRESH_INTERVAL = 30 * 1024;

    private static String RESOURCE_PATH = "http://slashdot.org/slashdot.xml";

    public void detach()
    {
        story = null;

        super.detach();
    }

    public void setStory(SlashdotStory value)
    {
        story = value;
    }

    public SlashdotStory getStory()
    {
        return story;
    }

    public List getStories()
    {
        long now = System.currentTimeMillis();

        if (now - lastRefresh > REFRESH_INTERVAL)
        {
            if (CAT.isDebugEnabled())
                CAT.debug("Forcing refresh");

            stories = null;
        }

        if (stories == null)
            readStories();

        return stories;
    }

    private void readStories()
    {
        if (CAT.isDebugEnabled())
            CAT.debug("Reading Slashdot stories from " + RESOURCE_PATH);

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
            stories = parser.parseStories(url, RESOURCE_PATH);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

        lastRefresh = System.currentTimeMillis();
    }

}