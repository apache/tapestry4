package tutorial.portal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
    public List parseStories(URL url, String resourcePath) throws DocumentParseException
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