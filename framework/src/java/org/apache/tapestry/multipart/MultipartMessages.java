// Copyright 2005 The Apache Software Foundation
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

import java.io.File;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class MultipartMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(
            MultipartMessages.class, "MultipartStrings");

    public static String unableToDecode(Throwable cause)
    {
        return _formatter.format("unable-to-decode", cause);
    }

    public static String unsupportedEncoding(String encoding, Throwable cause)
    {
        return _formatter.format("unsupported-encoding", encoding, cause);
    }

    public static String unableToOpenContentFile(UploadPart part, Throwable cause)
    {
        return _formatter.format("unable-to-open-content-file", part.getFilePath(), cause);
    }

    public static String writeFailure(File file, Throwable cause)
    {
        return _formatter.format("write-failure", file, cause);
    }

}