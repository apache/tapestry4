package net.sf.tapestry.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.Tapestry;

/**
 *  A wrapper around {@link DocumentBuilder} (itself a wrapper around
 *  some XML parser), this class provides error handling and entity
 *  resolving.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.10
 * 
 **/

public abstract class AbstractDocumentParser implements ErrorHandler, EntityResolver
{
    private static final Log LOG = LogFactory.getLog(AbstractDocumentParser.class);

    private DocumentBuilder _builder;
    private IResourceLocation _resourceLocation;

    /**
     *  Map used to resolve public identifiers to corresponding InputSource.
     *
     **/

    private Map _entities;

    /** 
     * 
     *  Compiler used to convert pattern strings into {@link Pattern}
     *  instances.
     * 
     *  @since 2.2 
     * 
     **/

    protected PatternCompiler _patternCompiler;

    /** 
     * 
     *  Matcher used to match patterns against input strings.
     * 
     *  @since 2.2 
     * 
     **/

    protected PatternMatcher _matcher;

    /** 
     * 
     *  Map of compiled {@link Pattern}s, keyed on pattern
     *  string.  Patterns are lazily compiled as needed.
     * 
     *  @since 2.2 
     * 
     **/

    protected Map _compiledPatterns;

    /**
     *  Simple property names match Java variable names; a leading letter
     *  (or underscore), followed by letters, numbers and underscores.
     * 
     *  @since 2.2
     * 
     **/

    public static final String SIMPLE_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z]\\w*$";

    /**
     *  Invoked by subclasses (usually inside thier constructor) to register
     *  a public id and corresponding input source.  Generally, the source
     *  is a wrapper around an input stream to a package resource.
     *
     *  @param publicId the public identifier to be registerred, generally
     *  the publicId of a DTD related to the document being parsed
     *  @param entityPath the resource path of the entity, typically a DTD
     *  file.  Relative files names are expected to be stored in the same package
     *  as the class file, otherwise a leading slash is an absolute pathname
     *  within the classpath.
     *
     **/

    protected void register(String publicId, String entityPath)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Registering " + publicId + " as " + entityPath);

        if (_entities == null)
            _entities = new HashMap();

        _entities.put(publicId, entityPath);
    }

    public IResourceLocation getResourceLocation()
    {
        return _resourceLocation;
    }

    public void setResourceLocation(IResourceLocation value)
    {
        _resourceLocation = null;
    }

    /** 
     *  Invoked by subclasses to parse a document.  Obtains (or re-uses) a
     *  {@link DocumentBuilder} and parses the document from the {@link InputSource}.
     *
     *  @param resourceLocation location from which parsing occurs
     *  @param rootElementName the expected root element of the {@link Document}, or
     *  null if the rootElementName isn't known before parsing
     *
     *  @throws DocumentParseException wrapped around {@link SAXParseException} or
     *  {@link IOException}, or if the root element is wrong.
     *`
     **/

    protected Document parse(IResourceLocation resourceLocation, String rootElementName) throws DocumentParseException
    {
        boolean error = true;

        _resourceLocation = resourceLocation;

        if (LOG.isDebugEnabled())
            LOG.debug("Parsing " + resourceLocation + " for element " + rootElementName);

        URL url = _resourceLocation.getResourceURL();

        if (url == null)
            throw new DocumentParseException(
                Tapestry.getString("AbstractDocumentParser.missing-resource", resourceLocation),
                resourceLocation);

        try
        {

            InputSource source = new InputSource(url.toExternalForm());

            if (_builder == null)
                _builder = constructBuilder();

            Document document = _builder.parse(source);

            if (getRequireValidatingParser())
            {
                // The document parsed and validated, so it must
                // have a <!DOCTYPE>

                String publicId = document.getDoctype().getPublicId();

                // Ensure its a known type.

                if (!_entities.containsKey(publicId))
                    throw new DocumentParseException(
                        Tapestry.getString("AbstractDocumentParser.unknown-public-id", resourceLocation, publicId),
                        resourceLocation);
            }

            error = false;

            validateRootElement(document, rootElementName);

            return document;
        }
        catch (SAXParseException ex)
        {
            // This constructor captures the line number and column number

            throw new DocumentParseException(
                Tapestry.getString("AbstractDocumentParser.unable-to-parse", resourceLocation, ex.getMessage()),
                resourceLocation,
                ex);
        }
        catch (SAXException ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("AbstractDocumentParser.unable-to-parse", resourceLocation, ex.getMessage()),
                resourceLocation,
                ex);
        }
        catch (IOException ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("AbstractDocumentParser.unable-to-read", resourceLocation, ex.getMessage()),
                resourceLocation,
                ex);
        }
        catch (ParserConfigurationException ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("AbstractDocumentParser.unable-to-construct-builder", ex.getMessage()),
                ex);
        }
        finally
        {
            // If there was an error, discard the builder --- it may be in
            // an unknown and unusable state.

            if (error && _builder != null)
            {
                LOG.debug("Discarding builder due to parse error.");
                _builder = null;
            }
        }
    }

    /**
     *  Validates that the root element of the specified document matches the expected
     *  root element name.
     * 
     *  @throws DocumentParseException if the root element is not as expected.
     * 
     *  @since 2.2
     * 
     **/

    protected void validateRootElement(Document document, String rootElementName) throws DocumentParseException
    {

        Element root = document.getDocumentElement();
        if (!root.getTagName().equals(rootElementName))
        {
            throw new DocumentParseException(
                Tapestry.getString(
                    "AbstractDocumentParser.incorrect-document-type",
                    rootElementName,
                    root.getTagName()),
                _resourceLocation);
        }
    }

    /**
     *  Throws the exception, which is caught and wrapped
     *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
     *
     **/

    public void warning(SAXParseException exception) throws SAXException
    {
        throw exception;
    }

    /**
     *  Throws the exception, which is caught and wrapped
     *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
     *
     **/

    public void error(SAXParseException exception) throws SAXException
    {
        throw exception;
    }

    /**
     *  Throws the exception, which is caught and wrapped
     *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
     *
     **/

    public void fatalError(SAXParseException exception) throws SAXException
    {
        throw exception;
    }

    /**
     *  Checks for a previously registered public ID and returns the corresponding
     *  input source.
     *
     **/

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
    {
        String entityPath = null;

        if (LOG.isDebugEnabled())
            LOG.debug("Attempting to resolve entity; publicId = " + publicId + " systemId = " + systemId);

        if (_entities != null)
            entityPath = (String) _entities.get(publicId);

        if (entityPath == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Entity not found, using " + systemId);

            return null;
        }

        InputStream stream = getClass().getResourceAsStream(entityPath);

        InputSource result = new InputSource(stream);

        if (result != null && LOG.isDebugEnabled())
            LOG.debug("Resolved " + publicId + " as " + result + " (for " + entityPath + ")");

        return result;
    }

    /**
     *  Returns true if the node is an element with the specified
     *  name.
     *
     **/

    protected boolean isElement(Node node, String elementName) throws DocumentParseException
    {
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return false;

        // Cast it to Element

        Element element = (Element) node;

        // Note:  Using Xerces 1.0.3 and deferred DOM loading
        // (which is explicitly turned off), this sometimes
        // throws a NullPointerException.

        return element.getTagName().equals(elementName);

    }

    /**
     *  Returns the value of an {@link Element} node.  That is, all the {@link TextArea}
     *  nodes appended together.  Invokes trim() to remove leading and trailing spaces.
     *
     **/

    protected String getValue(Node node)
    {
        String result;
        Node child;
        Text text;
        StringBuffer buffer;

        buffer = new StringBuffer();

        for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            text = (Text) child;

            buffer.append(text.getData());
        }

        result = buffer.toString().trim();

        return result;
    }

    /**
     *  Returns the value of an {@link Element} node (via {@link #getValue(Node)}),
     *  but then validates that the result is a good identifier (starts with a
     *  letter, contains letters, numbers, dashes, underscore).
     *
     **/

    protected String getId(Node node) throws DocumentParseException
    {
        String result = getValue(node);
        char[] array = result.toCharArray();
        char ch;
        boolean fail = false;

        for (int i = 0; i < array.length; i++)
        {
            ch = array[i];

            if (i == 0)
                fail = !Character.isLetter(ch);
            else
            {
                fail = !(Character.isLetter(ch) || Character.isDigit(ch) || ch == '-' || ch == '_');
            }

            if (fail)
                throw new DocumentParseException(
                    Tapestry.getString(
                        "AbstractDocumentParser.invalid-identifier",
                        result,
                        getNodePath(node.getParentNode())),
                    _resourceLocation,
                    null);
        }

        return result;
    }

    /**
     *  Returns a 'path' to the given node, which is a list of enclosing
     *  element names seperated by periods.  The root element name is first,
     *  and the node's element is last.  This is used when reporting some
     *  parse errors.
     *
     **/

    protected String getNodePath(Node node)
    {
        int count = 0;
        int length = 0;

        String[] path = new String[10];

        while (node != null)
        {
            // Dynamically expand the list before it overflows.

            if (count == path.length)
            {
                String newPath[] = new String[count * 2];
                System.arraycopy(path, 0, newPath, 0, count);

                path = newPath;
            }

            String nodeName = node.getNodeName();

            path[count++] = nodeName;
            node = node.getParentNode();

            length += nodeName.length() + 1;

        }

        StringBuffer buffer = new StringBuffer(length);
        boolean addDot = false;

        for (int i = count - 1; i >= 0; i--)
        {

            if (addDot)
                buffer.append('.');

            buffer.append(path[i]);

            addDot = true;
        }

        return buffer.toString();
    }

    /**
     *  Constructs a new {@link DocumentBuilder} to be used for parsing.
     *  The builder is used and reused, at least until there is an error
     *  parsing a document (at which point, it is discarded).
     *
     *  <p>This implementation obtains a builder with the following
     *  characteristics:
     *  <ul>
     *  <li>validating (if {@link #getRequireValidatingParser()} returns true}
     *  <li>ignoringElementContentWhitespace
     *  <li>ignoringComments
     *  <li>coalescing
     *  </ul>
     *
     *  <p>These characteristics are appropriate to parsing things such
     *  as Tapestry specifications; subclasses with unusual demands
     *  may need to override this method.
     *
     *  <p>The builder uses this {@link AbstractDocumentParser}
     *  as the entity resolver and error handler.
     *
     **/

    protected DocumentBuilder constructBuilder() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setValidating(getRequireValidatingParser());
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);

        DocumentBuilder result = factory.newDocumentBuilder();

        result.setErrorHandler(this);
        result.setEntityResolver(this);

        if (LOG.isDebugEnabled())
            LOG.debug("Constructed new builder " + result);

        return result;
    }

    /**
     *  Used by {@link #constructBuilder()} to determine if the a validating
     *  {@link DocumentBuilder} is required.  This implementation returns true,
     *  subclasses that don't require a validating builder (such as documents
     *  without a known DTD), may override to return false.
     *
     *  @since 1.0.1
     **/

    protected boolean getRequireValidatingParser()
    {
        return true;
    }

    /**
     *  Returns the value of the named attribute of the node.  Returns null
     *  if the node doesn't contain an attribute with the given name.
     *
     *  @since 1.0.1
     * 
     **/

    protected String getAttribute(Node node, String attributeName)
    {
        NamedNodeMap map = node.getAttributes();

        if (map == null)
            return null;

        Node attributeNode = map.getNamedItem(attributeName);

        if (attributeNode == null)
            return null;

        return attributeNode.getNodeValue();
    }

    /**
     *  Validates that the input value matches against the specified
     *  Perl5 pattern.  If valid, the method simply returns.
     *  If not a match, then an error message is generated (using the
     *  errorKey and the input value) and a
     *  {@link DocumentParseException} is thrown.
     * 
     *  @since 2.2
     * 
     **/

    protected void validate(String value, String pattern, String errorKey) throws DocumentParseException
    {
        if (_compiledPatterns == null)
            _compiledPatterns = new HashMap();

        Pattern compiled = (Pattern) _compiledPatterns.get(pattern);

        if (compiled == null)
        {
            compiled = compilePattern(pattern);

            _compiledPatterns.put(pattern, compiled);
        }

        if (_matcher == null)
            _matcher = new Perl5Matcher();

        if (_matcher.matches(value, compiled))
            return;

        throw new InvalidStringException(Tapestry.getString(errorKey, value), value, getResourceLocation());
    }

    /** 
     * 
     *  Returns a pattern compiled for single line matching
     * 
     *  @since 2.2 
     * 
     **/

    protected Pattern compilePattern(String pattern)
    {
        if (_patternCompiler == null)
            _patternCompiler = new Perl5Compiler();

        try
        {
            return _patternCompiler.compile(pattern, Perl5Compiler.SINGLELINE_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

}