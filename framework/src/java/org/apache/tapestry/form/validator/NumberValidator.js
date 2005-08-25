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

Tapestry.validate_min_number = function(event, fieldId, min, message)
{
	var field = this.find(fieldId);
	var value = field.value;
	
	if (value == "") return;
	
    if (Number(value) < min)
      event.invalid_field(field, message)
}

Tapestry.validate_max_number = function(event, fieldId, max, message)
{
	var field = this.find(fieldId);
	var value = field.value;
	
	if (value == "") return;
	
    if (Number(value) > max)
      event.invalid_field(field, message)
}
