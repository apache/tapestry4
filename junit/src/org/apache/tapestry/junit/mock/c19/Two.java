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

package org.apache.tapestry.junit.mock.c19;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

/**
 *  Test page for the {@link org.apache.tapestry.form.Upload}
 *  component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
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
