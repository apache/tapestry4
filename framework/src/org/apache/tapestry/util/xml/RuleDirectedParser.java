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

package org.apache.tapestry.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.RegexpMatcher;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A simplified version of {@link org.apache.commons.digester.Digester} without
 * as many bells and whistles ... but with some key features needed when parsing
 * a document (rather than a configuration file):
 * <ul>
 *   <li>Notifications for each bit of text</ul>
 *   <li>Tracking of exact location within the document.</li>
 * </ul>
 * 
 * <p>
 * Like Digester, there's an object stack and a rule stack.  The rules are much
 * simpler (more coding), in that there's a one-to-one relationship between
 * an element and a rule.
 *
 * <p>
 *  Based on SAX2.
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

public class RuleDirectedParser extends DefaultHandler
{
    private static final Log LOG = LogFactory.getLog(RuleDirectedParser.class);

    private IResourceLocation _documentLocation;
    private List _ruleStack = new ArrayList();
    private List _objectStack = new ArrayList();
    private Object _documentObject;

    private int _line = -1;
    private int _column = -1;
    private ILocation _location;

    private static SAXParserFactory _parserFactory;
    private SAXParser _parser;

    private RegexpMatcher _matcher;

    private String _uri;
    private String _localName;
    private String _qName;

    /**
     *  Map of {@link IRule} keyed on the local name
     *  of the element.
     */
    private Map _ruleMap = new HashMap();

    /**
     *  Map of {@link
     */

    private Map _entities = new HashMap();

    public Object parse(IResourceLocation documentLocation)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Parsing: " + documentLocation);

        try
        {
            _documentLocation = documentLocation;

            URL url = documentLocation.getResourceURL();

            if (url == null)
                throw new DocumentParseException(
                    Tapestry.getString("RuleDrivenParser.resource-missing", documentLocation),
                    documentLocation,
                    null,
                    null);

            return parse(url);
        }
        finally
        {
            _documentLocation = null;
            _ruleStack.clear();
            _objectStack.clear();
            _documentObject = null;

            _uri = null;
            _localName = null;
            _qName = null;

            _line = -1;
            _column = -1;
            _location = null;
        }
    }

    protected Object parse(URL url)
    {
        if (_parser == null)
            _parser = constructParser();

        InputStream stream = null;

        try
        {

            stream = url.openStream();
        }
        catch (IOException ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("RuleDrivenParser.unable-to-open-resource", url),
                _documentLocation,
                null,
                ex);
        }

        InputSource source = new InputSource(stream);

        try
        {
            _parser.parse(source, this);

            stream.close();
        }
        catch (Exception ex)
        {
            throw new DocumentParseException(
                Tapestry.getString("RuleDrivenParser.parse-error", url, ex.getMessage()),
                _documentLocation,
                getLocation(),
                ex);
        }

        if (LOG.isDebugEnabled())
            LOG.debug("Document parsed as: " + _documentObject);

        return _documentObject;
    }

    /**
     * Returns an {@link ILocation} representing the
     * current position within the document (depending
     * on the parser, this may be accurate to
     * column number level).
     */

    public ILocation getLocation()
    {
        if (_location == null)
            _location = new Location(_documentLocation, _line, _column);

        return _location;
    }

    /**
     * Pushes an object onto the object stack.  The first object
     * pushed is the "document object", the root object returned
     * by the parse.
     */
    public void push(Object object)
    {
        if (_documentObject == null)
            _documentObject = object;

        push(_objectStack, object, "object stack");
    }

    /**
     * Returns the top object on the object stack.
     */
    public Object peek()
    {
        return peek(_objectStack, 0);
    }

    /**
     * Returns an object within the object stack, at depth.
     * Depth 0 is the top object, depth 1 is the next-to-top object,
     * etc.
     */

    public Object peek(int depth)
    {
        return peek(_objectStack, depth);
    }

    /**
     * Removes and returns the top object on the object stack.
     */
    public Object pop()
    {
        return pop(_objectStack, "object stack");
    }

    private Object pop(List list, String name)
    {
        Object result = list.remove(list.size() - 1);

        if (LOG.isDebugEnabled())
            LOG.debug("Popped " + result + " off " + name + " (at " + getLocation() + ")");

        return result;
    }

    private Object peek(List list, int depth)
    {
        return list.get(list.size() - 1 - depth);
    }

    private void push(List list, Object object, String name)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Pushing " + object + " onto " + name + " (at " + getLocation() + ")");

        list.add(object);
    }

    /**
     * Pushes a new rule onto the rule stack.
     */

    protected void pushRule(IRule rule)
    {
        push(_ruleStack, rule, "rule stack");
    }

    /**
     * Returns the top rule on the stack.
     */

    protected IRule peekRule()
    {
        return (IRule) peek(_ruleStack, 0);
    }

    protected IRule popRule()
    {
        return (IRule) pop(_ruleStack, "rule stack");
    }

    public void addRule(String localElementName, IRule rule)
    {
        _ruleMap.put(localElementName, rule);
    }

    /**
     *  Registers
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

    public void registerEntity(String publicId, String entityPath)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Registering " + publicId + " as " + entityPath);

        if (_entities == null)
            _entities = new HashMap();

        _entities.put(publicId, entityPath);
    }

    protected IRule selectRule(String localName, Attributes attributes)
    {
        IRule rule = (IRule) _ruleMap.get(localName);

        if (rule == null)
            throw new DocumentParseException(
                Tapestry.getString("RuleDrivenParser.no-rule-for-element", localName),
                _documentLocation,
                getLocation(),
                null);

        return rule;
    }

    /**
     * Uses the {@link Locator} to track the position
     * in the document as a {@link ILocation}.
     * 
     * @see #getLocation()
     */
    public void setDocumentLocator(Locator locator)
    {
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();

        if (_line == line && _column == column)
            return;

        _line = line;
        _column = column;
        _location = null;
    }

    /**
     * Invokes {@link IRule#characters(RuleDirectedParser, char[], int, int)} on the
     * top rule.
     */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        peekRule().characters(this, ch, start, length);
    }

    /**
     * Pops the top rule off the stack and
     * invokes {@link IRule#endElementt(RuleDirectedParser, String)}.
     */
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
		_uri = uri;
		_localName = localName;
		_qName = qName;
		
        popRule().endElement(this);
    }

    /**
     * Invokes {@link IRule#ignorableWhitespace(RuleDirectedParser, char[], int, int)}
     * on the top rule.
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
    {
        peekRule().ignorableWhitespace(this, ch, start, length);
    }

    /**
     * Invokes {@link #selectRule(String, Attributes)} to choose a new rule,
     * which is pushed onto the rule stack, then invokes
     * {@link IRule#startElement(RuleDirectedParser, String, Attributes)}.
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
    	_uri = uri;
    	_localName = localName;
    	_qName = qName;
    	
        String name = extractName(uri, localName, qName);

        IRule newRule = selectRule(name, attributes);

        pushRule(newRule);

        newRule.startElement(this, attributes);
    }

    private String extractName(String uri, String localName, String qName)
    {
        return Tapestry.isNull(localName) ? qName : localName;
    }

    /**
     * Uses {@link javax.xml.parsers.SAXParserFactory} to create a instance
     * of a validation SAX2 parser.
     */
    protected synchronized SAXParser constructParser()
    {
        if (_parserFactory == null)
        {
            _parserFactory = SAXParserFactory.newInstance();
            configureParserFactory(_parserFactory);
        }

        try
        {
            return _parserFactory.newSAXParser();
        }
        catch (SAXException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        catch (ParserConfigurationException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }

    }

    /**
     * Configures a {@link SAXParserFactory} before
     * {@link SAXParserFactory#newSAXParser()} is invoked.
     * The default implementation sets validating to true
     * and namespaceAware to false,
     */

    protected void configureParserFactory(SAXParserFactory factory)
    {
        factory.setValidating(true);
        factory.setNamespaceAware(false);
    }

    /**
     *  Throws the exception.
     */
    public void error(SAXParseException ex) throws SAXException
    {
        fatalError(ex);
    }

    /**
     *  Throws the exception.
     */
    public void fatalError(SAXParseException ex) throws SAXException
    {
        // Sometimes, a bad parse "corrupts" a parser so that it doesn't
        // work properly for future parses (of valid documents),
        // so discard it here.

        _parser = null;

        throw ex;
    }

    /**
     *  Throws the exception.
     */
    public void warning(SAXParseException ex) throws SAXException
    {
        fatalError(ex);
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException
    {
        String entityPath = null;

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Attempting to resolve entity; publicId = " + publicId + " systemId = " + systemId);

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
     *  Validates that the input value matches against the specified
     *  Perl5 pattern.  If valid, the method simply returns.
     *  If not a match, then an error message is generated (using the
     *  errorKey and the input value) and a
     *  {@link InvalidStringException} is thrown.
     * 
     **/

    public void validate(String value, String pattern, String errorKey)
        throws DocumentParseException
    {
        if (_matcher == null)
            _matcher = new RegexpMatcher();

        if (_matcher.matches(pattern, value))
            return;

        throw new InvalidStringException(Tapestry.getString(errorKey, value), value, getLocation());
    }

    public IResourceLocation getDocumentLocation()
    {
        return _documentLocation;
    }

    /**
     * Returns the localName for the current element.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public String getLocalName()
    {
        return _localName;
    }

    /**
     * Returns the qualified name for the current element.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public String getQName()
    {
        return _qName;
    }

    /**
     * Returns the URI for the current element.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes) 
     */
    public String getUri()
    {
        return _uri;
    }

}
