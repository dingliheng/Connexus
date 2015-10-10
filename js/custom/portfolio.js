/**================================================================================
*
* Portfolio.js
* @package JANStrap
* Version 1.0
*
* Responsible for animation in Portfolio and display of portfolio on click and essential
* for those pages which have the portfolio i.e. Landing Page 
*
* Rrequires jQuery
*
**================================================================================**/

/*==========================================================
	
	Small Description And Title Display On Mouse Over
	
==========================================================*/
$(".portfolio").mouseover(function(){
	$(this).find(".portfolio-description").stop().slideDown();
}).mouseout(function(){
	$(this).find(".portfolio-description").stop().slideUp();
});

/*==========================================================
	
	Cutting of portfolio description as the size of screen
	
==========================================================*/
var portHeading=Array();
var portDesc=Array();
$.each($(".portfolio-description"),function(){
	portHeading.push($(this).find('h4').text());
	portDesc.push($(this).find('p').text());
	$(this).find('p').text($(this).find('p').text().substr(0,150)+"...");
});

/*==========================================================
	
	If page can view portfolio then displaying whole 
	portfolio on image click
	
==========================================================*/
if($(".portfolio-view").length>0){
	$(".portfolio").click(function(e){
		e.preventDefault();
		$('body, html').stop().animate({scrollTop: $("#portfolio-view").position().top-50});
		$obj=$(this);
		$(".portfolio-view").css({"border":"1px dashed"}).find(".portfolio-view-heading").text($obj.find("h4").text()).parent().find(".portfolio-view-image").
		html("<img src='"+$obj.find(".portfolio-image").find('img').attr('src')+"' class='img-responsive img-thumbnail' alt='Portfolio Image' />");
		$desc="";
		for (var i=0;i<portHeading.length;i++){
			if(portHeading[i]==$obj.find("h4").text()){
				$desc=portDesc[i];
				break;
			}
		}
		$(".portfolio-view").find(".portfolio-view-description").text($desc);
	});
}