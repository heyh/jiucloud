// JavaScript Document
$(document).ready(function(){
	$( "#jz_payment" ).dialog({
      autoOpen: false,
      width:180,
      modal: true,
      show: {
        effect: "blind",
        duration: 100
      },
      hide: {
        effect: "explode",
        duration: 100
      }
    });
	
});