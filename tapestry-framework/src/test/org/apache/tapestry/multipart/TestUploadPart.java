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
package org.apache.tapestry.multipart;

import org.apache.commons.fileupload.FileItem;
import org.apache.tapestry.TestBase;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 *  Tests functionality of {@link UploadPart}.
 */
@Test
public class TestUploadPart extends TestBase {

    public void test_Windows_File_Name()
    {
        FileItem item = newMock(FileItem.class);
        
        expect(item.getName()).andReturn("C:\\\\documents\\things\\image.png");
        
        replay();
        
        UploadPart part = new UploadPart(item);
        assertEquals(part.getFileName(), "image.png");
        
        verify();
    }
    
    public void test_Windows_File_Path()
    {
        FileItem item = newMock(FileItem.class);
        
        expect(item.getName()).andReturn("C:\\\\documents\\things\\image.png");
        
        replay();
        
        UploadPart part = new UploadPart(item);
        assertEquals(part.getFilePath(), "C:\\\\documents\\things\\image.png");
        
        verify();
    }
}
