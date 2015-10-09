/**================================================================================
*
* sticky-navbar.js
* @package JANStrap
* Version 1.0
*
* Responsible for making navbar sticky after scrolling to an specific element
* and require for the pages in which navbar has to sticky after scrolling to 
* specific element
*
* Rrequires jQuery
*
**================================================================================**/


/*==============================================================

	Object of element after which navbar have to be sticky

===============================================================*/
var $obj=$('#navbar');


$(function() {
	var navbar_offset_top = $obj.offset().top;
	var sticky_navigation = function(){
		var scroll_top = $(window).scrollTop();
		if (scroll_top > (navbar_offset_top-50)) { 
			$('.navbar').addClass('navbar-fixed-top').css({"margin-top":"0px"});
		} else {
			$('.navbar').removeClass('navbar-fixed-top').css({"margin-top":"-50px"});
		};   
	};
	sticky_navigation();
	$(window).scroll(function() {
		sticky_navigation();
	});
});