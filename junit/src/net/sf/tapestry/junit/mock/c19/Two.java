/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.junit.mock.c19;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IUploadFile;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.html.BasePage;

/**
 *  Test page for the {@link net.sf.tapestry.form.Upload}
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public abstract class Two extends BasePage
{
    public abstract IUploadFile getFile();
    public abstract void setFile(IUploadFile file);

    public boolean getUploadMatch()
    {
        IUploadFile file = getFile();
        String path = file.getFilePath();

        InputStream expected = null;
        InputStream actual = null;

        try
        {
            expected = new FileInputStream(path);
            actual = file.getStream();

            int i = 0;

            while (true)
            {
                int actualByte = actual.read();
                int expectedByte = expected.read();

                if (actualByte != expectedByte)
                {
                    System.err.println(
                        "Input deviation at index "
                            + i
                            + "; expected "
                            + expectedByte
                            + ", not "
                            + actualByte);

                    return false;
                }

                if (actualByte < 0)
                    break;

                i++;
            }
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
        finally
        {
            Tapestry.close(expected);
            Tapestry.close(actual);
        }

        return true;
    }
}
