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

package org.apache.tapestry.asset;

import java.io.IOException;

import org.apache.hivemind.Messages;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class AssetMessages
{

    private final static Messages MESSAGES = new MessageFormatter(AssetMessages.class);

    /** @since 4.1 */
    private AssetMessages()
    {
    }

    static String missingAsset(String path, Resource resource)
    {
        return MESSAGES.format("missing-asset", path, resource);
    }

    static String noSuchResource(String resourcePath)
    {
        return MESSAGES.format("no-such-resource", resourcePath);
    }

    static String unableToReadResource(String resourcePath, IOException cause)
    {
        return MESSAGES.format("unable-to-read-resource", resourcePath, cause);
    }

    static String md5Mismatch(String path)
    {
        return MESSAGES.format("md5-mismatch", path);
    }

    static String exceptionReportTitle(String path)
    {
        return MESSAGES.format("exception-report-title", path);
    }

    static String missingClasspathResource(String path)
    {
        return MESSAGES.format("missing-classpath-resource", path);
    }

    static String missingContextResource(String path)
    {
        return MESSAGES.format("missing-context-resource", path);
    }
}
