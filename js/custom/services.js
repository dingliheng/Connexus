/**================================================================================
*
* services.js
* @package JANStrap
* Version 1.0
*
* Responsible for color animation Services Items and essential
* for those pages which have the Services Items i.e. Landing Page 
*
* Rrequires jQuery
*
**================================================================================**/

$("#services").find('.item').mouseover(function(){
	$(this).stop().animate({'background-color':'rgb(80, 147, 208)',color:'#fff'})
	$(this).children("h3, p, i").stop().animate({color:'#fff'});
}).mouseout(function(){
	$(this).stop().animate({'background-color':'#fff',color:'#222'})
	$(this).children("h3, p, i").stop().animate({color:'#222'});
});
