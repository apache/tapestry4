/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package tutorial.portal;

import com.primix.tapestry.util.xml.*;
import java.net.*;
import java.util.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;

/**
 * Parser for the Slashdot format.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class SlashdotParser extends AbstractDocumentParser
{
	public List parseStories(URL url, String resourcePath)
		throws DocumentParseException
	{
		try
		{
			InputStream stream = url.openStream();
			InputSource source = new InputSource(stream);
			Document document = parse(source, resourcePath, "backslash");

			return build(document);
		}
		catch (IOException ex)
		{
			throw new DocumentParseException("Unable to read " + resourcePath + ".", ex);
		}
		finally
		{
			setResourcePath(null);
		}

	}

	/**
	 *  Returns false, since there's no DTD for the document
	 *  we need to parse.
	 *
	 */

	protected boolean getRequireValidatingParser()
	{
		return false;
	}

	private List build(Document document) throws DocumentParseException
	{
		List result = new ArrayList();
		Node node = document.getDocumentElement();

		for (Node child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "story"))
			{
				SlashdotStory s = buildStory(child);
				result.add(s);
				continue;
			}
		}

		return result;
	}

	private SlashdotStory buildStory(Node node) throws DocumentParseException
	{
		SlashdotStory result = new SlashdotStory();

		for (Node child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "title"))
			{
				result.title = getValue(child);
				continue;
			}

			if (isElement(child, "author"))
			{
				result.author = getValue(child);
				continue;
			}

			if (isElement(child, "url"))
			{
				result.URL = getValue(child);
				continue;
			}

			if (isElement(child, "time"))
			{
				result.date = getValue(child);
				continue;
			}
		}

		return result;
	}
}