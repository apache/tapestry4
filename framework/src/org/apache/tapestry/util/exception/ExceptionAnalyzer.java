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

package org.apache.tapestry.util.exception;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *  Analyzes an exception, creating one or more 
 *  {@link ExceptionDescription}s
 *  from it.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionAnalyzer
{
    private List exceptionDescriptions;
    private List propertyDescriptions;
    private CharArrayWriter writer;

    private static final int LIST_SIZE = 3;

    private boolean exhaustive = false;

    /**
     *  If true, then stack trace is extracted for each exception.  If false,
     *  the default, then stack trace is extracted for only the deepest exception.
     *
     **/

    public boolean isExhaustive()
    {
        return exhaustive;
    }

    public void setExhaustive(boolean value)
    {
        exhaustive = value;
    }

    /**
     *  Analyzes the exceptions.  This builds an {@link ExceptionDescription} for the
     *  exception.  It also looks for a non-null {@link Throwable}
     *  property.  If one exists, then a second {@link ExceptionDescription} 
     *  is created.  This continues until no more nested exceptions can be found.
     *
     *  <p>The description includes a set of name/value properties 
     *  (as {@link ExceptionProperty}) object.  This list contains all
     *  non-null properties that are not, themselves, {@link Throwable}.
     *
     *  <p>The name is the display name (not the logical name) of the property.  The value
     *  is the <code>toString()</code> value of the property.
     *
     *  Only properties defined in subclasses of {@link Throwable} are included.
     *
     *  <p>A future enhancement will be to alphabetically sort the properties by name.
     **/

    public ExceptionDescription[] analyze(Throwable exception)
    {
        ExceptionDescription[] result;

        if (writer == null)
            writer = new CharArrayWriter();

        if (propertyDescriptions == null)
            propertyDescriptions = new ArrayList(LIST_SIZE);

        if (exceptionDescriptions == null)
            exceptionDescriptions = new ArrayList(LIST_SIZE);

        while (exception != null)
        {
            exception = buildDescription(exception);
        }

        result = new ExceptionDescription[exceptionDescriptions.size()];
        result = (ExceptionDescription[]) exceptionDescriptions.toArray(result);

        exceptionDescriptions.clear();
        propertyDescriptions.clear();

        writer.reset();

        // We never actually close() the writer which is bad ... I'm expecting that
        // the finalize() method will close them, or that they don't need to
        // close.

        return result;
    }

    protected Throwable buildDescription(Throwable exception)
    {
        BeanInfo info;
        Class exceptionClass;
        ExceptionProperty property;
        PropertyDescriptor[] descriptors;
        PropertyDescriptor descriptor;
        Throwable next = null;
        int i;
        Object value;
        Method method;
        ExceptionProperty[] properties;
        ExceptionDescription description;
        String stringValue;
        String message;
        String[] stackTrace = null;

        propertyDescriptions.clear();

        message = exception.getMessage();
        exceptionClass = exception.getClass();

        // Get properties, ignoring those in Throwable and higher
        // (including the 'message' property).

        try
        {
            info = Introspector.getBeanInfo(exceptionClass, Throwable.class);
        }
        catch (IntrospectionException e)
        {
            return null;
        }

        descriptors = info.getPropertyDescriptors();

        for (i = 0; i < descriptors.length; i++)
        {
            descriptor = descriptors[i];

            method = descriptor.getReadMethod();
            if (method == null)
                continue;

            try
            {
                value = method.invoke(exception, null);
            }
            catch (Exception e)
            {
                continue;
            }

            if (value == null)
                continue;

            // Some annoying exceptions duplicate the message property
            // (I'm talking to YOU SAXParseException), so just edit that out.

            if (message != null && message.equals(value))
                continue;

            // Skip Throwables ... but the first non-null
            // found is the next exception.  We kind of count
            // on there being no more than one Throwable
            // property per Exception.

            if (value instanceof Throwable)
            {
                if (next == null)
                    next = (Throwable) value;

                continue;
            }

            stringValue = value.toString().trim();

            if (stringValue.length() == 0)
                continue;

            property = new ExceptionProperty(descriptor.getDisplayName(), value.toString());

            propertyDescriptions.add(property);
        }

        // If exhaustive, or in the deepest exception (where there's no next)
        // the extract the stack trace.

        if (next == null || exhaustive)
            stackTrace = getStackTrace(exception);

        // Would be nice to sort the properties here.

        properties = new ExceptionProperty[propertyDescriptions.size()];

        ExceptionProperty[] propArray =
            (ExceptionProperty[]) propertyDescriptions.toArray(properties);

        description =
            new ExceptionDescription(
                exceptionClass.getName(),
                message,
                propArray,
                stackTrace);

        exceptionDescriptions.add(description);

        return next;
    }

    /**
     *  Gets the stack trace for the exception, and converts it into an array of strings.
     *
     *  <p>This involves parsing the 
     *   string generated indirectly from
     *  <code>Throwable.printStackTrace(PrintWriter)</code>.  This method can get confused
     *  if the message (presumably, the first line emitted by printStackTrace())
     *  spans multiple lines.
     *
     *  <p>Different JVMs format the exception in different ways.
     *
     *  <p>A possible expansion would be more flexibility in defining the pattern
     *  used.  Hopefully all 'mainstream' JVMs are close enough for this to continue
     *  working.
     *
     **/

    protected String[] getStackTrace(Throwable exception)
    {
        writer.reset();

        PrintWriter printWriter = new PrintWriter(writer);

        exception.printStackTrace(printWriter);

        printWriter.close();

        String fullTrace = writer.toString();

        writer.reset();

        // OK, the trick is to convert the full trace into an array of stack frames.

        StringReader stringReader = new StringReader(fullTrace);
        LineNumberReader lineReader = new LineNumberReader(stringReader);
        int lineNumber = 0;
        List frames = new ArrayList();

        try
        {
            while (true)
            {
                String line = lineReader.readLine();

                if (line == null)
                    break;

                // Always ignore the first line.

                if (++lineNumber == 1)
                    continue;

                frames.add(stripFrame(line));
            }

            lineReader.close();
        }
        catch (IOException ex)
        {
            // Not likely to happen with this particular set
            // of readers.
        }

        String result[] = new String[frames.size()];

        return (String[]) frames.toArray(result);
    }

    private static final int SKIP_LEADING_WHITESPACE = 0;
    private static final int SKIP_T = 1;
    private static final int SKIP_OTHER_WHITESPACE = 2;

    /**
     *  Sun's JVM prefixes each line in the stack trace
     *  with "<tab>at ", other JVMs don't.  This method
     *  looks for and strips such stuff.
     *
     **/

    private String stripFrame(String frame)
    {
        char array[] = frame.toCharArray();

        int i = 0;
        int state = SKIP_LEADING_WHITESPACE;
        boolean more = true;

        while (more)
        {
            // Ran out of characters to skip?  Return the empty string.

            if (i == array.length)
                return "";

            char ch = array[i];

            switch (state)
            {
                // Ignore whitespace at the start of the line.

                case SKIP_LEADING_WHITESPACE :

                    if (Character.isWhitespace(ch))
                    {
                        i++;
                        continue;
                    }

                    if (ch == 'a')
                    {
                        state = SKIP_T;
                        i++;
                        continue;
                    }

                    // Found non-whitespace, not 'a'
                    more = false;
                    break;

                    // Skip over the 't' after an 'a'

                case SKIP_T :

                    if (ch == 't')
                    {
                        state = SKIP_OTHER_WHITESPACE;
                        i++;
                        continue;
                    }

                    // Back out the skipped-over 'a'

                    i--;
                    more = false;
                    break;

                    // Skip whitespace between 'at' and the name of the class

                case SKIP_OTHER_WHITESPACE :

                    if (Character.isWhitespace(ch))
                    {
                        i++;
                        continue;
                    }

                    // Not whitespace
                    more = false;
                    break;
            }

        }

        // Found nothing to strip out.

        if (i == 0)
            return frame;

        return frame.substring(i);
    }

    /**
     *  Produces a text based exception report to the provided stream.
     *
     **/

    public void reportException(Throwable exception, PrintStream stream)
    {
        int i;
        int j;
        ExceptionDescription[] descriptions;
        ExceptionProperty[] properties;
        String[] stackTrace;
        String message;

        descriptions = analyze(exception);

        for (i = 0; i < descriptions.length; i++)
        {
            message = descriptions[i].getMessage();

            if (message == null)
                stream.println(descriptions[i].getExceptionClassName());
            else
                stream.println(
                    descriptions[i].getExceptionClassName() + ": " + descriptions[i].getMessage());

            properties = descriptions[i].getProperties();

            for (j = 0; j < properties.length; j++)
                stream.println(
                    "   " + properties[j].getName() + ": " + properties[j].getValue());

            // Just show the stack trace on the deepest exception.

            if (i + 1 == descriptions.length)
            {
                stackTrace = descriptions[i].getStackTrace();

                for (j = 0; j < stackTrace.length; j++)
                    stream.println(stackTrace[j]);
            }
            else
                stream.println();
        }
    }

}