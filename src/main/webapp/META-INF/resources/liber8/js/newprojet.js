/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : Sofiaa Faddi
 */

$(document).ready(function() {
    var max_input     = 20; //le nombre maximun des input
    var adduser       = $("#adduser"); // div contenant tout les utilisateurs
    var add_button      = $(".addbouton1"); //Ajouter bouton id
    
    numberinput=4;
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(numberinput < max_input){ //max input box allowed
           numberinput++; //text box increment
            $(".usernew").append('<li><div class="control-group">\n\
<label class="control-label userline" for="selectError'+numberinput+'">@user2</label>\n\
<div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="userprojet[]"><option>Modification</option>\n\
<option>Lecture</option></select><a class="remove_field"><i class="icon-trash"></i></a></div></div>'); //add input box
        }
    $(adduser).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parents('li').remove(); numberinput--;
    });
    });
});
    
   
    