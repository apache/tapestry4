// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * Localized messages for the org.apache.tapestry.parse package.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
class ParseMessages
{
    protected static MessageFormatter _formatter = new MessageFormatter(ParseMessages.class,
            "ParseStrings");

    static String commentNotEnded(int line)
    {
        return _formatter.format("comment-not-ended", new Integer(line));
    }

    static String unclosedUnknownTag(int line)
    {
        return _formatter.format("unclosed-unknown-tag", new Integer(line));
    }

    static String unclosedTag(String tagName, int line)
    {
        return _formatter.format("unclosed-tag", tagName, new Integer(line));
    }

    static String missingAttributeValue(String tagName, int line, String attributeName)
    {
        return _formatter.format(
                "missing-attribute-value",
                tagName,
                new Integer(line),
                attributeName);
    }

    static String componentMayNotBeIgnored(String tagName, int line)
    {
        return _formatter.format("component-may-not-be-ignored", tagName, new Integer(line));
    }

    static String componentIdInvalid(String tagName, int line, String jwcid)
    {
        return _formatter.format("component-id-invalid", tagName, new Integer(line), jwcid);
    }

    static String unknownComponentId(String tagName, int line, String jwcid)
    {
        return _formatter.format("unknown-component-id", tagName, new Integer(line), jwcid);
    }

    static String nestedIgnore(String tagName, int line)
    {
        return _formatter.format("nested-ignore", tagName, new Integer(line));
    }

    static String contentBlockMayNotBeIgnored(String tagName, int line)
    {
        return _formatter.format("content-block-may-not-be-ignored", tagName, new Integer(line));
    }

    static String contentBlockMayNotBeEmpty(String tagName, int line)
    {
        return _formatter.format("content-block-may-not-be-empty", tagName, new Integer(line));
    }

    static String incompleteCloseTag(int line)
    {
        return _formatter.format("incomplete-close-tag", new Integer(line));
    }

    static String improperlyNestedCloseTag(String tagName, int closeLine, String startTagName,
            int startLine)
    {
        return _formatter.format("improperly-nested-close-tag", new Object[]
        { tagName, new Integer(closeLine), startTagName, new Integer(startLine) });
    }

    static String unmatchedCloseTag(String tagName, int line)
    {
        return _formatter.format("unmatched-close-tag", tagName, new Integer(line));
    }

    static String failConvertBoolean(String value)
    {
        return _formatter.format("fail-convert-boolean", value);
    }

    static String failConvertDouble(String value)
    {
        return _formatter.format("fail-convert-double", value);
    }

    static String failConvertInt(String value)
    {
        return _formatter.format("fail-convert-int", value);
    }

    static String failConvertLong(String value)
    {
        return _formatter.format("fail-convert-long", value);
    }

    static String unableToCopy(String id)
    {
        return _formatter.format("unable-to-copy", id);
    }

    static String bothTypeAndCopyOf(String id)
    {
        return _formatter.format("both-type-and-copy-of", id);
    }

    static String missingTypeOrCopyOf(String id)
    {
        return _formatter.format("missing-type-or-copy-of", id);
    }

    static String frameworkLibraryIdIsReserved(String id)
    {
        return _formatter.format("framework-library-id-is-reserved", id);
    }

    static String incorrectDocumentType(String expected, String actual)
    {
        return _formatter.format("incorrect-document-type", expected, actual);
    }

    static String noAttributeAndBody(String attributeName, String elementName)
    {
        return _formatter.format("no-attribute-and-body", attributeName, elementName);
    }

    static String requiredExtendedAttribute(String elementName, String attributeName)
    {
        return _formatter.format("required-extended-attribute", elementName, attributeName);
    }

    static String invalidAttribute(String key, String value)
    {
        return _formatter.format(key, value);
    }

    static String missingResource(Resource resource)
    {
        return _formatter.format("missing-resource", resource);
    }

    static String errorReadingResource(Resource resource, Throwable cause)
    {
        return _formatter.format("error-reading-resource", resource, cause);
    }

    static String unknownPublicId(Resource resource, String publicId)
    {
        return _formatter.format("unknown-public-id", resource, publicId);
    }

    static String serviceElementNotSupported()
    {
        return _formatter.getMessage("service-element-not-supported");
    }

    static String rangeError(TemplateToken token, int length)
    {
        return _formatter.format("range-error", token, new Integer(length));
    }
}