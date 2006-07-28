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

package org.apache.tapestry.vlib.services;

import java.rmi.RemoteException;

import org.apache.hivemind.lib.RemoteExceptionCoordinator;
import org.apache.hivemind.lib.RemoteExceptionEvent;
import org.apache.hivemind.lib.RemoteExceptionListener;
import org.apache.hivemind.service.ThreadCleanupListener;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.vlib.EntitySelectionModel;
import org.apache.tapestry.vlib.ejb.IOperations;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.ejb.Publisher;

/**
 * Implementation of {@link org.apache.tapestry.vlib.services.ModelSource}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ModelSourceImpl implements ModelSource, RemoteExceptionListener, ThreadCleanupListener
{
    private IPropertySelectionModel _publisherModel;

    private IPropertySelectionModel _personModel;

    private IPropertySelectionModel _optionalPersonModel;

    private IOperations _operations;

    private RemoteTemplate _remoteTemplate;

    private RemoteExceptionCoordinator _coordinator;

    public void initializeService()
    {
        _coordinator.addRemoteExceptionListener(this);
    }

    public void threadDidCleanup()
    {
        _coordinator.removeRemoteExceptionListener(this);
    }

    public synchronized IPropertySelectionModel getPublisherModel()
    {
        if (_publisherModel == null)
            _publisherModel = buildPublisherModel();

        return _publisherModel;
    }

    public synchronized IPropertySelectionModel getPersonModel()
    {
        if (_personModel == null)
            buildPersonModels();

        return _personModel;
    }

    public synchronized IPropertySelectionModel getOptionalPersonModel()
    {
        if (_optionalPersonModel == null)
            buildPersonModels();

        return _optionalPersonModel;
    }

    public synchronized void clear()
    {
        _publisherModel = null;
        _personModel = null;
        _optionalPersonModel = null;
    }

    public void remoteExceptionDidOccur(RemoteExceptionEvent event)
    {
        clear();
    }

    private IPropertySelectionModel buildPublisherModel()
    {
        RemoteCallback<Publisher[]> callback = new RemoteCallback()
        {
            public Publisher[] doRemote() throws RemoteException
            {
                return _operations.getPublishers();
            }
        };

        Publisher[] publishers = _remoteTemplate.execute(callback, "Error reading publishers.");

        int count = Tapestry.size(publishers);

        EntitySelectionModel model = new EntitySelectionModel();

        model.add(null, "");

        for (int i = 0; i < count; i++)
            model.add(publishers[i].getId(), publishers[i].getName());

        return model;
    }

    private void buildPersonModels()
    {
        Person[] persons = _remoteTemplate.getPersons();

        int count = Tapestry.size(persons);

        EntitySelectionModel requiredModel = new EntitySelectionModel();
        EntitySelectionModel optionalModel = new EntitySelectionModel();

        optionalModel.add(null, "");

        for (int i = 0; i < count; i++)
        {
            Integer id = persons[i].getId();
            String naturalName = persons[i].getNaturalName();

            requiredModel.add(id, naturalName);
            optionalModel.add(id, naturalName);
        }

        _personModel = requiredModel;
        _optionalPersonModel = optionalModel;
    }

    public void setOperations(IOperations operations)
    {
        _operations = operations;
    }

    public void setRemoteTemplate(RemoteTemplate remoteTemplate)
    {
        _remoteTemplate = remoteTemplate;
    }

    public void setCoordinator(RemoteExceptionCoordinator coordinator)
    {
        _coordinator = coordinator;
    }

}
