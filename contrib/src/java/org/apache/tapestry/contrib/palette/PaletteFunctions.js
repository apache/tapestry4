// $Id: PaletteFunctions.js,v 1.3 2002/05/03 20:03:06 hship Exp $
// Requires: /org/apache/tapestry/html/PracticalBrowserSniffer.js

function palette_clear_selections(element)
{
  var options = element.options;
  
  for (var i = 0; i < options.length; i++)
    options[i].selected = false;
}

function palette_select_all(element)
{
  var options = element.options;

  for (var i = 0; i < options.length; i++)
    options[i].selected = true;
}

function palette_sort(element, sorter)
{
  var options = element.options;
  var list = new Array();
  var index = 0;
  var isNavigator = (navigator.family == "nn4" || navigator.family == "gecko");
  
  while (options.length > 0)
  {
    var option = options[0];
        
    if (isNavigator)
    {
      // Can't transfer option in nn4, nn6
      
     if (navigator.family == 'gecko')
      	var copy = document.createElement("OPTION");
     else
        var copy = new Option(option.text, option.value);

      	copy.text = option.text;
      	copy.value = option.value;
      	copy.selected = options.selected;
      	
      list[index++] = copy;
    }
    else
      list[index++] = option;

    
    options[0] = null;
  }
  
  list.sort(sorter);
  
  for (var i = 0; i < list.length; i++)
  {
    options[i] = list[i]; 
  }


}

function palette_label_sorter(a, b)
{
  var a_text = a.text;
  var b_text = b.text;
  
  if (a_text == b_text)
    return 0;
    
  if (a_text < b.text)
    return -1;
    
  return 1;
}

function palette_sort_by_label(element)
{
  palette_sort(element, palette_label_sorter);
}

function palette_value_sorter(a, b)
{
  var a_value = a.value;
  var b_value = b.value;
  
  if (a_value == b_value)
    return 0;
    
  if (a_value < b_value)
    return -1;
    
  return 1;
}

function palette_sort_by_value(element)
{
  palette_sort(element, palette_value_sorter);
}
  
function palette_transfer_selections(source, target)
{
  var sourceOptions = source.options;
  var targetOptions = target.options;
  
  var targetIndex = target.selectedIndex;
  var offset = 0;
  
  palette_clear_selections(target);
  
  for (var i = 0; i < sourceOptions.length; i++)
  {
    var option = sourceOptions[i];
    
    if (option.selected)
    {

       if (navigator.family == 'nn4' || navigator.family == 'gecko')
       {
           // Can't share options between selects in NN4
           
           var newOption = new Option(option.text, option.value, false, true);
 
           sourceOptions[i] = null;
      
          // Always added to end in NN4
                     
          targetOptions[targetOptions.length] = newOption;
       }
       else
       {  
         sourceOptions.remove(i);
         
         if (targetIndex < 0)
           targetOptions.add(option);
         else
           targetOptions.add(option, targetIndex + offset++);
      }
    
      i--;
    }
  }

}

function palette_swap_options(options, selectedIndex, targetIndex)
{
  var option = options[selectedIndex];

  // It's very hard to reorder options in NN4
  
  if (navigator.family == 'nn4' || navigator.family == 'gecko')
  {
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

