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

package org.apache.tapestry.event;

import java.util.Collection;
import java.util.EventObject;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.DescriptionReceiver;

/**
 * Event object used by {@link org.apache.tapestry.event.ReportStatusListener}; the event
 * implements {@link org.apache.tapestry.describe.DescriptionReceiver}; classes (typically,
 * HiveMind service implementations) that implement the listener interface will "describe"
 * themselves to the event.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ReportStatusEvent extends EventObject implements DescriptionReceiver
{
    private final DescriptionReceiver _receiver;

    public ReportStatusEvent(Object source, DescriptionReceiver receiver)
    {
        super(source);

        Defense.notNull(receiver, "receiver");

        _receiver = receiver;
    }

    public void array(String key, Object[] values)
    {
        _receiver.array(key, values);
    }

    public void collection(String key, Collection values)
    {
        _receiver.collection(key, values);
    }

    public void describeAlternate(Object alternate)
    {
        _receiver.describeAlternate(alternate);
    }

    public void property(String key, boolean value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, byte value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, char value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, double value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, float value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, int value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, long value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, Object value)
    {
        _receiver.property(key, value);
    }

    public void property(String key, short value)
    {
        _receiver.property(key, value);
    }

    public void section(String section)
    {
        _receiver.section(section);
    }

    public void title(String title)
    {
        _receiver.title(title);
    }
}
