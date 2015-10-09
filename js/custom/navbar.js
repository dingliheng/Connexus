/**================================================================================
*
* Navbar.js
* @package JANStrap
* Version 1.0
*
* Responsible for color animation in navgation bar in theme and essential
* for those pages which have the navigation bar
*
* Rrequires jQuery
*
**================================================================================**/

$("#navbar").find("ul > li > a:not(.theme-color)").mouseover(function(){
	$(this).stop().animate({"background-color":"rgb(80, 147, 208)","color":"#fff"},100);
}).mouseout(function(){
	$(this).stop().animate({"background-color":"#fff","color":"rgb(80, 147, 208)"},500);
});
