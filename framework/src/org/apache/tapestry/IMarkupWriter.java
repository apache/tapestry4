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

package org.apache.tapestry;

/**
 *  Defines an object that can write markup (XML, HTML, XHTML) style output.
 *  A <code>IMarkupWriter</code> handles translation from unicode to
 *  the markup language (escaping characters such as '&lt;' and '&gt;' to
 *  their entity equivalents, '&amp;lt;' and '&amp;gt;') as well as assisting
 *  with nested elements, closing tags, etc.
 *
 *  @author Howard Ship, David Solis
 *  @version $Id$
 **/

public interface IMarkupWriter
{
    /**
     * Writes an integer attribute into the currently open tag.
     *
     * @throws IllegalStateException if there is no open tag.
     *
     **/

    public void attribute(String name, int value);

    /**
     * Writes a boolean attribute into the currently open tag.
     *
     * @throws IllegalStateException if there is no open tag.
     *
     * @since 3.0
     **/

    public void attribute(String name, boolean value);

    /**
     * Writes an attribute into the most recently opened tag. This must be called after
     * {@link #begin(String)}
     * and before any other kind of writing (which closes the tag).
     *
     * <p>The value may be null.
     *
     * @throws IllegalStateException if there is no open tag.
     **/

    public void attribute(String name, String value);

    /**
     * Similar to {@link #attribute(String, String)} but no escaping of invalid elements
     * is done for the value.
     * 
     * @throws IllegalStateException if there is no open tag.
     *
     * @since 3.0
     **/

    public void attributeRaw(String name, String value);

    /**
     * Closes any existing tag then starts a new element. The new element is pushed
     * onto the active element stack.
     **/

    public void begin(String name);

    /**
     * Starts an element that will not later be matched with an <code>end()</code>
     * call. This is useful for elements that
     * do not need closing tags.
     *
     **/

    public void beginEmpty(String name);

    /**
     * Invokes checkError() on the <code>PrintWriter</code> used to
     *  format output.
     **/

    public boolean checkError();

    /**
     * Closes this <code>IMarkupWriter</code>. Close tags are
     * written for any active elements. The <code>PrintWriter</code>
     * is then sent <code>close()</code>.  A nested writer will commit
     * its buffer to its containing writer.
     *
     **/

    public void close();

    /**
     * Closes the most recently opened element by writing the '&gt;' that ends
     * it. Once this is invoked, the <code>attribute()</code> methods
     * may not be used until a new element is opened with {@link #begin(String)} or
     * or {@link #beginEmpty(String)}.
     **/

    public void closeTag();

    /**
     * Writes an XML/HTML comment. Any open tag is first closed. 
     * The method takes care of
     * providing the <code>&lt;!--</code> and <code>--&gt;</code>, and
     * provides a blank line after the close of the comment.
     *
     * <p><em>Most</em> characters are valid inside a comment, so no check
     * of the contents is made (much like {@link #printRaw(String)}.
     *
     **/

    public void comment(String value);

    /**
     * Ends the element most recently started by {@link
     * #begin(String)}.  The name of the tag is popped off of the
     * active element stack and used to form an HTML close tag.
     *
     **/

    public void end();

    /**
     * Ends the most recently started element with the given
     * name. This will also end any other intermediate
     * elements. This is very useful for easily ending a table or
     * even an entire page.
     *
     **/

    public void end(String name);

    /**
     * Forwards <code>flush()</code> to this 
     * <code>IMarkupWriter</code>'s <code>PrintWriter</code>.
     *
     **/

    public void flush();

    /**
     *  Returns a nested writer, one that accumulates its changes in a
     *  buffer.  When the nested  writer is closed, it writes its
     *  buffer into its containing <code>IMarkupWriter</code>.
     *
     **/

    public IMarkupWriter getNestedWriter();

    /**
     *
     * The primary <code>print()</code> method, used by most other
     * methods.
     *
     * <p>Prints the character array, first closing any open tag. Problematic characters
     * ('&lt;', '&gt;' and '&amp;') are converted to appropriate
     * entities.
     *
     * <p>Does <em>nothing</em> if <code>data</code> is null.
     *
     * <p>Closes any open tag.
     *
     **/

    public void print(char[] data, int offset, int length);

    /**
     * Prints a single character, or its equivalent entity.
     *
     * <p>Closes any open tag.
     *
     **/

    public void print(char value);

    /**
     * Prints an integer.
     *
     * <p>Closes any open tag.
     *
     **/

    public void print(int value);

    /**
     * Invokes {@link #print(char[], int, int)} to print the string.  Use
     * {@link #printRaw(String)} if the character data is known to be safe.
     *
     * <p>Does <em>nothing</em> if <code>value</code> is null.
     *
     * <p>Closes any open tag.
     *
     * @see #print(char[], int, int)
     *
     **/

    public void print(String value);

    /**
     * Closes the open tag (if any), then prints a line seperator to
     * the output stream.
     *
     **/

    public void println();

    /**
     * Prints a portion of an output buffer to the stream.
     * No escaping of invalid elements is done, which
     * makes this more effecient than <code>print()</code>. 
     * Does <em>nothing</em> if <code>buffer</code>
     * is null.
     *
     * <p>Closes any open tag.
     *
     **/

    public void printRaw(char[] buffer, int offset, int length);

    /**
     * Prints output to the stream. No escaping of invalid elements is done, which
     * makes this more effecient than <code>print()</code>.
     *
     * <p>Does <em>nothing</em> if <code>value</code>
     * is null.
     *
     * <p>Closes any open tag.
     *
     **/

    public void printRaw(String value);

    /**
     *  Returns the type of content generated by this response writer, as
     *  a MIME type.
     *
     **/

    public String getContentType();
}