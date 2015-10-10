/**================================================================================
*
* Navigation.js
* @package JANStrap
* Version 1.0
*
* Responsible for color animation in navigation arrows from one section to another in theme and essential
* for those pages which have the navigation arrows i.e. Landing Page 
*
* Rrequires jQuery
*
**================================================================================**/


$("#moveDownTop > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'#fff'})
});
$("#moveDownSecond > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'rgb(118, 58, 122)'})
});
$("#moveDownThird > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'#fff'})
});
$("#moveDownFourth > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'#D05A00'})
});
$("#moveDownFifth > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'rgb(2, 136, 173)'})
});
$("#moveDownSixth > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'#fff'})
});
$("#moveUpFirst > a > i").mouseover(function(){
	$(this).stop().animate({color:'#DFD297'})
}).mouseout(function(){
	$(this).stop().animate({color:'#7FCA9F'})
});