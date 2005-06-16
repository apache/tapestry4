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

function handle_invalid_field(field, message)
{
    focus(field);
    
    window.alert(message);
    
    return false;
}

function focus(field)
{
    field.focus();
    
    if (field.select)
    {
        field.select();
    }
}

function trim(field)
{
	field.value = field.value.replace(/^\s+/g, '').replace(/\s+$/g, '');
	
	return true;
}

function require(field, message)
{
    if (field.value.length == 0)
    {
        return handle_invalid_field(field, message)
    }

    return true
}
