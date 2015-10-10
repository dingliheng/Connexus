/**================================================================================
*
* Price-table.js
* @package JANStrap
* Version 1.0
*
* Responsible for color animation and size animations in price table and essential
* for those pages which have the Price Table i.e. Landing Page 
*
* Rrequires jQuery
*
**================================================================================**/


/*=====================================================================

	Color animation on offer cell

=====================================================================*/
$(".price-cell").mouseover(function(){
	$(this).stop().animate({color:'#fff','background-color':'rgb(71, 194, 233)'});
}).mouseout(function(){
	$(this).stop().animate({'background-color':'#fff',color: '#222'});
});


/*=====================================================================

	Size animation on signup cell

=====================================================================*/
$(".price-cell-last").mouseover(function(){
	$(this).stop().animate({'padding':'30px'});
}).mouseout(function(){
	$(this).stop().animate({'padding':'20px'});
});