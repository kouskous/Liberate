/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : SOFIAA FADDI
 */

 $( function() {
     elementsprojects = App.currentVoletElement.split('/')[1];
    if (typeof elementsprojects  === "undefined") {
        $('.gestionsuser .alert-error').css("display","block");
        $('.gestionsuser .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un projet</span>');
        $('.addbouton').click(function(){
            return false;
        });
     }
     else {
        $('.userproject .alert-error').css("display","none");
    
          $.ajax({ 
                 url      : "/Liber8/getUsersInProject?nomProjet="+elementsprojects,
                 dataType : "json",
                 type     : "GET",
                  success  : function(data) {  
                      console.log(data);
                      if(data.response ==="true") {
                         
                          var contenu = JSON.parse(data.content);
                          
                         
                          
   
                              
                      for (i =0;i<Object.keys(contenu).length-1; i++) 
                      {
                        console.log(contenu[0].droit);
                          if(contenu[0].droit==='admin')
                          {
                           
                                 console.log("hh");
                          if(contenu[i+1].droit==='admin')
                          {
                              option1 ='reporteur';
                              option2 = 'developpeur';
                          }
                          else if(contenu[i+1].droit==='reporteur')
                          {
                              option1 ='admin';
                              option2 = 'developpeur';
                          }
                          else if(contenu[i+1].droit==='developpeur')
                          {
                              option1 ='admin';
                              option2 = 'reporteur';
                          }
                          
           $(".userproject").append('<li><div class="control-group">\n\
	<label class="control-label userline" for="selectError'+i+'">'+contenu[i+1].utilisateur+'</label>\n\
					                   <div class="controls userline"><select id="selectError'+i+1+'" data-rel="chosen" name="droit'+i+1+'" class="droit">\n\
                            <option value="'+contenu[i+1].droit+'">'+contenu[i+1].droit+'</option>\n\
					                   <option value="'+option1+'">'+option1+'</option><option value="'+option2+'">'+option2+'</option></select><a class="remove_field"><input type="hidden" value="'+contenu[i+1].utilisateur+'" class="utilisateur'+i+1+'" name="utilisateur'+i+1+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		
                     
                  } else {
                      console.log("no admin");
                      
                      
                      
                  }
                      }
                      
                      
                      
     } else {
         $('#gestionsuser .alert-error').css('display','block');
                          $('#listuser .modal-body .alert-error').css('display','none');
                         $('#projet .modal-content .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>'+data.errors+ '</span>');
       
     }
         
   
   
     }
      
         });
      console.log(elementsprojects);
     
      $.ajax({ 
           url      : "/Liber8/getUsersNotInProject?nomProjet="+elementsprojects,
          dataType : "json",
          success  : function(data) { 
             
               availableusers = new Array();
              for(i=0;i<Object.keys(data.content).length;i++) {
                  
              
         availableusers.push(data.content[i].pseudo);
          
     }
         console.log('adminjdjd');
         console.log(availableusers);
         
            $( "#usersname" ).autocomplete({
               source: availableusers
             });
     
     
     
          }
      });
      var max_input     = 10; //le nombre maximun des input
    var adduser       = $("#adduser"); // div contenant tout les utilisateurs
    var add_button      = $("#adduserbouton"); //Ajouter bouton id
    
     numberinput=0;
            $(add_button).click(function(e){ //on add input button click
                 e.preventDefault();
                
                if(numberinput < max_input){ //max input box allowed
                    numberinput++; //text box increment
                   
                   var getname=$('#usersname').val();
                     
           	        	if (jQuery.inArray(getname,availableusers) === -1  || getname ==="") {
                            $('#gestionsuser .modal-body .alert-error').css('display','block');
    				                $('.gestionsuser .modal-body .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom est incorrect ! Réessayer </span>');
  			             	}
                                        
           	        	else {
                                    console.log("fermer");
                            $('#gestionsuser .modal-body .alert-error').css('display','none');
                            $('#gestionuser').modal('hide');
                            $('#usersname').val("");
           			             $(".userproject").append('<li><div class="control-group">\n\
				                  	<label class="control-label userline" for="selectError'+numberinput+'">'+getname+'</label>\n\
					                   <div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="droit'+numberinput+'" class="droit"><option value="admin">admin</option>\n\
					                   <option value="reporteur">reporteur</option><option class="developpeur">developpeur</option></select><a class="remove_field"><input type="hidden" value="'+getname+'" class="utilisateur'+numberinput+'" name="utilisateur'+numberinput+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		for(var i = availableusers.length - 1; i >= 0; i--) {
    if(availableusers[i] === getname) {
       availableusers.splice(i, 1);
    }
}

            
            }
            
                }
                                        
           
        });
      numberpofelement = $('ul.userproject li').size();
      console.log( numberpofelement);
       
  }
 });
 
 