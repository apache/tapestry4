// Copyright 2006 The Apache Software Foundation
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

package org.apache.tapestry.integration.app1.pages;

/**
 * Integration test for TAPESTRY-1775.
 */
public abstract class Tap1775 extends Home {

    public abstract String getMessage();
    public abstract void setMessage(String msg);

    public void addToMessage(String msg)
    {
        String original = getMessage();
        setMessage(original==null ? msg : original + " " + msg);
    }

    public void doSubmit()
    {
        addToMessage("SUBMIT");
    }

    public void doFirst()
    {
        addToMessage("FIRST");
    }

    public void doSecond()
    {
        addToMessage("SECOND");
    }

    public void doSuccess()
    {
        addToMessage("SUCCESS");
    }

    public void doCancel()
    {
        addToMessage("CANCEL");
    }

    public void doRefresh()
    {
        addToMessage("REFRESH");
    }
}