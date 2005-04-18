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

package org.apache.tapestry.asset;

import java.io.IOException;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class AssetMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(AssetMessages.class,
            "AssetStrings");

    public static String missingAsset(String path, Resource resource)
    {
        return _formatter.format("missing-asset", path, resource);
    }

    public static String noSuchResource(String resourcePath)
    {
        return _formatter.format("no-such-resource", resourcePath);
    }

    public static String unableToReadResource(String resourcePath, IOException cause)
    {
        return _formatter.format("unable-to-read-resource", resourcePath, cause);
    }

    public static String md5Mismatch(String path)
    {
        return _formatter.format("md5-mismatch", path);
    }

    public static String exceptionReportTitle(String path)
    {
        return _formatter.format("exception-report-title", path);
    }
}