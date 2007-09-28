//  Copyright 2004 The Apache Software Foundation
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
dojo.provide("tapestry.palette");

tapestry.palette={
	
	clearSelections:function(element){
		var options = element.options;
		for (var i = 0; i < options.length; i++) {
			options[i].selected = false;
		}
	},
	
	selectAll:function(element){
		var options = element.options;
  		for (var i = 0; i < options.length; i++) {
    		options[i].selected = true;
  		}
	},
	
	sort:function(element,sorter){
		var options = element.options;
	  	
	  	var list = [];
	  	var index = 0;
	  	var isNavigator = tapestry.isMozilla || !options.remove;
	  	
	  	while (options.length > 0){
	  		var option = options[0];
	        
	        if (isNavigator){
	      		// Can't transfer option in nn4, nn6
	      		var copy = new Option(option.text, option.value);
	      		copy.text = option.text;
	      		copy.value = option.value;
	      		copy.selected = options.selected;
	      		
	      		list[index++] = copy;
	    	} else {
	      		list[index++] = option;
	    	}
	    	
	    	options[0] = null;
	  	}
	  	
	  	list.sort(sorter);
	  	
	  	for (var i = 0; i < list.length; i++){
	    	options[i] = list[i]; 
	    }
	},
	
	labelSorter:function(a, b){
		var a_text = a.text;
	  	var b_text = b.text;
	  
	  	if (a_text == b_text) {return 0;}
	    
	  	if (a_text < b.text) { return -1;}
	  	
	  	return 1;
	},
	
	sortByLabel:function(element){
		this.sort(element, this.labelSorter);
	},
	
	valueSorter:function(a,b){
		var a_value = a.value;
  		var b_value = b.value;
  
  		if (a_value == b_value) { return 0; }
    	
  		if (a_value < b_value) { return -1; }
    
  		return 1;
	},
	
	sortByValue:function(element){
		this.sort(element, this.valueSorter);
	},
	
	transferSelections:function(source, target){
		var sourceOptions = source.options;
  		var targetOptions = target.options;
  
                var oldSourceIndex = source.selectedIndex;
  		var targetIndex = target.selectedIndex;
  		var offset = 0;
  
  		this.clearSelections(target);
  		
  		for (var i=0; i < sourceOptions.length; i++){
    		var option = sourceOptions[i];
    		
    		if (option.selected) {
       			if (tapestry.isMozilla || !sourceOptions.remove){
           			// Can't share options between selects in NN4
           			var newOption = new Option(option.text, option.value, false, true);
           			sourceOptions[i] = null;
          			// Always added to end in NN4
          			targetOptions[targetOptions.length] = newOption;
       			} else {
         			sourceOptions.remove(i);
         			
         			if (targetIndex < 0) {
           				targetOptions.add(option);
         			} else {
           				targetOptions.add(option, targetIndex + offset++);
         			}
      			}
      			
      			i--;
    		}
  		}
                // refresh display in IE - otherwise, option may display empty for a while!
                if (tapestry.isIE) {
                    for (var i=0; i < sourceOptions.length; i++){
                        source.selectedIndex = i;                        
                    }
                    source.selectedIndex = oldSourceIndex;
                    source.selectedIndex = -1;
                }
	},
	
	swapOptions:function(options, selectedIndex, targetIndex){
		var option = options[selectedIndex];

  		// It's very hard to reorder options in NN4
  		if (tapestry.isMozilla || !options.remove){
    		var swap = options[targetIndex];
    
    		var hold = swap.text;
    		swap.text = option.text;
    		option.text = hold;
    
    		hold = swap.value;
    		swap.value = option.value;
    		option.value = hold;
    
    		hold = swap.selected;
    		swap.selected = option.selected;
    		option.selected = hold;
    
    		// defaultSelected isn't relevant to the Palette
    		return;
  		}
  		
  		// Sensible browsers ...
  		options.remove(selectedIndex);
  		options.add(option, targetIndex);
	}
}
