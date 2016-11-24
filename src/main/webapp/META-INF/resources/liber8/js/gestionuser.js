/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Author : SOFIAA FADDI
 */

 $( function() {
     // deja supprimer tableau 
      
     
     elementsprojects = App.currentVoletElement.split('/')[1];
      $('.addbouton').click(function(){
           
      });
     
    if (typeof elementsprojects  === "undefined") {
        $('.gestionsuser .alert-error').css("display","block");
        $('.gestionsuser .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Veuillez selectionner un projet</span>');
        $('.addbouton').click(function(){
            return false;
        });
     }
    else 
    {   
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
                                if(contenu[0].droit==='admin')
                                    {
                                     
                                       
                                       
                                       
                                        $(".userproject").append('<li><div class="control-group">\n\
                                        <label class="control-label userline" for="selectError'+i+'">'+contenu[i+1].utilisateur+'</label>\n\
                                        <div class="controls userline"><select id="selectError'+i+1+'" data-rel="chosen" name="droit'+i+1+'" class="droit">\n\
                                        <option value="'+contenu[i+1].droit+'">'+contenu[i+1].droit+'</option>\n\
                                        <option value="'+option1+'">'+option1+'</option><option value="'+option2+'">'+option2+'</option></select><a class="remove_field"><input type="hidden" value="'+contenu[i+1].utilisateur+'" class=" add utilisateur'+i+1+'" name="utilisateur'+i+1+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		   
                                    } 
                                    else if((contenu[0].droit==="reporteur") || (contenu[0].droit==="developpeur") ) {
                                       
                                       
                                        $(".userproject").append('<li><div class="control-group">\n\
                                        <label class="control-label userline" for="selectError'+i+'">'+contenu[i+1].utilisateur+'</label>\n\
                                        <div class="controls userline"><input type ="text" value="'+contenu[i+1].droit+'" disabled></div></div>'); //add input box
                                        $(".addbouton").click(function(){
                                            $('.gestionsuser .alert-error').css("display","block");
                                            $('.gestionsuser .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Vous n\'etes pas administrateur, Vous n\'avez pas le droit d\'ajouter un utilisateur. </span>');
                                            return false; 
                                        });
                                        $('#submitaddusrer').click(function(){
                                           $("#close_modal_btn").trigger("click");
                                        });     
                                    }
                            }
                    } 
                    else {
                           
                    }
                }
      
        });
        
     
    
     
   
        $.ajax({ 
           url      : "/Liber8/getUsersNotInProject?nomProjet="+elementsprojects,
          dataType : "json",
          success  : function(data) { 
             
               availableusers = new Array();
               
              for(i=0;i<Object.keys(data.content).length;i++) 
              {     
                availableusers.push(data.content[i].pseudo);
               }
 
                 $( "#usersname" ).autocomplete({
               source: availableusers
             });
               
     
     
          }
      });
      var max_input     = 10; //le nombre maximun des input
      var adduser       = $(".gestionsuser"); // div contenant tout les utilisateurs
      var add_button      = $("#adduserbouton"); //Ajouter bouton id
      $(".closebtn").click(function(e){ //on add input button click
            $('#gestionuser').modal('hide');
             });
            numberinput=0;
            $(add_button).click(function(e){
               
                 e.preventDefault();
                
                if(numberinput < max_input){ //max input box allowed
                    numberinput++; //text box increment
                   
                   var getname=$('#usersname').val();
                     
           	        	if (jQuery.inArray(getname,availableusers) === -1  || getname ==="") {
                            $('#gestionsuser .modal-body .alert-error').css('display','block');
    				                $('.gestionsuser .modal-body .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>Le nom est incorrect ! Réessayer </span>');
  			             	}
                                        
           	        	else {
                                   
                            $('#gestionsuser .modal-body .alert-error').css('display','none');
                            $('#gestionuser').modal('hide');
                            $('#usersname').val("");
           			             $(".userproject").append('<li><div class="control-group">\n\
				                  	<label class="control-label userline" for="selectError'+numberinput+'">'+getname+'</label>\n\
					                   <div class="controls userline"><select id="selectError'+numberinput+'" data-rel="chosen" name="droit'+numberinput+'" class="droit"><option value="admin">admin</option>\n\
					                   <option value="reporteur">reporteur</option><option class="developpeur">developpeur</option></select><a class="remove_field"><input type="hidden" value="'+getname+'" class="add utilisateur'+numberinput+'" name="utilisateur'+numberinput+'"><i class="icon-trash"></i></a></div></div>'); //add input box
                        		for(var i = availableusers.length - 1; i >= 0; i--) {
                                             if(availableusers[i] === getname) {
                                             availableusers.splice(i, 1);
                                            }
                                            
                                        }
                                       
                                    }
            
                    }
                                        
           
        });
        $(adduser).on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); 
                var valeursupprimer = $(this).parents('li').find('.add').val();
                var supprimer = $(this).parents('li').find('input:hidden');
              
                
  
                $(supprimer).empty();;
                
               
                $(this).parents('li').remove(); 
                availableusers.push(valeursupprimer );
                numberinput--;
            });
      
       
  }
    $("#submitaddusrer").click(function(e){
        
            var utilisateur = {};
            var droit = {};
            var i =0;
            var j =0;
            $('input:hidden').each(function(){
                utilisateur[i++] = this.value;
            });
            $('.droit').each(function(){
                droit[j++] = this.value;
            });
             $.ajax({ 
                url      : "/Liber8/gestionUsers?nomProjet="+elementsprojects,
                dataType : "json",
                type     : "POST",
                data     : {
                       nomProjet : elementsprojects,
                       utilisateurs: JSON.stringify(utilisateur),
                       droits: JSON.stringify(droit)
                   },
                           
                success  : function(data) {
                    console.log(data);
                   if(data.errors!==""){
                    
                         $('#projet .modal-content .alert-error').css('display','block');
                          $('#gestionuser .modal-body .alert-error').css('display','none');
                         $('#projet .modal-content .alert-error').html('<i class="icon-exclamation-sign" aria-hidden="true"></i><span>'+data.errors+ '</span>');
                    }
                    else if(data.response==="true"){
                           $("#close_modal_btn").trigger("click");
                    }
                }
            });
            
        });
        $(".onclose").click(function(){
           $('#gestionuser').modal('hide');
        });
       
 });
 
 