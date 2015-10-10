/**================================================================================
*
* tooltip.js
* @package JANStrap
* Version 1.0
*
* Responsible for initiating the tooltip of bootstrap manually and 
* essential for all pages having tooltip
*
* Rrequires jQuery
*
**================================================================================**/


$(".ttip").mouseover(function(){
	$(this).tooltip("show");
}).mouseout(function(){
	$(this).tooltip("hide");
});