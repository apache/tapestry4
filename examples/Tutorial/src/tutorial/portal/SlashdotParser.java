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

public class SlashdotParser
    extends AbstractDocumentParser
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
    
    protected DocumentBuilder constructBuilder()
        throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // The big difference, no doctype for the slashdot.xml file.
        
        factory.setValidating(false);
        
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        
        DocumentBuilder result = factory.newDocumentBuilder();
        
        result.setErrorHandler(this);
        result.setEntityResolver(this);
        
        return result;
    }
    
    private List build(Document document)
        throws DocumentParseException
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
    
    private SlashdotStory buildStory(Node node)
        throws DocumentParseException
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
