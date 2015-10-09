/**================================================================================
*
* social.js
* @package JANStrap
* Version 1.0
*
* Responsible for color animation in social icons in footer of each page 
* and the coming soon page and essential for pages having social icons
*
* Rrequires jQuery
*
**================================================================================**/


/*======================================================================

	Color animation in facebook social icon

=======================================================================*/

$(".facebook").mouseover(function(){
	$(this).stop().animate({color:'#3B5998'})
}).mouseout(function(){
	$(this).stop().animate({color:'rgb(85, 85, 85)'})
});


/*======================================================================

	Color animation in twitter social icon

=======================================================================*/

$(".twitter").mouseover(function(){
	$(this).stop().animate({color:'#00aced'})
}).mouseout(function(){
	$(this).stop().animate({color:'rgb(85, 85, 85)'})
});


/*======================================================================

	Color animation in google-plus social icon

=======================================================================*/
$(".google").mouseover(function(){
	$(this).stop().animate({color:'#dd4b39'})
}).mouseout(function(){
	$(this).stop().animate({color:'rgb(85, 85, 85)'})
});