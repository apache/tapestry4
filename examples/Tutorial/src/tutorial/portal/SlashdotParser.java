package tutorial.portal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.tapestry.IAsset;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.asset.ExternalAsset;
import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 * Parser for the Slashdot format.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SlashdotParser extends AbstractDocumentParser
{
    private static class URLResourceLocation implements IResourceLocation
    {
        private URL _URL;

        private URLResourceLocation(URL URL)
        {
            _URL = URL;
        }

        public IResourceLocation getLocalization(Locale locale)
        {
            return this;
        }

        public String getName()
        {
            return _URL.getFile();
        }

        public IResourceLocation getRelativeLocation(String name)
        {
            return this; // Not right, but good enough.
        }

        public URL getResourceURL()
        {
            return _URL;
        }

        public IAsset toAsset()
        {
            return new ExternalAsset(_URL.toString());
        }

    }

    public List parseStories(URL URL) throws DocumentParseException
    {
        IResourceLocation location = new URLResourceLocation(URL);

        try
        {
            Document document = parse(location, "backslash");

            return build(document);
        }
        finally
        {
            setResourceLocation(null);
        }

    }

    /**
     *  Returns false, since there's no DTD for the document
     *  we need to parse.
     *
     **/

    protected boolean getRequireValidatingParser()
    {
        return false;
    }

    private List build(Document document) throws DocumentParseException
    {
        List result = new ArrayList();
        Node node = document.getDocumentElement();

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
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

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
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