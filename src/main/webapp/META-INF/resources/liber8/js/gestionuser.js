/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : SOFIAA FADDI
 */

 $( function() {
    var elementsprojects = App.currentVoletElement.split('/')[1];
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
                      if(data.response ==="true") {
                         
                          var contenu = JSON.parse(data.content);
                         
                          
   
                              
                      for (i =0;i<Object.keys(contenu).length-1; i++) 
                      {
                        
                          if(contenu[0].droit==='admin')
                          {
                             
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
                      console.log("no admin")
                       $('.gestionsuser .alert-error').css("display","block");
        $('.gestionsuser .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Vous n\'etes pas  un administrateur , nous ne pouvez pas changer les droits </span>');
    
                      
                  }
                      }
                      
                      
                      
     } else {
         $('#gestionsuser .alert-error').css('display','block');
                          $('#listuser .modal-body .alert-error').css('display','none');
                         $('#projet .modal-content .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>'+data.errors+ '</span>');
       
     }
         
   
   
     }
      
         });
     }
 });
 