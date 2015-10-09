/**================================================================================
*
* smoothscroll.js
* @package JANStrap
* Version 1.0
*
* Responsible for scroll animation onclicking on an anchor and essential
* for all pages having anchor tag.
*
* Rrequires jQuery
*
**================================================================================**/

$("a[data-url]").click(function(event){
	event.preventDefault();
	elem=$(this).attr('data-url');
	x=$(elem).position();
	$('body, html').stop().animate({scrollTop: x.top-50});
});