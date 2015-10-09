/**================================================================================
*
* testimonials.js
* @package JANStrap
* Version 1.0
*
* Responsible for animating testimonials and essential for the pages having testimonials
*
* Rrequires jQuery
*
**================================================================================**/


$(document).ready(function(){
	clientSays();
	setInterval(function(){
			clientSays();
		},8000);
		
});
var clientSay=$(".testimonials");
var cs=0;
function clientSays(){
	$(".testimonial").animate({
		'opacity':"0",
	},200).html($("#testimonial_"+cs).html()).animate({
		'opacity':"1",
	},2000);
	if((cs+1)>=clientSay.length)
		cs=0;
	else
		cs++;
	return 0;
}