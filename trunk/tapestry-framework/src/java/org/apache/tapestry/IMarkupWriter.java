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

package org.apache.tapestry;

/**
 * Defines an object that can write markup (XML, HTML, XHTML) style output. A
 * <code>IMarkupWriter</code> handles translation from unicode to the markup language (escaping
 * characters such as '&lt;' and '&gt;' to their entity equivalents, '&amp;lt;' and '&amp;gt;') as
 * well as assisting with nested elements, closing tags, etc.
 * 
 * @author Howard Ship, David Solis
 */

public interface IMarkupWriter
{
    /**
     * Writes an integer attribute into the currently open tag.
     * 
     * @throws IllegalStateException
     *             if there is no open tag.
     */

    void attribute(String name, int value);

    /**
     * Writes a boolean attribute into the currently open tag.
     * 
     * @throws IllegalStateException
     *             if there is no open tag.
     * @since 3.0
     */

    void attribute(String name, boolean value);

    /**
     * Writes an attribute into the most recently opened tag. This must be called after
     * {@link #begin(String)}and before any other kind of writing (which closes the tag).
     * <p>
     * The value may be null.
     * 
     * @throws IllegalStateException
     *             if there is no open tag.
     */

    void attribute(String name, String value);

    /**
     * Similar to {@link #attribute(String, String)}but no escaping of invalid elements is done for
     * the value.
     * 
     * @throws IllegalStateException
     *             if there is no open tag.
     * @since 3.0
     */

    void attributeRaw(String name, String value);

    /**
     * Closes any existing tag then starts a new element. The new element is pushed onto the active
     * element stack.
     */

    void begin(String name);

    /**
     * Starts an element that will not later be matched with an <code>end()</code> call. This is
     * useful for elements that do not need closing tags.
     */

    void beginEmpty(String name);

    /**
     * Invokes checkError() on the <code>PrintWriter</code> used to format output.
     */

    boolean checkError();

    /**
     * Closes this <code>IMarkupWriter</code>. Close tags are written for any active elements.
     * The <code>PrintWriter</code> is then sent <code>close()</code>. A nested writer will
     * commit its buffer to its containing writer.
     */

    void close();

    /**
     * Closes the most recently opened element by writing the '&gt;' that ends it. Once this is
     * invoked, the <code>attribute()</code> methods may not be used until a new element is opened
     * with {@link #begin(String)}or or {@link #beginEmpty(String)}.
     */

    void closeTag();

    /**
     * Writes an XML/HTML comment. Any open tag is first closed. The method takes care of providing
     * the <code>&lt;!--</code> and <code>--&gt;</code>, and provides a blank line after the
     * close of the comment.
     * <p>
     * <em>Most</em> characters are valid inside a comment, so no check of the contents is made
     * (much like {@link #printRaw(String)}.
     */

    void comment(String value);

    /**
     * Ends the element most recently started by {@link#begin(String)}. The name of the tag is
     * popped off of the active element stack and used to form an HTML close tag.
     */

    void end();

    /**
     * Ends the most recently started element with the given name. This will also end any other
     * intermediate elements. This is very useful for easily ending a table or even an entire page.
     */

    void end(String name);

    /**
     * Forwards <code>flush()</code> to this <code>IMarkupWriter</code>'s
     * <code>PrintWriter</code>.
     */

    void flush();

    /**
     * Returns a nested writer, one that accumulates its changes in a buffer. When the nested writer
     * is closed, it writes its buffer of markup into its containing <code>IMarkupWriter</code>
     * using {@link #printRaw(String)}.
     */

    NestedMarkupWriter getNestedWriter();

    /**
     * Version of {@link #print(char[], int, int, boolean)}&nbsp;that assumes filter is
     * <em>enabled</em>.
     */

    void print(char[] data, int offset, int length);

    /**
     * The primary <code>print()</code> method, used by most other methods.
     * <p>
     * Prints the character array, first closing any open tag. Problematic characters ('&lt;',
     * '&gt;' and '&amp;') are converted to appropriate entities.
     * <p>
     * Does <em>nothing</em> if <code>data</code> is null.
     * <p>
     * Closes any open tag.
     * 
     * @param data
     *            contains the characters to print, or null to not print anything
     * @param offset
     *            offset into the array to start printing from
     * @param length
     *            number of characters to print
     * @param raw
     *            if true, filtering is disabled
     * @since 4.0
     */

    void print(char[] data, int offset, int length, boolean raw);

    /**
     * Prints a single character, or its equivalent entity.
     * <p>
     * Closes any open tag.
     */

    void print(char value);

    /**
     * Prints an integer.
     * <p>
     * Closes any open tag.
     */

    void print(int value);

    /**
     * As with {@link #print(char[], int, int, boolean)}, but the data to print is defined by the
     * String. Assumes filtering is <em>enabled</em>.
     */

    void print(String value);

    /**
     * As with {@link #print(char[], int, int, boolean)}, but the data to print is defined by the
     * String.
     */

    void print(String value, boolean raw);

    /**
     * Closes the open tag (if any), then prints a line seperator to the output stream.
     */

    void println();

    /**
     * Version of {@link #print(char[], int, int, boolean)}that assumes filter is <em>enabled</em>.
     */

    void printRaw(char[] buffer, int offset, int length);

    /**
     * As with {@link #print(char[], int, int, boolean)}, but the data to print is defined by the
     * String. Assumes filtering is <em>disabled</em>.
     */

    void printRaw(String value);

    /**
     * Returns the type of content generated by this response writer, as a MIME type.
     */

    String getContentType();
}
