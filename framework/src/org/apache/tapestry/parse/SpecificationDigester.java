//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.RegexpMatcher;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *  Extension of {@link org.apache.commons.digester.Digester} with additional rules, hooks
 *  and methods needed when parsing Tapestry specifications.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class SpecificationDigester extends Digester
{
    private List _documentRules;
    private IResourceLocation _resourceLocation;
    private RegexpMatcher _matcher;

    private ILocation _lastLocation;
    private int _lastLine;
    private int _lastColumn;

    /**
     *  Notifications are sent with the very first element.
     * 
     **/

    private boolean _firstElement = true;

    public void addDocumentRule(IDocumentRule rule)
    {
        if (_documentRules == null)
            _documentRules = new ArrayList();

        rule.setDigester(this);

        _documentRules.add(rule);
    }

    public void addSetBooleanProperty(String pattern, String attributeName, String propertyName)
    {
        addRule(pattern, new SetBooleanPropertyRule(attributeName, propertyName));
    }

    public void addSetExtendedProperty(
        String pattern,
        String attributeName,
        String propertyName,
        boolean required)
    {
        addRule(pattern, new SetExtendedPropertyRule(attributeName, propertyName, required));
    }

    public void addValidate(
        String pattern,
        String attributeName,
        String valuePattern,
        String errorKey)
    {
        addRule(pattern, new ValidateRule(getMatcher(), attributeName, valuePattern, errorKey));
    }

    public void addSetConvertedProperty(
        String pattern,
        Map map,
        String attributeName,
        String propertyName)
    {
        addRule(pattern, new SetConvertedPropertyRule(map, attributeName, propertyName));
    }

    public void addConnectChild(String pattern, String methodName, String attributeName)
    {
        addRule(pattern, new ConnectChildRule(methodName, attributeName));
    }

    public void addInitializeProperty(String pattern, String propertyName, Object value)
    {
        addRule(pattern, new InitializePropertyRule(propertyName, value));
    }

    public void addSetLimitedProperties(String pattern, String attributeName, String propertyName)
    {
        addRule(pattern, new SetLimitedPropertiesRule(attributeName, propertyName));
    }

    public void addSetLimitedProperties(
        String pattern,
        String[] attributeNames,
        String[] propertyNames)
    {
        addRule(pattern, new SetLimitedPropertiesRule(attributeNames, propertyNames));
    }

    public void addBody(String pattern, String propertyName)
    {
        addRule(pattern, new BodyRule(propertyName));
    }

    /**
     *  Returns the {@link org.xml.sax.Locator} for the Digester.  This object
     *  is provided by the underlying SAX parser to identify where in the
     *  document the parse is currently located; this information can be
     *  used to build a {@link org.apache.tapestry.ILocation}.
     * 
     **/

    public Locator getLocator()
    {
        return locator;
    }

    public ILocation getLocationTag()
    {
        int line = -1;
        int column = -1;

        if (locator != null)
        {
            line = locator.getLineNumber();
            column = locator.getColumnNumber();
        }

        if (_lastLine != line || _lastColumn != column)
            _lastLocation = null;

        if (_lastLocation == null)
        {
            _lastLine = line;
            _lastColumn = column;
            _lastLocation = new Location(_resourceLocation, line, column);
        }

        return _lastLocation;
    }

    public IResourceLocation getResourceLocation()
    {
        return _resourceLocation;
    }

    public void setResourceLocation(IResourceLocation resourceLocation)
    {
        _resourceLocation = resourceLocation;

        _lastLocation = null;
        _lastLine = -1;
        _lastColumn = -1;
    }

    public RegexpMatcher getMatcher()
    {
        if (_matcher == null)
            _matcher = new RegexpMatcher();

        return _matcher;
    }

    public void endDocument() throws SAXException
    {
        int count = Tapestry.size(_documentRules);

        for (int i = 0; i < count; i++)
        {
            IDocumentRule rule = (IDocumentRule) _documentRules.get(i);

            try
            {

                rule.endDocument();
            }
            catch (Exception ex)
            {
                throw createSAXException(ex);
            }
        }

        super.endDocument();

        for (int i = 0; i < count; i++)
        {
            IDocumentRule rule = (IDocumentRule) _documentRules.get(i);

            try
            {
                rule.finish();
            }
            catch (Exception ex)
            {
                throw createSAXException(ex);
            }
        }
    }

    public void startDocument() throws SAXException
    {
        _firstElement = true;

        super.startDocument();
    }

    public void startElement(
        String namespaceURI,
        String localName,
        String qName,
        Attributes attributes)
        throws SAXException
    {
        if (_firstElement)
        {
            sendStartDocumentNotification(namespaceURI, localName, qName, attributes);

            _firstElement = false;
        }

        super.startElement(namespaceURI, localName, qName, attributes);
    }

    private void sendStartDocumentNotification(
        String namespaceURI,
        String localName,
        String qName,
        Attributes attributes)
        throws SAXException
    {
        int count = Tapestry.size(_documentRules);

        String name = Tapestry.isBlank(localName) ? qName : localName;

        for (int i = 0; i < count; i++)
        {
            IDocumentRule rule = (IDocumentRule) _documentRules.get(i);

            try
            {
                rule.startDocument(namespaceURI, name, attributes);
            }
            catch (Exception ex)
            {
                throw createSAXException(ex);
            }
        }

    }

    /**
     * Invokes {@link #fatalError(SAXParseException)}.
     */
    public void error(SAXParseException exception) throws SAXException
    {
        fatalError(exception);
    }

    /**
     * Simply re-throws the exception.  All exceptions when parsing
     * documents are fatal.
     */
    public void fatalError(SAXParseException exception) throws SAXException
    {
        throw exception;
    }

    /**
     * Invokes {@link #fatalError(SAXParseException)}.
     */
    public void warning(SAXParseException exception) throws SAXException
    {
        fatalError(exception);
    }

}
