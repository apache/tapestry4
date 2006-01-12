// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class MultipartMessages
{

    private final static Messages MESSAGES = new MessageFormatter(MultipartMessages.class);

    /** @since 4.1 */
    private MultipartMessages()
    {
    }

    static String unableToDecode(Throwable cause)
    {
        return MESSAGES.format("unable-to-decode", cause);
    }

    static String unsupportedEncoding(String encoding, Throwable cause)
    {
        return MESSAGES.format("unsupported-encoding", encoding, cause);
    }

    static String unableToOpenContentFile(UploadPart part, Throwable cause)
    {
        return MESSAGES.format("unable-to-open-content-file", part.getFilePath(), cause);
    }

    static String writeFailure(File file, Throwable cause)
    {
        return MESSAGES.format("write-failure", file, cause);
    }

}
